package b2d.l.mahtmagandhi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_request_appointment.*
import kotlinx.android.synthetic.main.activity_setting_profile.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RequestAppointmentActivity : AppCompatActivity() {
    private var aptId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_appointment)


        tie_appointment_date_book_app.setOnClickListener {
            val dp =   MaterialDatePicker.Builder.datePicker().build()
            dp.addOnPositiveButtonClickListener {
                val date = Date(it)
                val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
                //formatter.setTimeZone(TimeZone.getTimeZone(""))
                val dateFormatted: String = formatter.format(date)
                tie_appointment_date_book_app.setText(dateFormatted)
            }
            dp.show(supportFragmentManager, "date")
        }

        tie_appointment_date_book_app.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
                val date = formatter.parse(s.toString())
                val transDateFormatter = SimpleDateFormat("yyyy-MM-dd")

                    validDateCheck(transDateFormatter.format(date))
            }
        })


        actv_choose_time.setOnItemClickListener { parent, view, position, id ->
            val item = parent.selectedItem as AppointmentTime
            aptId = item.id
        }

    }

    private fun validDateCheck(date: String) {

        val url = Url.baseurl + "/appt_time_slot_list"
        val jsonObjectRequet = object :JsonObjectRequest(Request.Method.POST, url, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {
                val isSuccess = response!!.optBoolean("success")
                val statusCode = response.optInt("status_code")
                if (isSuccess && statusCode == 200) {
                    val data = response.optJSONObject("data")
                    val names = data.names();

                    var hasDate = false
                    val ts = mutableListOf<AppointmentTime>()

                    for(i in 0..names.length()-1){
                        if (names.get(i).toString().equals(date,false)){
                            hasDate = true
                            val times = data.optJSONArray(names.get(i).toString())
                            for (j in 0..times.length()-1){
                                val aptObj = times.getJSONObject(j)
                                val aptId = aptObj.optInt("appt_id")
                                val apptTime = aptObj.optString("appt_time")
                                ts.add(AppointmentTime(aptId,apptTime))
                            }


                        }
                    }
                    actv_choose_time.setAdapter(ArrayAdapter<AppointmentTime>(baseContext,android.R.layout.simple_spinner_dropdown_item,ts))
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                         if (ts.size==0) {
                             aptId = 0
                             actv_choose_time.setText("", false)
                         }
                         else {
                                aptId = ts.get(0).id
                             actv_choose_time.setText(ts.get(0).time, false)
                         }
                    }


                    if (!hasDate){
                        Snackbar.make(sv_req_app,"Sorry for not available your choice date for appointment. ",Snackbar.LENGTH_LONG).show()
                    }
                   // Log.d("Request", "onResponse: " + key)

                }
            }

        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("RequestAppointment", "onErrorResponse: "+error)
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

        val doa = tie_appointment_date_book_app.text.toString()
        val time = actv_choose_time.text.toString()

        val purpose = tie_appointment_purpose_book_app.text.toString()

         if (doa!!.isEmpty()) Toast.makeText(this, "Please insert your date of appointment.", Toast.LENGTH_SHORT).show()
         if (time!!.isEmpty()) Toast.makeText(this, "Please select your time of appointment.", Toast.LENGTH_SHORT).show()
        else if (purpose!!.isEmpty()) Toast.makeText(this, "Please insert your purpose.", Toast.LENGTH_SHORT).show()
        else{
             val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
             val transDateFor = SimpleDateFormat("yyyy-MM-dd")
             val date = formatter.parse(doa)

             val appt_date = transDateFor.format(date);
             val appt_time = time
             val appt_id = aptId.toString()
             val message = purpose

             val jo = JSONObject()
             jo.put("appt_id",appt_id)
             jo.put("appt_date",appt_date)
             jo.put("appt_time",appt_time)
             jo.put("message",message)

             val url = Url.baseurl + "/appt_booking"

             val jor = object :JsonObjectRequest(Request.Method.POST, url, jo, Response.Listener<JSONObject> {
                 Log.d("Response", "submit: " + it)

             }, object : Response.ErrorListener {
                 override fun onErrorResponse(error: VolleyError?) {
                     Log.d("Response", "onErrorResponse: "+error)
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



    }
}

data class AppointmentTime(val id:Int,val time:String){
    override fun toString(): String {
        return time
    }
}