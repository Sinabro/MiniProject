package com.miniproject2.soma.miniproject02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 9. 5..
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList<Data> source;
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<Data> source) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;

    }

    public void setSource(ArrayList<Data> source) {

        this.source = source;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return (source != null) ? source.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return ((source != null) && (position >= 0 && position < source.size()) ? source.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Data data = (Data) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item, parent, false);

            viewHolder.textView_word = (TextView) convertView.findViewById(R.id.textView_word);
            viewHolder.textView_mean = (TextView) convertView.findViewById(R.id.textView_mean);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView_word.setText(data.word);
        viewHolder.textView_mean.setText(data.mean);

        return convertView;
    }
}