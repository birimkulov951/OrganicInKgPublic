package com.example.organicinkgpublic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    /** SplashScreen Constant*/
    private val SPLASH_TIME_OUT: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*// Status Bar icon color
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val window: Window = this.window
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            window.decorView.systemUiVisibility = View.STATUS_BAR_HIDDEN
            //window.statusBarColor = Color.TRANSPARENT
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            // todo test this
            //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }*/



        // Anim
        Handler(Looper.getMainLooper()).postDelayed({

            val alphaAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.alpha_anim)
            invisible_logos.startAnimation(alphaAnim)

            visible_logo.visibility = View.GONE
            invisible_logos.visibility = View.VISIBLE
            invisible_logos_2.visibility = View.VISIBLE

            invisible_logos.startAnimation(alphaAnim)
            invisible_logos_2.startAnimation(alphaAnim)

        }, 1100)


        Handler(Looper.getMainLooper()).postDelayed({

            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this,MainActivity::class.java))
            // close this activity
            finish()

        }, SPLASH_TIME_OUT)

    }

}
