package edu.pw.aicatching.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentCameraBinding
import edu.pw.aicatching.viewModels.GarmentViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private var imageCapture: ImageCapture? = null
    private val viewModel: GarmentViewModel by activityViewModels()
    private lateinit var cameraExecutor: ExecutorService

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                this.context,
                "Permissions not granted.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root
        handleCreateGarmentErrorMessage()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
        binding.captureButton.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()
        return view
    }

    private fun allPermissionsGranted(): Boolean =
        requireContext().checkSelfPermission(REQUIRED_PERMISSIONS) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        this.context?.let { ContextCompat.getMainExecutor(it) }?.let { executor ->
            imageCapture.takePicture(
                executor,
                object : ImageCapture.OnImageCapturedCallback() {

                    @SuppressLint("UnsafeOptInUsageError")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        viewModel.createGarment(image.image?.toByteArray())
                        view?.let { Navigation.findNavController(it).navigate(R.id.garmentDescriptionFragment) }
                    }

                    override fun onError(exc: ImageCaptureException) {
                        exc.message?.let { it1 -> Log.e("Camera:takePhoto", it1) }
                    }
                }
            )
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = this.context?.let { ProcessCameraProvider.getInstance(it) }

        this.context?.let { ContextCompat.getMainExecutor(it) }?.let {
            cameraProviderFuture?.addListener(
                {
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()
                        .also { it1 ->
                            it1.setSurfaceProvider(binding.cameraView.surfaceProvider)
                        }

                    imageCapture = ImageCapture.Builder().build()
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            this, CAMERA_SELECTOR, preview, imageCapture
                        )
                    } catch (exc: IllegalArgumentException) {
                        exc.message?.let { message -> Log.d("Camera:startCamera", message) }
                    }
                },
                it
            )
        }
    }

    private fun handleCreateGarmentErrorMessage() {
        viewModel.mainGarmentErrorMessage.observe(
            viewLifecycleOwner
        ) { Log.i("CameraFragment:onCreateView:createGarment", it) }
    }

    private fun Image.toByteArray(): ByteArray {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return bytes
    }

    companion object {
        private val CAMERA_SELECTOR = CameraSelector.DEFAULT_BACK_CAMERA
        private const val REQUIRED_PERMISSIONS =
            Manifest.permission.CAMERA
    }
}
