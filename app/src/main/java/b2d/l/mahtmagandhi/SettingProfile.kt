package b2d.l.mahtmagandhi

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting_profile.*

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
        editTextNumberSigned.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_district.inputType = InputType.TYPE_NULL
        tv_edit_profile.setOnClickListener {
            editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            editTextPhone2.inputType = InputType.TYPE_CLASS_PHONE
            editText_postal_code.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editTextNumberSigned.inputType = InputType.TYPE_CLASS_TEXT
            editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_district.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
        }
    }

    fun back(view: View?) {
        finish()
    }
}