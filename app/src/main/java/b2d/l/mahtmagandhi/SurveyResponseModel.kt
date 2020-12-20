package b2d.l.mahtmagandhi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurveyResponseModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("success")
    val success: Boolean
):Parcelable{

@Parcelize
data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_answered")
        val isAnswered: Int,
        @SerializedName("optionsData")
        val optionsData: List<OptionsData>,
        @SerializedName("title")
        val title: String
):Parcelable{
@Parcelize
data class OptionsData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("options_data")
        val optionsData: String
):Parcelable}}