package com.example.mohan.notes;

/**
 * Created by Mohan on 03-15-2018.
 */

public class Note {
    int _id;
    public String _title;
    public String _description;

    public Note(String _title,String _description) {
        this._title = _title;
        this._description = _description;
    }
    public Note(){
        this._title = "sample title";

        this._description = "sample description";
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_description(String _description) {

        this._description = _description;
    }



    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {

        return _title;
    }

    public String get_description() {

        return _description;
    }


}
