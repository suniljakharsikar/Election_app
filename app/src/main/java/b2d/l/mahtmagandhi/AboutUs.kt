package b2d.l.mahtmagandhi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AboutUs : AppCompatActivity() {
    private var preferences: SharedPreferences? = null
    var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        avi = findViewById(R.id.avi)
        /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        val adapter = SliderAdapterExample(this)


        /*adapter.addItem(new SliderItem());
        adapter.addItem(new SliderItem());
        adapter.addItem(new SliderItem());
        sliderView.setSliderAdapter(adapter);*/textView = findViewById(R.id.textView16)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = Url.baseurl + "/about_us"
        val jsonRequest = JSONObject()
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {

                    startAnim()
                    val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
                        Log.d("ashok", response.toString())
                        try {
                            if (response.getBoolean("success")) {
                                sliderView.visibility = View.VISIBLE
                                if(response.getJSONArray("data").length()==0)sliderView.visibility  = View.GONE
                                val data = response.getJSONArray("data").getJSONObject(0)
                                val title = data.getString("title")
                                val description = data.getString("description")
                                textView!!.setText(description)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    textView!!.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT))
                                } else {
                                    textView!!.setText(Html.fromHtml(description))
                                }
                                val data_images = response.getJSONArray("data_images")
                                for (i in 0 until data_images.length()) {
                                    adapter.addItem(SliderItem(Url.burl + data_images.getJSONObject(i).getString("image_url")))
                                }
                                if (data_images.length()==0) sliderView.visibility = View.GONE
                                sliderView.setSliderAdapter(adapter)
                                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                                //sliderView.setAutoCycleDirection(SliderView.AUTO_C);
                                sliderView.indicatorSelectedColor = Color.WHITE
                                sliderView.indicatorUnselectedColor = Color.GRAY
                                sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
                                //sliderView.startAutoCycle();
//                        Log.d("ashok", data.toString());
                            } else {
                                // Toast.makeText(AboutUs.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                                //login page
                                val preferences = PreferenceManager.getDefaultSharedPreferences(this@AboutUs)
                                val editor = preferences.edit()
                                editor.clear()
                                editor.apply()
                                startActivity(Intent(this@AboutUs, LoginActivity::class.java))
                            }
                            Log.d("ashok", response.getString("message"))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stopAnim()
                    }, Response.ErrorListener { error ->
                        customSnackBar(sliderView, this@AboutUs, error.toString(), ContextCompat.getColor(this@AboutUs, R.color.error), R.drawable.ic_error)
                        // // Toast.makeText(AboutUs.this, "" + error, // Toast.LENGTH_SHORT).show();
                        // Log.d("ashok", error.toString());
                        stopAnim()
                    }) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val headers: MutableMap<String, String> = HashMap()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = preferences!!.getString(Datas.token, "")!!
                            headers["lid"] = preferences!!.getString(Datas.lagnuage_id, "1")!!
                            Log.d("ashok", preferences!!.getString(Datas.lagnuage_id, "1"))
                            return headers
                        }
                    }
                    MySingleton.getInstance(this@AboutUs).addToRequestQueue(jsonObjectRequest)
                } else {
                    stopAnim()
                    customSnackBar(sliderView, this@AboutUs, "No Internet Available.", ContextCompat.getColor(this@AboutUs, R.color.error), R.drawable.ic_error)
                }
            }

        }
    }







    private var avi: ProgressBar? = null
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

    fun back(view: View?) {
        finish()
    }
}