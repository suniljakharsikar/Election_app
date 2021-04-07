package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_helpline.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONObject
import java.util.*

class Helpline : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var addressData: ArrayList<AddressData>? = null
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
        setContentView(R.layout.activity_helpline)
        textView42.text = intent.getStringExtra("title")
        avi = findViewById(R.id.avi6)
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv6)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(linearLayoutManager)

        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            if (isInternet) {
                fetchData()

            } else {
                stopAnim()
                customSnackBar(recyclerView!!, this, "No Internet Available.", ContextCompat.getColor(this, R.color.error), R.drawable.ic_error)
            }
        }

    }

    private fun fetchData() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = Url.baseurl + "/helpline"
        val jsonRequest = JSONObject()
        //startAnim();
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            stopAnim()
            Log.d("Helper", "onResponse: $response")
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), HelplineResponseModel::class.java)
                if (success) {
                    val adapter: RecyclerView.Adapter<*> = HelplineAdapter(data)
                    recyclerView!!.adapter = adapter
                } else {
                    // Toast.makeText(Helpline.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Helpline)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Helpline, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //stopAnim();
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@Helpline, error.toString(),
                    ContextCompat.getColor(this@Helpline, R.color.error), R.drawable.ic_error)
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