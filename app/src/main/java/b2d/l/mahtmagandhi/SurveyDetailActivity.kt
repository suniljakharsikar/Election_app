package b2d.l.mahtmagandhi

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_survey_detail.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*


class SurveyDetailActivity : AppCompatActivity() {
    private var data: SurveyResponseModifyModel.Data? = null

    private var avi: ProgressBar? = null

    fun startAnim() {
       // avi!!.show()
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
        data = intent.getParcelableExtra<SurveyResponseModifyModel.Data>("data")

        textView_que_survey.text = data?.title

        if (data !=null && data!!.is_answered==1)button_submit2.visibility = View.GONE


        for (i in data!!.optionsData) {
            val rb = RadioButton(this)
            rb.setPadding(0, 32, 0, 32)
            rb.text = i.options_data
            rb.tag = i.id.toString()
            rb.id = i.id

            val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(15, 20, 15, 20)
            rb.setLayoutParams(params)
            rg_options_survey.addView(rb)


        }
        rg_options_survey.check(data!!.answered_option_id)


        stopAnim()
    }

    fun back(view: View) {
        finish()
    }

    fun submit(view: View) {

        if(rg_options_survey.checkedRadioButtonId==null)
            Toast.makeText(this, "Please select your answer.", Toast.LENGTH_SHORT).show()
        else {
            val job = GlobalScope.async {
                return@async Utility.isInternetAvailable()
            }
            job.invokeOnCompletion {
                val isInternet = job.getCompleted()
                GlobalScope.launch (Dispatchers.Main) {
                    if (isInternet) {
                        val url = Url.baseurl + "/survey_response"

                        val rbid = rg_options_survey.checkedRadioButtonId
                        val radio = findViewById<RadioButton>(rbid)

                        if (radio == null) {
                            Toast.makeText(this@SurveyDetailActivity, "Please select your answer.", Toast.LENGTH_SHORT).show()

                        } else {
                            val jsr = JSONObject()
                            jsr.put("qid", data!!.id)
                            jsr.put("aid", radio.tag)
                            startAnim()
                            val jor = object : JsonObjectRequest(Request.Method.POST, url, jsr, object : Response.Listener<JSONObject> {
                                override fun onResponse(response: JSONObject?) {
                                    stopAnim()
                                    customSnackBar(rg_options_survey, this@SurveyDetailActivity, "Submit Successfully",
                                            ContextCompat.getColor(this@SurveyDetailActivity, R.color.success), R.drawable.ic_success)
                                    GlobalScope.launch(Dispatchers.Main) {
                                        delay(2000)
                                        finish()
                                    }
                                }
                            }, object : Response.ErrorListener {
                                override fun onErrorResponse(error: VolleyError?) {
                                    customSnackBar(rg_options_survey, this@SurveyDetailActivity, error.toString(),
                                            ContextCompat.getColor(this@SurveyDetailActivity, R.color.error), R.drawable.ic_error)
                                    stopAnim()
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

                            MySingleton.getInstance(this@SurveyDetailActivity).addToRequestQueue(jor)
                        }
                    } else {
                        stopAnim()
                        customSnackBar(rg_options_survey!!, this@SurveyDetailActivity, "No Internet Available.", ContextCompat.getColor(this@SurveyDetailActivity, R.color.error), R.drawable.ic_error)
                    }
                }
            }

        }
    }
}