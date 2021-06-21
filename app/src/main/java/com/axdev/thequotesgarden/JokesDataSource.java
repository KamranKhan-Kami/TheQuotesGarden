package com.axdev.thequotesgarden;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class JokesDataSource {
    SQLiteDatabase sqLiteDatabase;
    SQLiteOpenHelper helper;

    public final  String[] allColumns = {
            "_id",
            "_name",
            "_quote",
            "_catagory",
            "_fileName",
            "_fav"


    };

    public JokesDataSource(Context context) {
        helper = new JokesDatabaseHandler(context);
        open();
    }

    public  Joke addJoke(Joke joke){
        ContentValues values = new ContentValues();
        values.put("_name",joke.get_name());
        values.put("_quote",joke.get_quote());
        values.put("_catagory",joke.get_category());
        values.put("_fileName",joke.get_fileName());
        values.put("_fav",joke.get_fav());
        Long insertId = sqLiteDatabase.insert("jokes",null,values);
        joke.set_id(Integer.parseInt(String.valueOf(insertId)));
        if (joke.get_id()!=-1){
            Log.e("Log:","Data added successfully..");
        }else {
            Log.e("Log:",joke.get_id()+"");
        }
        return joke;
    }

    public void open(){
        sqLiteDatabase = helper.getWritableDatabase();
        Log.e("Log::","Database opened.");
    }
    public void close(){
        helper.close();
        Log.e("Log:","Database closed.");
    }

    public ArrayList<Joke> findAll(){
        open();
        ArrayList<Joke> jokeList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("jokes",allColumns,null,null,null,null,null);
        Log.e("Log:","Returned "+ cursor.getCount() + " rows");

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Joke joke = new Joke();
                joke.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                joke.set_category(cursor.getString(cursor.getColumnIndex("_catagory")));
                joke.set_fav(cursor.getString(cursor.getColumnIndex("_fav")));
                joke.set_fileName(cursor.getString(cursor.getColumnIndex("_fileName")));
                joke.set_quote(cursor.getString(cursor.getColumnIndex("_quote")));
                jokeList.add(joke);
            }

        }
        return  jokeList;
    }


    public void addData(Context context){

                Joke j;

                j= new Joke();
                j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
                j.set_fileName("Victor Kiam");
                j.set_category("Normal");
                new JokesDataSource(context).addJoke(j);


                j= new Joke();
                j.set_quote("What is the difference between a snowman and a snowwoman");
                j.set_fileName("Warren Buffett");
                j.set_category("Normal");
                new JokesDataSource(context).addJoke(j);

                j= new Joke();
                j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
                j.set_fileName("William Shakespeare");
                j.set_category("Normal");
                new JokesDataSource(context).addJoke(j);


                j= new Joke();
                j.set_quote("What is the difference between a snowman and a snowwoman");
                j.set_fileName("William Morris");
                j.set_category("Normal");
                new JokesDataSource(context).addJoke(j);

        j= new Joke();
        j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
        j.set_fileName("William J. Clinton");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);


        j= new Joke();
        j.set_quote("What is the difference between a snowman and a snowwoman");
        j.set_fileName("William James");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);

        j= new Joke();
        j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
        j.set_fileName("William Morris");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);


        j= new Joke();
        j.set_quote("What is the difference between a snowman and a snowwoman");
        j.set_fileName("William Shakespeare");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);

        j= new Joke();
        j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
        j.set_fileName("Winston Churchill");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);


        j= new Joke();
        j.set_quote("What is the difference between a snowman and a snowwoman");
        j.set_fileName("Woody Allen");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);

        j= new Joke();
        j.set_quote("Can a kangaroo jump higher than a house? Of course, a house doesn’t jump at all");
        j.set_fileName("Yasmina Khadra");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);


        j= new Joke();
        j.set_quote("What is the difference between a snowman and a snowwoman");
        j.set_fileName("Zig Ziglar");
        j.set_category("Normal");
        new JokesDataSource(context).addJoke(j);
    }
}
