package b2d.l.mahtmagandhi

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_request_appointment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URLEncoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RequestAppointmentActivity : AppCompatActivity() {
    private  var ts: MutableMap<String, MutableList<AppointmentTime>> = mutableMapOf()
    private lateinit var myCalendar: Calendar
    private var aptId: Int = 0
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
        setContentView(R.layout.activity_request_appointment)
        avi = avi8

        stopAnim()

        myCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }



        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch (Dispatchers.Main){
                if (isInternet) {
                    validDateCheck()

                } else {
                    stopAnim()
                    Utility.customSnackBar(actv_choose_date!!, this@RequestAppointmentActivity, "No Internet Available.", ContextCompat.getColor(this@RequestAppointmentActivity, R.color.error), R.drawable.ic_error)
                }
            }

        }
        actv_choose_date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (ts.containsKey(s.toString()))
                    actv_choose_time.setAdapter(ArrayAdapter<AppointmentTime>(this@RequestAppointmentActivity, android.R.layout.simple_spinner_dropdown_item, ts.get(s.toString())!!.toList()))


            }
        })


        actv_choose_time.setOnItemClickListener { parent, view, position, id ->
            try {
                val item = parent.selectedItem as AppointmentTime
                aptId = item.id
            } catch (e: Exception) {
            }
        }

    }
    private fun updateLabel() {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

           // tie_appointment_date_book_app.setText(sdf.format(myCalendar.time))

    }
    private fun validDateCheck() {
        startAnim()
        val url = Url.baseurl + "/appt_time_slot_list"
        val jsonObjectRequet = object :JsonObjectRequest(Request.Method.POST, url, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {

                val isSuccess = response!!.optBoolean("success")
                val statusCode = response.optInt("status_code")
                if (isSuccess && statusCode == 200) {
                    val data = response.optJSONObject("data")
                    val names = data.names();


                    var hasDate = false


                    for (i in 0..names.length() - 1) {
                        val ds = mutableListOf<AppointmentTime>()
                        val times = data.optJSONArray(names.get(i).toString())
                        for (j in 0..times.length() - 1) {
                            val aptObj = times.getJSONObject(j)
                            val aptId = aptObj.optInt("appt_id")
                            val apptTime = aptObj.optString("appt_time")
                            ds.add(AppointmentTime(aptId, apptTime))

                        }

                        val sdf = SimpleDateFormat("yyyy-mm-dd")
                        val tdf = SimpleDateFormat("dd MMM yyyy")
                        val d = sdf.parse(names.get(i).toString())
                        val currentTime = Calendar.getInstance()
                        if(d.after(currentTime.time)) {
                            ts.put(tdf.format(d), ds)
                        }


                    }
                    val adapter1 = StringAdapter(this@RequestAppointmentActivity, ArrayList<String>(ts.keys))

                    // actv_choose_time.setAdapter(ArrayAdapter<AppointmentTime>(baseContext,android.R.layout.simple_spinner_dropdown_item,ts))
                    actv_choose_date.setAdapter(adapter1)


                    stopAnim()

                    /* if (!hasDate){
                        Snackbar.make(sv_req_app,"Sorry for not available your choice date for appointment. ",Snackbar.LENGTH_LONG).show()
                    }*/
                    // Log.d("Request", "onResponse: " + key)

                }
            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                stopAnim()
                Log.d("RequestAppointment", "onErrorResponse: " + error)
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

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequet)

    }

    fun back(view: View) {
        finish()
    }

    fun submit(view: View) {

        val doa = actv_choose_date.text.toString()
        val time = actv_choose_time.text.toString()

        val purpose = tie_appointment_purpose_book_app.text.toString()

         if (doa!!.isEmpty()) Toast.makeText(this, "Please insert your date of appointment.", Toast.LENGTH_SHORT).show()
         if (time!!.isEmpty()) Toast.makeText(this, "Please select your time of appointment.", Toast.LENGTH_SHORT).show()
        else if (purpose!!.isEmpty()) Toast.makeText(this, "Please insert your purpose.", Toast.LENGTH_SHORT).show()
        else{
             val job = GlobalScope.async {
                 return@async Utility.isInternetAvailable()
             }
             job.invokeOnCompletion {
                 val isInternet = job.getCompleted()
                 GlobalScope.launch (Dispatchers.Main) {
                     if (isInternet) {
                         val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
                         val transDateFor = SimpleDateFormat("yyyy-MM-dd")
                         val date = formatter.parse(doa)

                         val appt_date = transDateFor.format(date);
                         val appt_time = time
                         val appt_id = aptId.toString()
                         val message = purpose

                         val jo = JSONObject()
                         jo.put("appt_id", appt_id)
                         jo.put("appt_date", appt_date)
                         jo.put("appt_time", appt_time)
                         jo.put("message", URLEncoder.encode(message, "UTF-8"))

                         startAnim()
                         val url = Url.baseurl + "/appt_booking"

                         val jor = object : JsonObjectRequest(Request.Method.POST, url, jo, Response.Listener<JSONObject> {
                             Log.d("Response", "submit: " + it)


                             stopAnim()
                             finish()
                         }, object : Response.ErrorListener {
                             override fun onErrorResponse(error: VolleyError?) {
                                 stopAnim()
                                 Log.d("Response", "onErrorResponse: " + error)
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

                         MySingleton.getInstance(baseContext).addToRequestQueue(jor)

                     } else {
                         stopAnim()
                         Utility.customSnackBar(actv_choose_date!!, this@RequestAppointmentActivity, "No Internet Available.", ContextCompat.getColor(this@RequestAppointmentActivity, R.color.error), R.drawable.ic_error)
                     }
                 }
             }

        }



    }
}

data class AppointmentTime(val id: Int, val time: String){
    override fun toString(): String {
        return time
    }
}