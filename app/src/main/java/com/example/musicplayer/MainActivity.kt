package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        main()
    }

    private fun main() {

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_nav_main) as NavHostFragment
        val nacController = navHost.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationMain, nacController)
        //setupActionBarWithNavController(nacController)
    }
}