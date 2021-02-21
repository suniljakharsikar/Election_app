package b2d.l.mahtmagandhi

data class SurveyResponseResultModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
        val answer_id: Int, // 12
        val qusetion_id: Int, // 10
        val result: String, // 58.33
        val totalResopnse: Int, // 7
        val totalResponseCount: Int // 12
    )
}