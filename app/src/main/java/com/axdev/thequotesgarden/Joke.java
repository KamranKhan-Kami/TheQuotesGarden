package com.axdev.thequotesgarden;

import java.io.Serializable;

/**
 * Created by CH_M_USMAN on 07-May-17.
 */
public class Joke implements Serializable {

    int _id;
    String _name;
    String _quote;
    String _category;
    String _fileName;
    String _fav;
    String _count;



    // Empty constructor
    public Joke() {

    }

    // Quote constructor
    public Joke(int id, String name, String quote, String category,
                 String fileName, String fav) {
        this._id = id;
        this._name = name;
        this._quote = quote;
        this._category = category;
        this._fileName = fileName;
        this._fav = fav;

    }

    // Author constructor
    public Joke(String name, String fileName, String count) {

        this._name = name;

        this._fileName = fileName;

        this._count = count;
    }


    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_count() {
        return _count;
    }

    public void set_count(String _count) {
        this._count = _count;
    }

    public String get_fav() {
        return _fav;
    }

    public void set_fav(String _fav) {
        this._fav = _fav;
    }

    public String get_fileName() {
        return _fileName;
    }

    public void set_fileName(String _fileName) {
        this._fileName = _fileName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_quote() {
        return _quote;
    }

    public void set_quote(String _quote) {
        this._quote = _quote;
    }
}
