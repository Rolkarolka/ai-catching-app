package edu.pw.aicatching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import androidx.camera.core.*
import java.util.concurrent.ExecutorService
import android.os.Build
import androidx.camera.lifecycle.ProcessCameraProvider
import edu.pw.aicatching.databinding.FragmentCameraBinding
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*


class CameraFragment : Fragment() {
    private lateinit var viewBinding: FragmentCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = inflater.inflate(R.layout.fragment_camera, container, false)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }
        viewBinding.image_capture_button.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
        return viewBinding
    }

    private fun takePhoto() {}


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        this.context?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1, it)
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this.context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = this.context?.let { ProcessCameraProvider.getInstance(it) }

        this.context?.let { ContextCompat.getMainExecutor(it) }?.let {
            cameraProviderFuture?.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also { it1 ->
                        it1.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview)

                } catch(exc: Exception) {
    //                Log.e(TAG, "Use case binding failed", exc)
                    println("Error")
                }

            }, it)
        }
    }



    companion object {
        private const val TAG = "AICatching"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
