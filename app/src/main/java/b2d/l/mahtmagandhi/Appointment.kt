package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList
import android.view.View
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import java.util.HashMap

class Appointment : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var empty_message: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        recyclerView = findViewById<RecyclerView>(R.id.rv_appointment)
        val la: LayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(la)


        empty_message = findViewById(R.id.textView7)
        empty_message!!.setVisibility(View.INVISIBLE)
        fetchData()
    }

    private fun fetchData() {
        val url = Url.baseurl + "/appt_history"
        val jor = object :JsonObjectRequest(Request.Method.POST, url, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {


                Log.d("Appointment", "onResponse: "+response)
                val gson = Gson()
                val am = gson.fromJson<AppointmentModel>(response.toString(), AppointmentModel::class.java)

                if (am.success) {
                    val data = am.data
                    if (data.size==0)       empty_message!!.setVisibility(View.VISIBLE)



                    val adapter = AppointmentAdapter(data)
                    recyclerView!!.adapter = adapter

                }
            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {

            }
        })
        {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                var preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

                headers["Token"] = preferences.getString(Datas.token, "").toString()
                headers["lid"] = preferences.getString(Datas.lagnuage_id, "1").toString()
                return headers
            }

        }

        MySingleton.getInstance(baseContext).addToRequestQueue(jor)


    }
    fun back(view: View?) {
        finish()
    }

    fun new_appointment(view: View?) {
        startActivity(Intent(baseContext, RequestAppointmentActivity::class.java))
    }
}