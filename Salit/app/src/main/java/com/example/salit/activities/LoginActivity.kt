package com.example.salit.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab.EmailValidator
import com.example.salit.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setListeners()
    }

    private fun setListeners(){
        loginButton.setOnClickListener {
            val userEmail = emailEditText.text.toString()
            val userPassword = passwordEditText.text.toString()
            when{
                !EmailValidator.isValidEmail(userEmail) ->
                    Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show()
                userPassword.isNullOrEmpty() ->
                    Toast.makeText(this, "Empty password", Toast.LENGTH_LONG).show()
                else -> {
                    intent.apply {
                        putExtra("EMAIL", userEmail)
                        putExtra("PASSWORD", userPassword)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}
