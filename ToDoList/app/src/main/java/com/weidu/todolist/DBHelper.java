package com.weidu.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adimv on 2016/8/13.
 */
public class DBHelper {

    private static final String LOGTAG = "DBHelper";
    private static final String DATABASE_NAME = "list.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "listdata";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";
    public static final String KEY_ADDI = "additional";
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_DESCRIPTION = 3;
    public static final int COLUMN_DATE = 2;
    public static final int COLUMN_ADDI = 4;
    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT = "INSERT INTO "+TABLE_NAME+"("+KEY_TITLE+","+KEY_DATE+","+KEY_DESCRIPTION+","+KEY_ADDI+")values(?,?,?,?)";
    public DBHelper(Context context) throws Exception {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
            db = openHelper.getWritableDatabase();
            insertStmt = db.compileStatement(INSERT);
        } catch(Exception e) {
            Log.e(LOGTAG," DBHelper constructor: could not get database " + e);
            throw(e);
        }
    }

    public long insert(ToDoList list){
        insertStmt.bindString(COLUMN_TITLE,list.getTitle());
        insertStmt.bindString(COLUMN_DATE,list.getDueDate());
        insertStmt.bindString(COLUMN_DESCRIPTION,list.getDescription());
        insertStmt.bindString(COLUMN_ADDI,list.getAddiInfo());
        long value = -1;
        try{
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d (LOGTAG, "value="+value);
        return value;
    }

    private static class OpenHelper extends SQLiteOpenHelper {
        private static final String LOGTAG = "OpenHelper";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_TITLE+" TEXT, "+KEY_DATE+" TEXT, "+KEY_DESCRIPTION+" TEXT, "+KEY_ADDI+" TEXT);";
        OpenHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.d(LOGTAG," onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch(Exception e){
                Log.e(LOGTAG," onCreate: Could not create SQL database: " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
            Log.w(LOGTAG,"Upgrading database, this will drop tables and recreate.");
            try{
                db.execSQL("DROP TABLES IF EXISTS "+TABLE_NAME);
                onCreate(db);
            } catch(Exception e) {
                Log.e(LOGTAG, " onUpgrade: Could not update SQL database: " + e);
            }
        }
    }

    public void deleteAll(){
        db.delete(TABLE_NAME,null,null);
    }
    public boolean deleteRecord(long rowId){
        return db.delete(TABLE_NAME,KEY_ID+"="+rowId,null)>0;
    }

    public List<ToDoList> selectAll(){
        List<ToDoList> Arraylist = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,new String[] {KEY_ID, KEY_TITLE, KEY_DATE, KEY_DESCRIPTION, KEY_ADDI},null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                ToDoList list = new ToDoList();
                list.setTitle(cursor.getString(COLUMN_TITLE));
                list.setDueDate(cursor.getString(COLUMN_DATE));
                list.setDescription(cursor.getString(COLUMN_DESCRIPTION));
                list.setAddiInfo(cursor.getString(COLUMN_ADDI));
                list.setId(cursor.getLong(COLUMN_ID));
                Arraylist.add(list);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return Arraylist;
    }

}
