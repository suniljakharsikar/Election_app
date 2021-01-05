package b2d.l.mahtmagandhi

import NotificationResponseModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notification.*
import org.json.JSONObject
import java.util.HashMap

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        rv_noti.layoutManager = LinearLayoutManager(this)
        fetchData()



    }

    private fun fetchData() {

        val url = Url.baseurl+"/notification_list"

        val jor = object :JsonObjectRequest(Request.Method.POST, url, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {
                Log.d("TAG", "onResponse: "+response)
                val gson = Gson()
                val r = gson.fromJson<NotificationResponseModel>(response.toString(), NotificationResponseModel::class.java)

                if (r.success) {
                    rv_noti.adapter = NotificationAdapter(r.data)
                }

            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("TAG", "onResponse error: "+error)

            }
        }){
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
        finish()
    }
}