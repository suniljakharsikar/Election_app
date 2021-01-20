package b2d.l.mahtmagandhi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StringAdapter extends ArrayAdapter<String> {

    private final Activity _context;
    private final ArrayList<String> rows;

    public class ViewHolder
    {

        TextView StringText;
    }

    public StringAdapter(Activity context, ArrayList<String> rows)
    {
        super(context,R.layout.simple_dropdown_string, R.id.textView_simple_dropdown ,rows);
        this._context = context;
        this.rows = rows;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = _context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.simple_dropdown_string,parent,false);

            holder = new ViewHolder();
            holder.StringText = (TextView) convertView.findViewById(R.id.textView_simple_dropdown);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.StringText.setText(rows.get(position));

        return convertView;
    }
}