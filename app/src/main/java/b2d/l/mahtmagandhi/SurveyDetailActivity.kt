package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_survey_detail.*

class SurveyDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_detail)

       val data =  intent.getParcelableExtra<SurveyResponseModel.Data>("data")

        textView_que_survey.text = data.title

        for(i in data.optionsData){
            val rb = RadioButton(this)
            rb.text = i.optionsData
            rb.tag = i.id.toString()
            rg_options_survey.addView(rb)
        }
    }

    fun back(view: View) {
        finish()
    }
}