package b2d.l.mahtmagandhi

data class NewsUpdateResponseModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
        var UnlikeStatus: Int, // 0
        val description: String, // <p><strong>Lorem Ipsum</strong>&nbsp;is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry&#39;s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>
        var dislike: Int, // 0
        val id: Int, // 16
        val image_name: Any?, // null
        var likeStatus: Int, // 0
        var likes: Int, // 0
        val title: String, // Testing
        val userData: List<UserData>
    ) {
        data class UserData(
            val id: Int, // 15
            val user_image: String,
            val user_name: String // lotu
        )
    }
}