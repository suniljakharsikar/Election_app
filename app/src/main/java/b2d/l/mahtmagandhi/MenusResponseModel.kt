package b2d.l.mahtmagandhi

data class MenusResponseModel(
        val `data`: List<Data>,
        val menu_type: Map<Int,String>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
        val icon: String,
        val id: Int, // 1
        val menu: String, // About us 1234
        val type: Int, // 1
        val url: String
    )
}