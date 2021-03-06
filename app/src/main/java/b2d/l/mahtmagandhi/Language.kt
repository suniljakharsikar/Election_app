package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Language : AppCompatActivity() {
    var listView: ListView? = null
    private var preferences: SharedPreferences? = null
    private var listadapter: MyLangAdapter? = null
    private var avi: ProgressBar? = null
    private val langIds = arrayListOf<Int>()
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

    override fun onStart() {
        super.onStart()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val loginstatus = preferences.getBoolean(Datas.language_selected, false)
        if (loginstatus) {
            startActivity(Intent(this@Language, Home::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        avi = findViewById(R.id.avi)

        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
//        hideSystemUI();
        listView = findViewById(R.id.language_list)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(Language.this, "test", // Toast.LENGTH_SHORT).show();
            }
        });*/stopAnim()
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
                    customSnackBar(listView!!, this@Language, "No Internet Available.", ContextCompat.getColor(this@Language, R.color.error), R.drawable.ic_error)
                }
            }

        }    }

    private fun fetchData() {
        val url = Url.baseurl + "/language_list"
        val jsonRequest = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), LanguageResponseModel::class.java)
                if (success) {
                    val strings = mutableListOf<String>()
                    for (i in data){
                        strings.add(i.name)
                        langIds.add(i.id)
                    }


                    listadapter = MyLangAdapter(this@Language, strings.toTypedArray())
                    listView!!.adapter = listadapter
                } else {
                    // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Language)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Language, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            customSnackBar(listView!!, this@Language, error.toString(),
                    ContextCompat.getColor(this@Language, R.color.error), R.drawable.ic_error)
            stopAnim()
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Token"] = preferences!!.getString(Datas.token, "")!!
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    fun back(view: View?) {
        finish()
    }

    fun submit(view: View?) {
        /* String index = String.valueOf(listadapter.getselected() + 1);
        String url = Url.baseurl + "/language_update'";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("langId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    if (success) {
                        startActivity(new Intent(Language.this, Home.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(Language.this, "" + error, // Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Token", preferences.getString(Datas.token, ""));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);*/
        val url = Url.baseurl + "/language_update"
        val jsonRequest = JSONObject()
        if (langIds.size==0)return
        val index = (langIds.get(listadapter!!.getselected())).toString()
        try {
            jsonRequest.put("langId", index)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            try {
                val success = response.getBoolean("success")
                if (success) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Language)
                    val editor = preferences.edit()
                    editor.putBoolean(Datas.language_selected, true)
                    editor.putString(Datas.lagnuage_id, index)
                    editor.apply()
                    startActivity(Intent(this@Language, Home::class.java))
                    finish()
                } else {
                    // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Language)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Language, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener {
            stopAnim()
            // Toast.makeText(Language.this, "" + error, // Toast.LENGTH_SHORT).show();
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = preferences!!.getString(Datas.token, "")!!
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}