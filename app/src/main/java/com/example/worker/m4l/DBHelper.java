package com.example.worker.m4l;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public static final String TABLE_NAME_HISTORY = "history";
    public static final String HISTORY_COLUMN_ID = "id";
    public static final String HISTORY_COLUMN_MOVE = "move";
    public static final String HISTORY_COLUMN_MANUAL = "manual";
    public static final String HISTORY_COLUMN_ACTIVE = "active";
    public static final String HISTORY_COLUMN_DATE = "date";


//    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME_HISTORY
                        + " (" + HISTORY_COLUMN_ID +" integer primary key, "
                        + HISTORY_COLUMN_MOVE + " integer,"
                        + HISTORY_COLUMN_MANUAL + " integer,"
                        + HISTORY_COLUMN_ACTIVE + " integer, "
                        + HISTORY_COLUMN_DATE +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY);
        onCreate(db);
    }

    public boolean historyInsert (ModelHistory arg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COLUMN_MOVE, arg.getMove());
        contentValues.put(HISTORY_COLUMN_MANUAL, arg.getManual());
        contentValues.put(HISTORY_COLUMN_ACTIVE, arg.getActive());
        contentValues.put(HISTORY_COLUMN_DATE, arg.getDate());
        db.insert(TABLE_NAME_HISTORY, null, contentValues);
        return true;
    }

    public ModelHistory historyGetByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME_HISTORY + " where id="+id+"", null );

        ModelHistory history = new ModelHistory();
        if (res != null ) {
            if  (res.moveToFirst()) {
                do {
                    int move = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MOVE));
                    int manual = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MANUAL));
                    int active = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ACTIVE));
                    int idd = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ID));
                    String date = res.getString(res.getColumnIndex(HISTORY_COLUMN_DATE));
                    history.setModelHistory(idd, move, manual, active, date);
                    return history;
                }while (res.moveToNext());
            }
        }
        res.close();
        return history;
    }


    public ModelHistory historyGetByDate(String arg) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME_HISTORY + " where date = '"+arg+"'", null );

        ModelHistory history = new ModelHistory();
        if (res != null ) {
            if  (res.moveToFirst()) {
                do {
                    int move = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MOVE));
                    int manual = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MANUAL));
                    int active = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ACTIVE));
                    int idd = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ID));
                    String date = res.getString(res.getColumnIndex(HISTORY_COLUMN_DATE));
                    history.setModelHistory(idd, move, manual, active, date);
                    return history;
                }while (res.moveToNext());
            }
        }
        res.close();
        return history;
    }

    public int historyNumberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_HISTORY);
        return numRows;
    }

    public boolean historyUpdate (ModelHistory history) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COLUMN_MOVE, history.getMove());
        contentValues.put(HISTORY_COLUMN_MANUAL, history.getManual());
        contentValues.put(HISTORY_COLUMN_ACTIVE, history.getActive());
        contentValues.put(HISTORY_COLUMN_DATE, history.getDate());

        db.update(TABLE_NAME_HISTORY, contentValues, "id="+history.getId(), null);
        return true;
    }

    public Integer historyDelete (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_HISTORY,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ModelHistory> historyGetAll() {
        ArrayList<ModelHistory> array_list = new ArrayList<ModelHistory>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME_HISTORY, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ModelHistory history = new ModelHistory();
            int move = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MOVE));
            int manual = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MANUAL));
            int active = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ACTIVE));
            int idd = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ID));
            String date = res.getString(res.getColumnIndex(HISTORY_COLUMN_DATE));
            history.setModelHistory(idd, move, manual, active, date);
            array_list.add(history);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<ModelHistory> historyGetAll(String strStart, String strEnd) {
        ArrayList<ModelHistory> array_list = new ArrayList<ModelHistory>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME_HISTORY + " where date >= '" + strStart + "' and date <= '" + strEnd + "'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ModelHistory history = new ModelHistory();
            int move = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MOVE));
            int manual = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MANUAL));
            int active = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ACTIVE));
            int idd = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ID));
            String date = res.getString(res.getColumnIndex(HISTORY_COLUMN_DATE));
            history.setModelHistory(idd, move, manual, active, date);
            array_list.add(history);
            res.moveToNext();
        }
        return array_list;
    }

    public int[] historyGetSum(String strStart, String strEnd) {
        int[] array_list = new int[3];
        for(int i = 0; i < 3; i++)
            array_list[i] = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME_HISTORY + " where date >= '" + strStart + "' and date <= '" + strEnd + "'", null );
        res.moveToFirst();

        int n = 0;
        while(res.isAfterLast() == false){
            int move = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MOVE));
            int manual = res.getInt(res.getColumnIndex(HISTORY_COLUMN_MANUAL));
            int active = res.getInt(res.getColumnIndex(HISTORY_COLUMN_ACTIVE));

            array_list[0] += move;
            array_list[1] += manual;
            array_list[2] += active;
            n++;
            res.moveToNext();
        }

        if(n > 0){
            array_list[0] /= n;
            array_list[1] /= n;
            array_list[2] /= n;
        }
        return array_list;
    }
}