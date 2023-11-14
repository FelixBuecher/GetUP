package com.crix.getup.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crix.getup.databinding.ActivityLoginBinding
import com.crix.getup.ui.mainnavigation.HomeActivity
import com.crix.getup.util.Auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var auth = Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener {
            val userName = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            auth.login(userName, password).addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.btRegister.setOnClickListener {
            val userName = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            auth.register(userName, password).addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}