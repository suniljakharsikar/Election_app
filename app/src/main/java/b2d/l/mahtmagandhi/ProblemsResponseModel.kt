package b2d.l.mahtmagandhi

import com.google.gson.annotations.SerializedName

data class ProblemsResponseModel(
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
            val description: String, // Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
            @SerializedName("id")
            val id: String, // 1
            @SerializedName("imageArr")
            val imageArr: List<ImageArr>,
            @SerializedName("title")
            val title: String // What is Lorem Ipsum?
    ) {
        data class ImageArr(
                @SerializedName("id")
                val id: Int, // 1
                @SerializedName("image_name")
                val imageName: String, // election.suniljakhar.in/public/upload/problem_suggestion/764902-1604505719_profile.png
                @SerializedName("parent_id")
                val parentId: Int // 3
        )
    }
}