package b2d.l.mahtmagandhi

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.multidex.MultiDexApplication


class MyApplication : MultiDexApplication() {
    val TAG = "MyApplication"
    override fun onCreate() {
        super.onCreate()
         val fontRequest =  FontRequest(
                "com.google.android.gms.fonts",
        "com.google.android.gms",
        "Noto Color Emoji Compat",
        R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.init( FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest) )
        val builder = VmPolicy.Builder()
        builder.detectFileUriExposure()
        StrictMode.setVmPolicy(builder.build())

    }
}