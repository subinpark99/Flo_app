package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() , LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginLoginBtn.setOnClickListener {
            login()
        }

        binding.loginCloseIv.setOnClickListener {
            finish()
        }
    }

    private fun login() {


        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.loginIdEt.text.toString() + "@" + binding.loginInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()


        val authService= AuthService()
        authService.setLoginView(this)

        authService.login(User(email, password, ""))


    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when(code){
            1000-> {
                saveJwt2(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        binding.loginEmailErrorTv.visibility = View.VISIBLE
    }
}