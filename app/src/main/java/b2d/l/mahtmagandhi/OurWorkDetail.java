package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class OurWorkDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_work_detail);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        OurWorkResponseModel.Data data = getIntent().getParcelableExtra("data");
        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(this);

        List<OurWorkResponseModel.Data.DataImage> imgs = data.getDataImages();
        ArrayList<SliderItem> list = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            OurWorkResponseModel.Data.DataImage img = imgs.get(i);
            if (img.getImageUrl().length() > 0) {
                list.add(new SliderItem(Url.burl + img.getImageUrl()));
                Log.d("OurWorkDetail", "onCreate: imageUrl" + img.getImageUrl());
            } else {
                Log.d("OurWorkDetail", "onCreate: imageName" + img.getImageName());

                list.add(new SliderItem(img.getImageName()));

            }
        }
        adapter.renewItems(list);
        TextView titleTv = findViewById(R.id.textView_our_work_title);
        TextView descTv = findViewById(R.id.tv_our_work_desc);

        titleTv.setText(data.getTitle());
        descTv.setText(data.getDescription());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    public void back(View view) {
        finish();
    }
}