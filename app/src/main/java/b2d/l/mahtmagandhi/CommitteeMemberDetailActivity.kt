package b2d.l.mahtmagandhi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_committee_member_detail.*

class CommitteeMemberDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_committee_member_detail)

       val dataGet =  intent.getParcelableExtra<CommitteMembersResponseModel.Data>("data")
               tv_avtar_desc_committe.text = dataGet.description
        tv_avtar_name_committe.text = "${dataGet.firstName} ${dataGet.lastName}"
        tv_degi_name_committe.text = dataGet.designation
        Glide.with(this).load(dataGet.image).into(iv_avtar_committe)

    }

    fun back(view: View) {
        finish()
    }
}