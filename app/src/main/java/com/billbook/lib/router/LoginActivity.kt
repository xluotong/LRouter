package com.billbook.lib.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.billbook.lib.router.annotation.Route

@Route("/user/login")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}