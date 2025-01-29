package com.example.cookingguideapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome) // Updated to use the dedicated activity layout

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.welcome_fragment_container, WelcomeFragment())
                .commit()
        }
    }
}
