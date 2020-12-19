package b2d.l.mahtmagandhi
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class OurWorkResponseModel(
        @SerializedName("data")
    val `data`: List<Data>,
        @SerializedName("message")
    val message: String,
        @SerializedName("status_code")
    val statusCode: Int, // 200
        @SerializedName("success")
    val success: Boolean // true
) {@Parcelize
    data class Data(
            @SerializedName("data_images")
        val dataImages: List<DataImage>,
            @SerializedName("description")
        val description: String, // Lorem Ipsum is simply dummy text of the printing and typesetting industry.
            @SerializedName("id")
        val id: Int, // 1
            @SerializedName("likeStatus")
        val likeStatus: Int, // 0
            @SerializedName("title")
        val title: String, // Road Development
            @SerializedName("UnlikeStatus")
        val unlikeStatus: Int // 0
    ):Parcelable {
    @Parcelize
    data class DataImage(
            @SerializedName("id")
            val id: Int, // 1
            @SerializedName("image_name")
            val imageName: String, // https://starofmysore.com/wp-content/uploads/2017/10/mahatma-gandhiji.jpg
            @SerializedName("image_url")
            val imageUrl: String,
            @SerializedName("parent_id")
            val parentId: Int // 1
        ):Parcelable
    }
}