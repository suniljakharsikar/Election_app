package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Handler().postDelayed(Runnable {
            imageView_bg_splash.visibility = View.VISIBLE
            Handler().postDelayed(Runnable {
                civ_avtar_splash.setImageResource(R.drawable.profile)
                civ_avtar_splash.performClick()

                Handler().postDelayed(Runnable {
                    group_splash_text.visibility = View.VISIBLE
                    Handler().postDelayed(Runnable {
                        startActivity(Intent(baseContext,LoginActivity::class.java))
                        finish()

                    },1000)
                },2000)

            },1000)
        },1000)




    }
}