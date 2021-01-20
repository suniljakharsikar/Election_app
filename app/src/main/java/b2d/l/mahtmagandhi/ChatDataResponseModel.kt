package b2d.l.mahtmagandhi

data class ChatDataResponseModel(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int, // 200
    val success: Boolean // true
) {
    data class Data(
        var UnlikeStatus: Int, // 0
        val commentCount: Int, // 0
        val description: String, // cross check 
        var dislike: Int, // 0
        val id: Int, // 37
        val imageData: List<ImageData>,
        val image_name: Any?, // null
        var likeStatus: Int, // 0
        var likes: Int, // 0
        val title: Any?, // null
        val userData: List<UserData>,
        val user_id: Int, // 15
        val created_at: String,
        var imgAdapter:ImagePagerAdapter?
    ) {
        data class ImageData(
            val id: Int, // 3
            val image_name: String, // /public/upload/ctalks/993112-p.jpg
            val parent_id: Int // 37
        )

        data class UserData(
            val id: Int, // 15
            val user_image: String?,
            val user_name: String // lotu
        )
    }
}