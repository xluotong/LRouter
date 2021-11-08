package com.billbook.lib.router

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.billbook.lib.router.annotation.Route

@Route("/user/login")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(v: View) {
        LRouter.navigate("/user/login".toRouteRequest())
    }
}