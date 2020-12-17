package b2d.l.mahtmagandhi

import android.os.Bundle
import android.text.InputType
import android.util.StatsLog.logEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_setting_profile.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SettingProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_profile)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        editTextPersonName2.inputType = InputType.TYPE_NULL
        editTextPhone2.inputType = InputType.TYPE_NULL
        editText_postal_code.inputType = InputType.TYPE_NULL
        editText_dob_setting_profile.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_district.inputType = InputType.TYPE_NULL
        button2.visibility = View.INVISIBLE
        tv_edit_profile.setOnClickListener {

            button2.visibility = View.VISIBLE
            editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            editTextPhone2.inputType = InputType.TYPE_CLASS_PHONE
            editText_postal_code.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            //editText_dob_setting_profile.inputType = InputType.TYPE_CLASS_TEXT
            editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_district.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_dob_setting_profile.setOnClickListener {
              val dp =   MaterialDatePicker.Builder.datePicker().build()
                dp.addOnPositiveButtonClickListener {
                    val date = Date(it)
                    val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
                    //formatter.setTimeZone(TimeZone.getTimeZone(""))
                    val dateFormatted: String = formatter.format(date)
                    editText_dob_setting_profile.setText(dateFormatted)
                }
                dp.show(supportFragmentManager,"date")
            }
        }
    }

    fun back(view: View?) {
        finish()
    }
}