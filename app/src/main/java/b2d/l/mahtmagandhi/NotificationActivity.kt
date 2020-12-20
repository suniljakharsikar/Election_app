package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        rv_noti.layoutManager = LinearLayoutManager(this)
        rv_noti.adapter = NotificationAdapter()

    }

    fun back(view: View) {
        finish()
    }
}