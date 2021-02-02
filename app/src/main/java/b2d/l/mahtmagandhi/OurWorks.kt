package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class OurWorks : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private var avi: ProgressBar? = null
    fun startAnim() {
        //avi.show();
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
        setContentView(R.layout.activity_our_works)
        avi = findViewById(R.id.avi)
        recyclerView = findViewById(R.id.rv_our_works)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
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
        val url = Url.baseurl + "/work_data"
        val jsonRequest = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            Log.d("ashok_ourwork", response.toString())
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), OurWorkResponseModel::class.java)
                if (success) {
                    val adapter: RecyclerView.Adapter<*> = OurWorkAdapter(data)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@OurWorks, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@OurWorks)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@OurWorks, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@OurWorks, error.toString(),
                    ContextCompat.getColor(this@OurWorks, R.color.error), R.drawable.ic_error)
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