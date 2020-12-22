package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_request_appointment.*
import kotlinx.android.synthetic.main.activity_setting_profile.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RequestAppointmentActivity : AppCompatActivity() {
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
    }

    fun back(view: View) {
        finish()
    }

    fun submit(view: View) {

     val name = tie_appointment_name_book_app.text
        val mobileNo = tie_appointment_mobile_book_app.text
        val doa = tie_appointment_date_book_app.text
        val purpose = tie_appointment_purpose_book_app.text
        if (name!!.isEmpty()) Toast.makeText(this, "Please insert your name.", Toast.LENGTH_SHORT).show()
        else if (mobileNo!!.isEmpty()) Toast.makeText(this, "Please insert your mobile no.", Toast.LENGTH_SHORT).show()
        else if (doa!!.isEmpty()) Toast.makeText(this, "Please insert your date of appointment.", Toast.LENGTH_SHORT).show()
        else if (purpose!!.isEmpty()) Toast.makeText(this, "Please insert your purpose.", Toast.LENGTH_SHORT).show()
        else{

        }



    }
}