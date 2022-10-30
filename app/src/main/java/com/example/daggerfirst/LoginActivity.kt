package com.example.daggerfirst

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.daggerfirst.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // setting up the viewModel and Login Button Listener
        val myViewModel: LoginViewModel by viewModels()
        val loginButton: ImageView = findViewById(R.id.button_login_git_user)
        val progressBarLogin: LottieAnimationView = findViewById(R.id.progress_bar_login)
        val editTextUserName: EditText = findViewById(R.id.editText_github_username)

        loginButton.setOnClickListener {
            loginButton.visibility = View.INVISIBLE
            progressBarLogin.visibility = View.VISIBLE
            val enteredUserName = editTextUserName.text.toString().trim()
            if (enteredUserName.isNotEmpty()) {
                myViewModel.getUserDetails(enteredUserName)
            }
        }

        // observing the login States stored in the ViewModel
        // and changing accordingly
        myViewModel.getLoginStates().observe(this) {
            if (it != null) {
                loginButton.visibility = View.VISIBLE
                progressBarLogin.visibility = View.INVISIBLE
                val mySharedPreferences =
                    getSharedPreferences(getString(R.string.shared_preference_key), MODE_PRIVATE)
                mySharedPreferences.edit()
                    .putString(getString(R.string.sp_github_key), it.login.lowercase()).apply()

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                // displaying the invalid user name error message
                // for 5 seconds
                loginButton.visibility = View.VISIBLE
                progressBarLogin.visibility = View.INVISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    val errorText: TextView = findViewById(R.id.tv_error_text)
                    errorText.visibility = View.VISIBLE
                    delay(5000)
                    errorText.visibility = View.INVISIBLE
                }
            }
        }
    }
}