package b2d.l.mahtmagandhi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SurveyResponseModifyModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    @Parcelize
    data class Data(
            val answered_option_id: Int, // 12
            val id: Int, // 10
            val is_answered: Int, // 1
            val optionsData: List<OptionsData>,
            val title: String // Testing on 06 Feb
    ):Parcelable {
        @Parcelize
        data class OptionsData(
            val id: Int, // 12
            val options_data: String,
            var answer_per:String?    // Testing Survey - Option1
        ):Parcelable
    }
}