package com.example.mohan.notes.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.mohan.notes.model.Note;

import java.util.ArrayList;

/**
 * Created by Mohan on 03-15-2018.
 */

public class mDBHandler extends SQLiteOpenHelper{
    private static final String TAG = "mDBHandler";


    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "notesDB.db";
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTENAME = "title";
    public static final String COLUMN_NOTEDES="description";
    public static final String COLUMN_CATEGORY="category";


    public mDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLE_NOTES+" ("+
                COLUMN_ID+" INTEGER PRIMARY KEY  AUTOINCREMENT, "+
                COLUMN_NOTENAME+" STRING, "+
                COLUMN_NOTEDES+" STRING, "+
                COLUMN_CATEGORY+" STRING"+
                ")";
        db.execSQL(query);
    }

    //Add a new row to the database
    public void addNote(Note note, Context c){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTENAME, note.get_title());
        values.put(COLUMN_NOTEDES,note.get_description());
        values.put(COLUMN_CATEGORY,note.get_category());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();
 //      Toast.makeText(c,"note added form handler",Toast.LENGTH_SHORT).show();
    }
    public void updateNote(int id,String title,String des,String category){
        ContentValues values=new ContentValues();
        values.put(COLUMN_NOTENAME,title);
        values.put(COLUMN_NOTEDES,des);
        values.put(COLUMN_CATEGORY,category);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NOTES,values,"_id="+id,null);
    }

    //Delete a note from the database
    public void deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE " + COLUMN_ID + "=\"" + Integer.toString(id) + "\";");
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE 1";// why not leave out the WHERE  clause?

        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex(COLUMN_NOTENAME)) != null) {
                dbString += recordSet.getString(recordSet.getColumnIndex("_id"));
                dbString +="  ";
                dbString += recordSet.getString(recordSet.getColumnIndex("title"));
                dbString += "\n";
            }
            recordSet.moveToNext();
        }
        db.close();
        return dbString;
    }

    public ArrayList<Note> getData(ArrayList<Note> notes,Context context,String category) {
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE 1";
        SQLiteDatabase db = getWritableDatabase();

        switch (category) {
            case   "TODO":
                query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_CATEGORY + " LIKE '%TODO%'";
                break;
            case "WORK":
                query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_CATEGORY + " LIKE '%WORK%'";
                break;
            case  "PERSONAL":
                query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_CATEGORY + " LIKE '%PERSONAL%'";
                break;
            case  "OTHER":
                query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_CATEGORY + " LIKE '%OTHER%'";
                break;

        }
        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(query, null);


        try {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                   note.set_id(cursor.getInt(cursor.getColumnIndex( "_id")));
                    note.set_description(cursor.getString(cursor.getColumnIndex("description")));
                    note.set_title(cursor.getString(cursor.getColumnIndex( "title")));
                    note.set_category(cursor.getString(cursor.getColumnIndex("category")));
                    notes.add(note);
                } while (cursor.moveToNext());
            }


            Log.e(TAG, "getData: notes size" + notes.size());
            // return notes list
            return notes;

        }catch (NullPointerException e){
          Toast.makeText(context, "note is null ", Toast.LENGTH_SHORT).show();

            return notes;
        }finally{
            cursor.close();
            db.close();
    }


    }


    public ArrayList<Note> getSearchResult(String search,ArrayList<Note> notes){


        SQLiteDatabase db = getWritableDatabase();


        String searchQuery="SELECT * FROM "+TABLE_NOTES+" WHERE "+COLUMN_NOTENAME+" LIKE '"+search+"%' OR "+COLUMN_NOTEDES+" LIKE '"+search+"%'";
        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(searchQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.set_description(cursor.getString(cursor.getColumnIndex("description")));
                    note.set_title(cursor.getString(cursor.getColumnIndex( "title")));
                    note.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                    note.set_category(cursor.getString(cursor.getColumnIndex("category")));
                    notes.add(note);
                   // Log.e("results", "getSearchResult: "+note.get_title(),null );
                } while (cursor.moveToNext());
            }



            // return notes list
            return notes;

        }catch (NullPointerException e){
            Log.e("error", "getSearchResult: ",null );

            return notes;
        }finally{
            cursor.close();
            db.close();
        }




    }

    public String resultsToString(String search,Context context){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query ="SELECT * FROM "+TABLE_NOTES+" WHERE "+COLUMN_NOTENAME+" LIKE '"+search+"%' OR "+COLUMN_NOTEDES+" LIKE '"+search+"%'";

        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(query, null);



        //Position after the last row means the end of the results
        try {
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex(COLUMN_NOTENAME)) != null) {
                        Toast.makeText(context,"cursor has data",Toast.LENGTH_SHORT).show();
                        dbString += cursor.getString(cursor.getColumnIndex("_id"));
                        dbString += "  ";
                        dbString += cursor.getString(cursor.getColumnIndex("title"));
                        dbString += "\n";
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("errror", "resultsToString: error",null );
        }finally {
            cursor.close();
            db.close();

        }
        return dbString;
    }
}
