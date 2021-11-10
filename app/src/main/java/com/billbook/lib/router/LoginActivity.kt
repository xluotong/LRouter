package com.billbook.lib.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.billbook.lib.router.annotation.Route
import com.billbook.lib.router.annotation.Routes

@Routes(
    routes = [Route("/user/login")],
    launcher = LoginLauncher::class
)
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}

class LoginLauncher : Launcher {
    override fun launch(context: Context, intent: Intent, contract: Launcher.Contract) {
        TODO("Not yet implemented")
    }

    override fun createIntent(context: Context, contract: Launcher.Contract): Intent {
        TODO("Not yet implemented")
    }
}