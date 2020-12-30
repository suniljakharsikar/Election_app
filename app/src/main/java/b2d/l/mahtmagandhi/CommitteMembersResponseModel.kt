package b2d.l.mahtmagandhi
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommitteMembersResponseModel(
        @SerializedName("data")
    val `data`: List<Data>,
        @SerializedName("message")
    val message: String,
        @SerializedName("status_code")
    val statusCode: Int, // 200
        @SerializedName("success")
    val success: Boolean, // true,
        @SerializedName("domain_name")
         val domain_name:String
):Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("description")
        val description: String, // There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.
        @SerializedName("designation")
        val designation: String, // Sr Engineer, Mumbai
        @SerializedName("first_name")
        val firstName: String, // Arun
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("image")
        var image: String, // https://www.ibts.org/wp-content/uploads/2017/08/iStock-476085198-300x300.jpg
        @SerializedName("last_name")
        val lastName: String // Agarwal
    ):Parcelable
}