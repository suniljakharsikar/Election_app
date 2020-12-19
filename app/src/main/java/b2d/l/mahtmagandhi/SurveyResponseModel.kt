package b2d.l.mahtmagandhi
import com.google.gson.annotations.SerializedName

data class SurveyResponseModel(
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
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("title")
        val title: String // Survey Question 1
    )
}