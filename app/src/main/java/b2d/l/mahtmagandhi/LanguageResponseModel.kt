package b2d.l.mahtmagandhi

data class LanguageResponseModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
        val id: Int, // 6
        val name: String // English
    )
}