package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_committee_member.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class CommitteeMember : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var committeeMemberData: ArrayList<CommitteeMemberData>? = null
    private var preferences: SharedPreferences? = null
    private var avi: ProgressBar? = null
    fun startAnim() {
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
        setContentView(R.layout.activity_committee_member)
        textView56.text = intent.getStringExtra("title")
        avi = findViewById(R.id.avi)

        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/recyclerView = findViewById(R.id.rv4)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        //        committeeMemberData.add(new CommitteeMemberData("Vinita Kumari", "Sarpanch Jhotwara", R.drawable.pro1));
//        committeeMemberData.add(new CommitteeMemberData("Ravindra Kumar", "Advocate", R.drawable.pro2));
//        committeeMemberData.add(new CommitteeMemberData("Dr. Vinay", "Doctor Child Speciality", R.drawable.pro3));
//        CommitteeMemberAdapter committeeMemberAdapter = new CommitteeMemberAdapter(this, committeeMemberData);
//        recyclerView.setAdapter(committeeMemberAdapter);
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            if (isInternet) {
                fetchCommitee()

            } else {
                stopAnim()
                customSnackBar(recyclerView!!, this@CommitteeMember, "No Internet Available.", ContextCompat.getColor(this@CommitteeMember, R.color.error), R.drawable.ic_error)
            }
        }

    }

    private fun fetchCommitee() {
        val url = Url.baseurl + "/committee_data"
        val json = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
            Log.d("ashok_comm", response.toString())
            try {
                if (response.getBoolean("success")) {
                    /* JSONArray data = response.getJSONArray("data");
                          committeeMemberData = new ArrayList<>();
                          for (int i = 0; i < data.length(); i++) {
                              JSONObject jsonObject = data.getJSONObject(i);
  
                              CommitteeMemberData c = gson.fromJson(jsonObject.toString(), CommitteeMemberData.class);
                              committeeMemberData.add(c);
                          }*/
                    val gson = Gson()
                    val (data, _, _, _, domain_name) = gson.fromJson(response.toString(), CommitteMembersResponseModel::class.java)
                    val committeeMemberAdapter1 = CommitteeMemberAdapter(this@CommitteeMember, data, domain_name)
                    recyclerView!!.adapter = committeeMemberAdapter1
                } else {
                    // // // Toast.makeText(CommitteeMember.this, "" + response.getString("message"), // // // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@CommitteeMember)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@CommitteeMember, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(recyclerView!!, this@CommitteeMember, error.toString(),
                    ContextCompat.getColor(this@CommitteeMember, R.color.error), R.drawable.ic_error)
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