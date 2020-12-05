package b2d.l.mahtmagandhi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyLangAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] strings;
    private int selectedIndex = 0;

    public MyLangAdapter(@NonNull Activity context, String[] strings) {
        super(context, R.layout.raw_language, strings);
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.raw_language, null, true);
        RadioButton radioButton = rowView.findViewById(R.id.radioButton);
        radioButton.setText(strings[position]);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notifyDataSetChanged();
                selectedIndex = position;
            }
        });
        if (selectedIndex == position) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
        return rowView;
    }

    public int getselected() {
        return selectedIndex;
    }
}
