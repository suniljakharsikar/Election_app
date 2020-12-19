package b2d.l.mahtmagandhi
import com.google.gson.annotations.SerializedName

data class HelplineResponseModel(
        @SerializedName("data")
    val `data`: List<Data>,
        @SerializedName("message")
    val message: String,
        @SerializedName("status_code")
    val statusCode: Int, // 200
        @SerializedName("success")
    val success: Boolean // true
) {
    data class Data(
        @SerializedName("description")
        val description: String, // Near Village Chowk,Tonk Road, JaipurContact:1234567890
        @SerializedName("id")
        val id: Int, // 2
        @SerializedName("title")
        val title: String // Address 2
    )
}