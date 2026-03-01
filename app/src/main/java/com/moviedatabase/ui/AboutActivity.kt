package com.moviedatabase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviedatabase.BuildConfig
import com.example.moviedatabase.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dynamically set the version from build.gradle.kts
        binding.tvAppVersion.text = "Version: ${BuildConfig.VERSION_NAME}"
    }
}