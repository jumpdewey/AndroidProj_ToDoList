package com.weidu.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public final static String TAG = "List";
    private List<ToDoList> lists = new ArrayList<>();
    public ArrayAdapter<ToDoList> adapter = null;
    private DBHelper dbHelper = null;
//    private final static String KEY_POSITION = "key_position";
//    private int mPosition;
//    private final static String INITIAL_POSITION = "pref_position";
//    public static final int Request_code = 1;
    private SQLiteDatabase db;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
//        SharedPreferences mySharedPreferences =
//                PreferenceManager.getDefaultSharedPreferences(getActivity());
//        if (savedInstanceState != null){
//            mPosition = savedInstanceState.getInt(KEY_POSITION, 0);
//            Log.d(TAG,"onActivityCreated savedInstanceState mPosition " + mPosition);
//        } else {
//            mPosition = mySharedPreferences.getInt(INITIAL_POSITION, 0);
//            Log.d(TAG,"onActivityCreated mySharedPreferences mPosition " + mPosition);
//        }
        try{
//            db= SQLiteDatabase.openOrCreateDatabase("/data/data/com.weidu.todolist/databases/list.db",null);
            dbHelper = new DBHelper(getActivity());
            lists = dbHelper.selectAll();
        } catch(Exception e){
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }
        ListView listView = (ListView)getActivity().findViewById(R.id.listView);
        adapter = new ToDoListAdapter(getActivity(),R.layout.list,lists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAdditionalInfoAlert(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                onDelete(view,i);
                return false;
            }
        });
        Button button = (Button)getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
    }

    public void onSave(){
        ToDoList list = new ToDoList();
        EditText title = (EditText)getActivity().findViewById(R.id.editTitle);
        EditText date = (EditText)getActivity().findViewById(R.id.editDate);
        EditText description = (EditText)getActivity().findViewById(R.id.editDesc);
        EditText AddiInfo = (EditText)getActivity().findViewById(R.id.editAddi);
        String titleStr = title.getText().toString();
        String dateStr = date.getText().toString();
        String descStr = description.getText().toString();
        String addiStr = AddiInfo.getText().toString();
        if(dateStr.length()!=10||dateStr.charAt(2)!='/'||dateStr.charAt(5)!='/'){
            Toast.makeText(getActivity(),"I wanna know the correct due day...",Toast.LENGTH_SHORT).show();
        } else if((!TextUtils.isEmpty(titleStr))&&(!TextUtils.isEmpty(dateStr))){
            list.setTitle(titleStr);
            list.setDueDate(dateStr);
            list.setDescription(descStr);
            list.setAddiInfo(addiStr);
            adapter.add(list);
            adapter.notifyDataSetChanged();
            long Id = 0;
            if(dbHelper!=null){
                Id = dbHelper.insert(list);
                list.setId(Id);
            }
            title.setText("");
            date.setText("");
            description.setText("");
            AddiInfo.setText("");
        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.alert_message),Toast.LENGTH_SHORT).show();
        }
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showAdditionalInfoAlert(int position){
        ToDoList list = adapter.getItem(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
        if(list.getAddiInfo()!=null) {
            alertDialogBuilder.setMessage(list.getAddiInfo());
        } else {
            alertDialogBuilder.setMessage("Oops! No additional information..");
        }
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Oho!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onDelete(View view, int position){
        ToDoList list = adapter.getItem(position);
        if(list!=null){
            String str = "Deleting: "+list.getTitle();
            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
            adapter.remove(list);
            adapter.notifyDataSetChanged();
        }
        if(dbHelper!=null) dbHelper.deleteRecord(list.getId());
    }

//    @Override
//    public void onDestroy()
//    {
//        savePrefs(INITIAL_POSITION, mPosition);
//        super.onDestroy();
//    }
//    private void savePrefs(String key, int value) {
//        SharedPreferences sp =
//                PreferenceManager.getDefaultSharedPreferences(getActivity());
//        SharedPreferences.Editor ed = sp.edit();
//        ed.putInt(key, value);
//        ed. apply ();
//    }

//    @Override
//    public void onActivityResult(int reqCode, int resCode, Intent data) {
//        super.onActivityResult(reqCode, resCode, data);
//        Log.d(TAG, " onActivityResult " + " data: " + data);
//
//    }

}
