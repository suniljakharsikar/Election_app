package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class ProblemSuggestion : AppCompatActivity() {
    private var rvProblems: RecyclerView? = null
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
        setContentView(R.layout.activity_problem_suggestion2)
        avi = findViewById(R.id.avi)
        initView()
    }

    private fun initView() {
        rvProblems = findViewById(R.id.rv_probs)
        rvProblems!!.setLayoutManager(LinearLayoutManager(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1010 && data.getBooleanExtra("reload", false)) {
            customSnackBar(rvProblems!!, this@ProblemSuggestion, "Successfully Submitted", ContextCompat.getColor(this@ProblemSuggestion, R.color.success), R.drawable.ic_success)
            // // Toast.makeText(this, "runnind code", // Toast.LENGTH_SHORT).show();
        }
    }

    override fun onResume() {
        super.onResume()
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch (Dispatchers.Main){
                if (isInternet) {

                    fetchData()

                } else {
                    stopAnim()
                    customSnackBar(rvProblems!!, this@ProblemSuggestion, "No Internet Available.", ContextCompat.getColor(this@ProblemSuggestion, R.color.error), R.drawable.ic_error)
                }
            }

        }    }

    fun back(view: View?) {
        finish()
    }

    fun createNewProbSug(view: View?) {
        startActivityForResult(Intent(this, CreateProblemAndSuggestionActivity::class.java), 1010)
    }

    private fun fetchData() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = Url.baseurl + "/psuggestion_data"
        val jsonRequest = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            Log.d("ashok_problem", response.toString())
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), ProblemsResponseModel::class.java)
                if (success) {
                    rvProblems!!.adapter = ProblemSuggestionRecyclerViewAdapter(data as MutableList<ProblemsResponseModel.Data>, "/psuggestion_comment_list", "/psuggestion_comment_post")
                } else {
                    // Toast.makeText(ProblemSuggestion.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@ProblemSuggestion)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@ProblemSuggestion, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(rvProblems!!, this@ProblemSuggestion, error.toString(),
                    ContextCompat.getColor(this@ProblemSuggestion, R.color.error), R.drawable.ic_error)
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
}