package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.json.JSONObject
import java.util.*

class Survey : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var avi: ProgressBar? = null
    fun startAnim() {
        //  avi.show();
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
        //        avi.hide();
        // or avi.smoothToHide();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)
        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv4)
        avi = findViewById(R.id.avi9)
        // use a linear layout manager
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
    }

    override fun onResume() {
        super.onResume()
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {
                    fetchData()

                } else {
                    stopAnim()
                    customSnackBar(recyclerView!!, this@Survey, "No Internet Available.", ContextCompat.getColor(this@Survey, R.color.error), R.drawable.ic_error)
                }

            }

        }    }

    private fun fetchData() {
        startAnim()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = Url.baseurl + "/survey"
        val jsonRequest = JSONObject()
        //startAnim();
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            stopAnim()
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), SurveyResponseModifyModel::class.java)
                if (success) {
                    val adapter: RecyclerView.Adapter<*> = Surveydapter(data,)
                    recyclerView!!.adapter = adapter
                } else {
                    // Toast.makeText(Survey.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Survey)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Survey, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //stopAnim();
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@Survey, error.toString(),
                    ContextCompat.getColor(this@Survey, R.color.error), R.drawable.ic_error)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Content-Type"] = "application/json"
                header["token"] = preferences.getString(Datas.token, "")!!
                header["lid"] = preferences.getString(Datas.lagnuage_id, "1")!!
                return header
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun back(view: View?) {
        finish()
    }
}