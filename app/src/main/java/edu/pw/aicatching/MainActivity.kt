package edu.pw.aicatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val showWardrobeButton = findViewById<Button>(R.id.showWardrobeButton)
        val openCameraButton = findViewById<Button>(R.id.openCameraButton)

        openCameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        showWardrobeButton.setOnClickListener {
            val intent = Intent(this, WardrobeActivity::class.java)
            startActivity(intent)
        }
    }
}