package com.billbook.lib.router.internel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.billbook.lib.router.Launcher
import com.billbook.lib.router.Request
import com.billbook.lib.router.RouteInfo
import com.billbook.lib.router.RouteType
import java.lang.RuntimeException

/**
 * @author xluotong@gmail.com
 */
internal class DefaultIntentLauncher : Launcher {

    override fun launch(context: Context, intent: Intent, contract: Launcher.Contract) {
        when (contract.routeInfo.type) {
            RouteType.ACTIVITY -> {
                launchActivity(context, intent, contract)
            }
            RouteType.SERVICE -> {
                context.startService(intent)
            }
        }
    }

    override fun createIntent(context: Context, contract: Launcher.Contract): Intent {
        val intent = Intent(context, contract.routeInfo.targetClass)
        with(contract.request) {
            flags?.let { intent.setFlags(it) }
            flags?.let { intent.setFlags(it) }
            extras?.let { intent.putExtras(it) }
        }
        return intent
    }

    private fun launchActivity(context: Context, intent: Intent, contract: Launcher.Contract) {
        val request = contract.request
        val fragment: Fragment? = request.fragment
        if (request.requestCode != null) {
            when {
                fragment != null -> {
                    fragment.startActivityForResult(intent, request.requestCode!!)
                }
                context is Activity -> {
                    ActivityCompat.startActivityForResult(
                        context,
                        intent,
                        request.requestCode!!,
                        request.options
                    )
                }
                else -> {
                    throw RuntimeException("Unresovled context of request")
                }
            }
        } else {
            if (context !is ContextThemeWrapper) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ActivityCompat.startActivity(context, intent, null)
        }
    }
}

internal data class DefaultContract(
    override val request: Request,
    override val routeInfo: RouteInfo
) : Launcher.Contract


