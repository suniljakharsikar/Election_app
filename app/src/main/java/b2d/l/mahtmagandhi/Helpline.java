package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Helpline extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<AddressData> addressData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv6);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressData = new ArrayList<>();
        addressData.add(new AddressData("Address 1", "Near Village Chok, Toer Road, Jaipur\n" + "Contact : 1234567890"));
        addressData.add(new AddressData("Address 2", "Near Village Chok, Toer Road, Jaipur\n" + "Contact : 1234567890"));
        addressData.add(new AddressData("Address 3", "Near Village Chok, Toer Road, Jaipur\n" + "Contact : 1234567890"));
        HelplineAdapter helplineAdapter = new HelplineAdapter(this, addressData);
        recyclerView.setAdapter(helplineAdapter);
    }
    public void back(View view) {
        finish();
    }
}