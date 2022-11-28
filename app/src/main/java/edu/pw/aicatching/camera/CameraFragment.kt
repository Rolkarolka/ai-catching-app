package edu.pw.aicatching.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import edu.pw.aicatching.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*


class CameraFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = inflater.inflate(R.layout.fragment_camera, container, false)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }
        viewBinding.takePhotoButton.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
        return viewBinding
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, java.util.Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = activity?.contentResolver?.let {
            ImageCapture.OutputFileOptions
                .Builder(
                    it,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()
        }

        this.context?.let { ContextCompat.getMainExecutor(it) }?.let { executor ->
            if (outputOptions != null) {
                imageCapture.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedCallback() {

                        override fun onCaptureSuccess(image: ImageProxy) {
                            super.onCaptureSuccess(image)
                            // TODO send to server
                            val bundle = bundleOf("clothCategory" to 1, "clothImage" to "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg")
                            view?.let { Navigation.findNavController(it).navigate(R.id.clothDescriptionFragment, bundle) }
                        }

                        override fun onError(exc: ImageCaptureException) {
                            exc.message?.let { it1 -> Log.e("Camera:takePhoto", it1) }
                        }
                    })
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        this.context?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1, it
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this.context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                            it1.setSurfaceProvider(cameraView.surfaceProvider)
                        }
                    imageCapture = ImageCapture.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            this, cameraSelector, preview, imageCapture
                        )
                    } catch (exc: Exception) {
                        exc.message?.let { message -> Log.d("Camera:startCamera", message) }
                    }
                },
                it
            )
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
