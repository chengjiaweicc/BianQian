package com.example.bianqiandemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceId;

    public MessageAdapter(@NonNull Context context, int textResourceId, @NonNull List<Message> objects) {
        super(context, textResourceId, objects);
        resourceId=textResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message meSsage=getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else{
            view = convertView;
        }

        TextView mtext=(TextView)view.findViewById(R.id.tv_text);
        TextView mtime=(TextView)view.findViewById(R.id.tv_time);
        mtext.setText(meSsage.getText());
        mtime.setText(meSsage.getTime());
        return view;
    }
}
