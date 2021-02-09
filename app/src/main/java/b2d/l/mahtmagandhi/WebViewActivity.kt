package b2d.l.mahtmagandhi

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        imageView60.setOnClickListener {
            onBackPressed()
        }

    val url =     intent.getStringExtra("url")
    val title =   intent.getStringExtra("title")

        textView_name_webView.text = title
        val myWebView = findViewById<View>(R.id.webView) as WebView
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        myWebView.loadUrl(url)
    }


}