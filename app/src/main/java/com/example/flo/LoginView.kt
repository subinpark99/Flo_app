package com.example.flo

import com.example.flo.Result

interface LoginView {

    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}