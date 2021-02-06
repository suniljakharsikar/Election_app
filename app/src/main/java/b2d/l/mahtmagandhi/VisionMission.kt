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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

class VisionMission : AppCompatActivity() {
    private var preferences: SharedPreferences? = null
    var textView: TextView? = null
    var imageView: ImageView? = null
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
        setContentView(R.layout.activity_vision_mission)
        avi = findViewById(R.id.avi)


        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/preferences = PreferenceManager.getDefaultSharedPreferences(this)
        textView = findViewById(R.id.tv111)
        imageView = findViewById(R.id.imageView40)
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
                    customSnackBar(textView!!, this@VisionMission, "No Internet Available.", ContextCompat.getColor(this@VisionMission, R.color.error), R.drawable.ic_error)
                }
            }

        }    }

    private fun fetchData() {
        val url = Url.baseurl + "/vision"
        val json = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, json, Response.Listener { response ->
            Log.d("ashok_vison", response.toString())
            try {
                if (response.getBoolean("success")) {
//                        // Toast.makeText(getBaseContext(), "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    val data = response.getJSONArray("data")
                    val jsonObject = data.getJSONObject(0)
                    val description = jsonObject.getString("description")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView!!.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        textView!!.text = Html.fromHtml(description)
                    }
                    // textView.setText(description);
                    val sliderView = findViewById<SliderView>(R.id.imageSlider)
                    val adapter = SliderAdapterExample(this)
                    val data_images = response.getJSONArray("data_images")
                    for (i in 0 until data_images.length()) {
                        adapter.addItem(SliderItem(Url.burl + data_images.getJSONObject(i).getString("image_url")))
                    }
                    if (data_images.length()==0) sliderView.visibility = View.GONE
                    else sliderView.visibility = View.VISIBLE
                    sliderView.setSliderAdapter(adapter)
                    sliderView.setInfiniteAdapterEnabled(false)
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                    //sliderView.setAutoCycleDirection(SliderView.AUTO_C);
                    sliderView.indicatorSelectedColor = Color.WHITE
                    sliderView.indicatorUnselectedColor = Color.GRAY
                    sliderView.scrollTimeInSec = 4 //set
                } else {
                    // Toast.makeText(getBaseContext(), "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            stopAnim()
            customSnackBar(textView!!, this@VisionMission, error.toString(),
                    ContextCompat.getColor(this@VisionMission, R.color.error), R.drawable.ic_error)
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