package b2d.l.mahtmagandhi
import com.google.gson.annotations.SerializedName

data class AppointmentModel(
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
        @SerializedName("appt_date")
        val apptDate: String, // 2020-12-28
        @SerializedName("appt_id")
        val apptId: Int, // 4
        @SerializedName("appt_time")
        val apptTime: String, // 11:30 AM - 12:00 AM
        @SerializedName("booked_status")
        val bookedStatus: Int, // 0
        @SerializedName("id")
        val id: Int, // 2
        @SerializedName("message")
        val message: String, // Lets discuss together...
        @SerializedName("user_id")
        val userId: Int // 9
    )
}