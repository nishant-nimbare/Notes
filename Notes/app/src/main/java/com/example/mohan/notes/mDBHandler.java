package com.example.mohan.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mohan on 03-15-2018.
 */

public class mDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "notesDB.db";
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTENAME = "title";
    public static final String COLUMN_NOTEDES="description";


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
                COLUMN_NOTEDES+" STRING"+
                ")";
        db.execSQL(query);
    }

    //Add a new row to the database
    public void addNote(Note note,Context c){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTENAME, note.get_title());
        values.put(COLUMN_NOTEDES,note.get_description());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();
 //      Toast.makeText(c,"note added form handler",Toast.LENGTH_SHORT).show();
    }
    public void updateNote(int id,String title,String des){
        ContentValues values=new ContentValues();
        values.put(COLUMN_NOTENAME,title);
        values.put(COLUMN_NOTEDES,des);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NOTES,values,"_id="+id,null);
    }

    //Delete a note from the database
    public void deleteNote(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE " + COLUMN_NOTENAME + "=\"" + title + "\";");
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

    public ArrayList<Note> getData(ArrayList<Note> notes,Context context) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE 1";

        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(query, null);


        try {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                   note.set_id(cursor.getInt(cursor.getColumnIndex( "_id")));
                    note.set_description(cursor.getString(cursor.getColumnIndex("description")));
                    note.set_title(cursor.getString(cursor.getColumnIndex( "title")));

                    notes.add(note);
                } while (cursor.moveToNext());
            }



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

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " +TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public ArrayList<Note> getSearchResult(String search,ArrayList<Note> notes,Context context){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+search+" FROM " + TABLE_NOTES + " WHERE 1";

        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(query, null);


        try {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.set_id(cursor.getInt(cursor.getColumnIndex( "_id")));
                    note.set_description(cursor.getString(cursor.getColumnIndex("description")));
                    note.set_title(cursor.getString(cursor.getColumnIndex( "title")));

                    notes.add(note);
                } while (cursor.moveToNext());
            }



            // return notes list
            return notes;

        }catch (Exception e){
            Toast.makeText(context, "search error ", Toast.LENGTH_SHORT).show();

            return notes;
        }finally{
            cursor.close();
            db.close();
        }
    }
}
