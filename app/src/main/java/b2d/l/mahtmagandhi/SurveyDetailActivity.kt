package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_survey_detail.*
import org.json.JSONObject
import java.util.HashMap

class SurveyDetailActivity : AppCompatActivity() {
    private var data: SurveyResponseModel.Data? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_detail)

        avi = findViewById(R.id.avi10)
        data = intent.getParcelableExtra<SurveyResponseModel.Data>("data")

        textView_que_survey.text = data?.title

        for (i in data!!.optionsData) {
            val rb = RadioButton(this)
            rb.text = i.optionsData
            rb.tag = i.id.toString()
            rg_options_survey.addView(rb)
        }


        stopAnim()
    }

    fun back(view: View) {
        finish()
    }

    fun submit(view: View) {

        if(rg_options_survey.checkedRadioButtonId==null)
            Toast.makeText(this, "Please select your answer.", Toast.LENGTH_SHORT).show()
        else {
            val url = Url.baseurl + "/survey_response"

            val rbid = rg_options_survey.checkedRadioButtonId
            val radio = findViewById<RadioButton>(rbid)
            val jsr = JSONObject()
            jsr.put("qid", data!!.id)
            if(radio==null) {
                Toast.makeText(this, "Please select your answer.", Toast.LENGTH_SHORT).show()
            return
            }
            jsr.put("aid", radio.tag)
            startAnim()
            val jor = object :JsonObjectRequest(Request.Method.POST, url, jsr, object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {
                    stopAnim()
                    Toast.makeText(this@SurveyDetailActivity, "Success", Toast.LENGTH_SHORT).show()
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@SurveyDetailActivity, error!!.message, Toast.LENGTH_SHORT).show()
                    stopAnim()
                }
            })  {
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
    }
}