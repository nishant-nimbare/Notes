package com.example.mohan.notes.model;

/**
 * Created by Mohan on 03-15-2018.
 */

public class Note {
    int _id;
    public String _title;
    public String _description;
    public String _category;


    public String get_category() {

        return _category;
    }

    public int get_id() {
        return _id;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public Note(String _title, String _description, String _category) {
        this._title = _title;
        this._category = _category;
        this._description = _description;
    }

    public Note() {
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
