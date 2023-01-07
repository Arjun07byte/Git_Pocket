package com.example.daggerfirst.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.daggerfirst.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redirectActivity()
    }

    private fun redirectActivity() {
        val mySharedPreferences =
            getSharedPreferences(getString(R.string.shared_preference_key), MODE_PRIVATE)
        val currentLoggedInUser =
            mySharedPreferences.getString(getString(R.string.sp_github_key), null)
        if (currentLoggedInUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }


}