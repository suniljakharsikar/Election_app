package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Meetings : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private val mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: LayoutManager? = null
    private var preferences: SharedPreferences? = null
    private var avi: ProgressBar? = null
    private var timeHeading: TextView? = null
    fun startAnim() {
        // avi.show();
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
        setContentView(R.layout.activity_meetings)
        avi = findViewById(R.id.avi)
        timeHeading = findViewById(R.id.textView_month_year_meeting)

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv1)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView!!.setHasFixedSize(true)

        // use a linear layout manager
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
        val x = intArrayOf(1, 2, 3, 3, 3)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
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
        }    }

    private fun fetchData() {
        val url = Url.baseurl + "/meeting_data"
        val jsonRequest = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            Log.d("ashok_meeting", response.toString())
            try {
                if (response.getBoolean("success")) {
                    val data = response.getJSONArray("data")
                    val meetings = ArrayList<Meeting>()
                    for (i in 0 until data.length()) {
                        val jsonObject = data.getJSONObject(i)
                        meetings.add(Meeting(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getString("meeting_date"), jsonObject.getString("meeting_time"), jsonObject.getString("latitude"), jsonObject.getString("longitude")))
                    }
                    val map: MutableMap<String, MutableList<Meeting>> = HashMap()
                    for (i in meetings.indices) {
                        val item = meetings[i]
                        val ms: MutableList<Meeting> = ArrayList()
                        if (map.containsKey(item.meeting_date)) {
                            val ts = map[item.meeting_date]!!
                            ts.add(item)
                            map[item.meeting_date] = ts
                        } else {
                            ms.add(item)
                            map[item.meeting_date] = ms
                        }
                    }
                    Log.d("Meetings", "onResponse: $map")
                    val keys: Set<String> = map.keys
                    val concatAdapter = ConcatAdapter()
                    for (key in keys) {
                        concatAdapter.addAdapter(MeetingHeaderAdapter(key, timeHeading!!))
                        val v: List<Meeting> = map[key]!!
                        concatAdapter.addAdapter(MeetingAdapter(v, this@Meetings, recyclerView))
                    }
                    recyclerView!!.adapter = concatAdapter
                } else {
                    // Toast.makeText(Meetings.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Meetings)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Meetings, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@Meetings, error.toString(),
                    ContextCompat.getColor(this@Meetings, R.color.error), R.drawable.ic_error)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Content-Type"] = "application/json"
                header["token"] = preferences!!.getString(Datas.token, "")!!
                header["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                return header
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun back(view: View?) {
        finish()
    }
}