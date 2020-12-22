package b2d.l.mahtmagandhi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import kotlinx.android.synthetic.main.fragment_image_picker_bottom_sheet.*

class ImagePickerBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_image_picker_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView_add_pic_camera.setOnClickListener {
            (requireActivity() as CreateProblemAndSuggestionActivity).getPic(tag,"c")

            dismiss()
        }

        imageView_add_pic_gallary.setOnClickListener {
            (requireActivity() as CreateProblemAndSuggestionActivity).getPic(tag,"g")

            dismiss()

        }

    }


}