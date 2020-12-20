package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class RequestAppointmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_appointment)
    }

    fun back(view: View) {
        finish()
    }
}