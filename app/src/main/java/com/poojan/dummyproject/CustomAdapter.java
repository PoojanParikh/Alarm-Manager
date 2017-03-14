package com.poojan.dummyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VNurtureTechnologies on 14/03/17.
 */

public class CustomAdapter extends BaseAdapter {

    ArrayList<ReminderModel> reminderModelArrayList;
    Context context;
    LayoutInflater inflater;

    CustomAdapter(Context context,ArrayList<ReminderModel> reminderModelArrayList){
        this.context=context;
        this.reminderModelArrayList=reminderModelArrayList;
    }
    @Override
    public int getCount() {
        return reminderModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return reminderModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        TextView reminderTextView,reminderId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_list_view, viewGroup, false);

            holder.reminderTextView = (TextView) view.findViewById(R.id.reminder_text_view);
            holder.reminderId = (TextView) view.findViewById(R.id.reminder_id);

            view.setTag(holder);
        }

        else{
            holder = (ViewHolder) view.getTag();
        }

        holder.reminderId.setText(String.valueOf(reminderModelArrayList.get(i).getId()));
        holder.reminderTextView.setText(reminderModelArrayList.get(i).getReminder());

        return view;
    }
}