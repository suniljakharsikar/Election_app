package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class Home : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    var homelistDatas: ArrayList<HomelistData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val decorView = window.decorView
        //((CardView) findViewById(R.id.cardView3)).setContentPadding(0,140,0,0);
        val TAG = "Home"
        val motionLayout = findViewById<View>(R.id.ml_home) as MotionLayout


/*// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();*/
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv_home)
        // use a linear layout manager
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
        homelistDatas = ArrayList()
        homelistDatas!!.add(HomelistData("About Us", R.drawable.info))
        homelistDatas!!.add(HomelistData("My Appointment", R.drawable.suitcase))
        homelistDatas!!.add(HomelistData("Community Chat", R.drawable.chatboxes))
        homelistDatas!!.add(HomelistData("News & Update", R.drawable.newspaper))
        homelistDatas!!.add(HomelistData("Our Works", R.drawable.groupwork))
        homelistDatas!!.add(HomelistData("Vision & Mission", R.drawable.vision))
        homelistDatas!!.add(HomelistData("Problem / Suggestion", R.drawable.problem))
        homelistDatas!!.add(HomelistData("Manifesto", R.drawable.menifesto))
        homelistDatas!!.add(HomelistData("Survey", R.drawable.sarve))
        homelistDatas!!.add(HomelistData("Committee Member", R.drawable.commetee))
        homelistDatas!!.add(HomelistData("Voting Guide", R.drawable.vote))
        homelistDatas!!.add(HomelistData("Helpline", R.drawable.helpline))
        homelistDatas!!.add(HomelistData("Setting & Profile", R.drawable.setting))
        homelistDatas!!.add(HomelistData("Meetings", R.drawable.suitcase))

        val mAdapter: RecyclerView.Adapter<*> = HomeAdapter(this, recyclerView, homelistDatas,this)
        recyclerView!!.setAdapter(mAdapter)
        findViewById<View>(R.id.imageView_noti_home).setOnClickListener {
            val intent = Intent(applicationContext, NotificationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            //                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
//                finish();
        }
        val cardView = findViewById<CardView>(R.id.cardView3)
        //ml_home.transitionToStart()
        Handler().postDelayed({


            ml_home.transitionToEnd()
            val dip = 48f
            val r = resources
            val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.displayMetrics
            )
            cardView.setContentPadding(0, px.toInt(), 0, 0)
        }, 700)


    }

    fun back(view: View?) {
        finish()
    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}