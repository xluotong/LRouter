package com.billbook.lib.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.billbook.lib.router.annotation.Autowired
import com.billbook.lib.router.annotation.Route
import com.billbook.lib.router.annotation.Routes
import javax.inject.Inject
import javax.inject.Named

@Routes(
    routes = [Route("/user/login")],
    launcher = LoginLauncher::class
)
class LoginActivity : AppCompatActivity() {

    @Autowired(name = "fromId1")
    lateinit var fromId: String

    @Inject
    lateinit var locationService:LocationService

    val locationService2:LocationService? by LRouter

    @Autowired
    lateinit var toId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LRouter.inject(this)
        setContentView(R.layout.activity_login)
    }
}

class TextFragment : Fragment() {

    @Autowired("extra11")
    lateinit var extra1:String
    @Autowired("extra22")
    lateinit var extra2:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
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