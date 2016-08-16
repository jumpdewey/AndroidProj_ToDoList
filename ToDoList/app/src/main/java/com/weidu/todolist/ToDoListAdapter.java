package com.weidu.todolist;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adimv on 2016/8/13.
 */
public class ToDoListAdapter extends ArrayAdapter<ToDoList> {
    private int LayoutResourceId;
    public final static String TAG = "ToDoListAdapter";
    private LayoutInflater inflater;
    private List<ToDoList> lists;

//    Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/segoepr.ttf");

    public ToDoListAdapter(Context context,int LayoutResourceId, List<ToDoList> lists){
        super(context,LayoutResourceId,lists);
        this.LayoutResourceId = LayoutResourceId;
        this.lists = lists;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ListHolder holder;
        if(null==convertView){
            Log.d(TAG, "getView: rowView null: position " + position);
            convertView = inflater.inflate(LayoutResourceId, parent, false);
            holder = new ListHolder();
            holder.date = (TextView)convertView.findViewById(R.id.Date);

            holder.description = (TextView)convertView.findViewById(R.id.Description);

            holder.title = (TextView)convertView.findViewById(R.id.Title);

            convertView.setTag(holder);
        } else {
            Log.d(TAG, "getView: rowView !null - reuse holder: position " + position);
            holder = (ListHolder) convertView.getTag();
        }
        Log.d(TAG,"size of lists: "+lists.size());
        try{
            ToDoList toDo = lists.get(position);
            holder.title.setText(toDo.getTitle());
            holder.description.setText(toDo.getDescription());
            holder.date.setText(toDo.getDueDate());
//            holder.date.setTypeface(typeFace);
//            holder.description.setTypeface(typeFace);
//            holder.title.setTypeface(typeFace);
        } catch (Exception e){
            Log.e(TAG, " getView lists " + e + " position was : " + position + " lists.size: " + lists.size());
        }
        return convertView;
    }
    static class ListHolder{
        TextView title;
        TextView description;
        TextView date;
    }
}
