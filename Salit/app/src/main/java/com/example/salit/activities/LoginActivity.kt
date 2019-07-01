package com.example.salit.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.salit.EmailValidator
import com.example.salit.R
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setListeners()
    }

    private fun credentialsValidator(userEmail: String, userPassword:String):Boolean{
        var passwordCorrect = false
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(baseContext)
            val userDao = database.UserDao()
            val user = userDao.getUser(userEmail)
            if (user!=null){
                if (user.password == userPassword){
                    passwordCorrect = true
                }
            }
        }
        return passwordCorrect
    }

    private fun userExists(userEmail: String):Boolean{
        var exists = false
        GlobalScope.launch(Dispatchers.IO){
            val user = AppDatabase.getDatabase(baseContext).UserDao().getUser(userEmail)
            if (user != null){
                exists = true
            }
        }
        return exists
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
                    if (userExists(userEmail)){
                        if (credentialsValidator(userEmail, userPassword)){
                            intent.apply {
                                putExtra("EMAIL", userEmail)
                                putExtra("PASSWORD", userPassword)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else{
                            Toast.makeText(this, "Password incorrect", Toast.LENGTH_LONG).show()
                        }
                    } else{
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
}
