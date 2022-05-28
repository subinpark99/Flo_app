package com.example.flo.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySignupBinding
import com.example.flo.ui.song.SongDatabase

class SignUpActivity : AppCompatActivity(), SignupView {

    lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupEndBtn.setOnClickListener {
            signUp()
        }

    }

    private fun getUser(): User {
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupInputEt.text.toString()
        val pwd: String = binding.signupPasswordEt.text.toString()
        val name: String = binding.signupNameEt.text.toString()

        return User(email, pwd,name)
    }


//    private fun signUp() {
//        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupInputEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.signupPasswordEt.text.toString() != binding.signupPwdCheckEt.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//        val users = userDB.userDao().getUsers()
//
//        Log.d("SIGNUPACT", users.toString())
//    }

    private fun signUp(){
        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupNameEt.text.toString().isEmpty() ) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signupPasswordEt.text.toString() != binding.signupPwdCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService= AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        finish()
    }

    override fun onSignUpFailure() {
        binding.signupEmailErrorTv.visibility= View.VISIBLE
    }

}