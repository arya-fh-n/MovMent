package com.arfdevs.myproject.movment.presentation.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arfdevs.myproject.movment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}