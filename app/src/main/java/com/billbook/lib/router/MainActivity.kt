package com.billbook.lib.router

import android.app.Service
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.billbook.lib.router.annotation.Route
import com.billbook.lib.router.annotation.Routes

@Route("/main/home")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LRouterInitializer.initialize(application)
        setContentView(R.layout.activity_main)
    }

    fun login(v:View){
        LRouter.navigateTo("/user/login".toRouteRequest())
    }

    fun register(v:View){
        LRouter.navigateTo("example://main/user/register".toRouteRequest())
    }
}

@Route("/main/second", interceptors = [LoginInterceptor::class, AuthInterceptor::class])
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Routes(
    [Route("/main/third", interceptors = [LoginInterceptor::class])],
    name = "Login",
    interceptors = [LoginInterceptor::class,AuthInterceptor::class,AuthInterceptor::class]
)
class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}


class LoginInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }
}

@Route("/main/main_service")
class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}