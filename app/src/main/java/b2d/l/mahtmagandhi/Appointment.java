package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Appointment extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView empty_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        recyclerView = findViewById(R.id.rv_appointment);
        RecyclerView.LayoutManager la = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(la);
        ArrayList<Appointment_data> data = new ArrayList<>();
        data.add(new Appointment_data("25 December 2020", "Booked", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));
        data.add(new Appointment_data("26 December 2020", "Pending", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));
        data.add(new Appointment_data("27 December 2020", "Expired", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));
        RecyclerView.Adapter appointment_adapter = new AppointmentAdapter(this, data);
        recyclerView.setAdapter(appointment_adapter);

        empty_message = findViewById(R.id.textView7);
        empty_message.setVisibility(View.INVISIBLE);
    }

    public void back(View view) {
        finish();
    }

    public void new_appointment(View view) {
    }
}