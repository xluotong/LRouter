package com.billbook.lib.router

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.billbook.lib.router.annotation.Route

@Route(scheme = "example",host = "main",path = "/user/register")
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}