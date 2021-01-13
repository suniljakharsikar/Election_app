package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_notification.*
import org.json.JSONObject
import java.util.*

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        avi = findViewById(R.id.avi)

        rv_noti.layoutManager = LinearLayoutManager(this)
        fetchData()


    }

    private var avi: AVLoadingIndicatorView? = null

    fun startAnim() {
        avi!!.show()
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
//        avi.hide();
        // or avi.smoothToHide();
    }

    private fun fetchData() {

        val url = Url.baseurl + "/notification_list"

        startAnim()
        val jor = object : JsonObjectRequest(Request.Method.POST, url, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "onResponse: " + response)
                val gson = Gson()
                val r = gson.fromJson<NotificationResponseModel>(response.toString(), NotificationResponseModel::class.java)

                if (r.success) {
                    rv_noti.adapter = NotificationAdapter(r.data)
                }

                stopAnim()
            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                stopAnim()
                Toast.makeText(this@NotificationActivity, "" + error.toString(), Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onResponse error: " + error)

            }
        }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                var preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

                headers["Token"] = preferences.getString(Datas.token, "").toString()
                headers["lid"] = preferences.getString(Datas.lagnuage_id, "1").toString()
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jor)

    }

    fun back(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
//        startActivity(Intent(baseContext, Home::class.java))
        finish()
    }
}