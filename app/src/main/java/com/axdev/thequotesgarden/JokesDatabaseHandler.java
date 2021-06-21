package com.axdev.thequotesgarden;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class JokesDatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "jokes.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    // Quotes table name
    private static final String TABLE_QUOTES = "jokestable";

    // Quotes Table Columns names
    private static final String KEY_ID = "id";

    static Context myContext;


    public JokesDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(myContext.getApplicationInfo().dataDir
                + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public static String getDatabasePath() {
        return myContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public void openDataBase() throws SQLException {
        File dbFile = myContext.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        try {
            CopyDataBaseFromAsset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.w(DataBaseHandler.class.getName(), "Data base is upgraded  ");

    }

    // Getting single Joke
    public Joke getJoke(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db
                .rawQuery(
                        "SELECT col_1, col_2, col_3, col_4, col_5, col_6, col_7 FROM joketsable WHERE col_1 = " + id, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                Joke quote = new Joke();
                quote.set_id(Integer.parseInt(cursor.getString(0)));
                quote.set_name(cursor.getString(1));
                quote.set_category(cursor.getString(2));
                quote.set_fav(cursor.getString(3));
                quote.set_fileName(cursor.getString(4));
                quote.set_quote(cursor.getString(6));
                return quote;
            }
        } finally {
            cursor.close();
            db.close();
        }
        // return Quote
        return null;

    }

    // Getting All Jokers
    public List<Quote> getAllJokers(CharSequence value) {
        List<Quote> quoteList = new ArrayList<Quote>();
        // Select All Query
        String selectQuery = "SELECT name,file_name, COUNT(author_name ) AS count FROM author LEFT JOIN quote ON name = author_name WHERE name LIKE '%"
                + value + "%'  GROUP BY name ORDER BY  name ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Quote quote = new Quote();

                    quote.setName(cursor.getString(0));

                    quote.setFileName(cursor.getString(1));
                    quote.setCount(cursor.getString(2));
                    quoteList.add(quote);

                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        // return quote list
        return quoteList;

    }

    // Getting All Categories
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<Category>();
        // Select All Query
        String selectQuery = "SELECT name, file_name, COUNT(author_name ) AS count FROM category LEFT JOIN quote ON name = category_name  GROUP BY name ORDER BY  name ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setName(cursor.getString(0));
                    category.setFileName(cursor.getString(1));
                    category.setCount(cursor.getString(2));
                    categoryList.add(category);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return categoryList;

    }

    // Getting All Jokes
    public ArrayList<Joke> getAllJokes(String limit) {

        openDataBase();
        ArrayList<Joke> jokeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT *  FROM jokestable  ORDER BY jokestable.col_1"+limit;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Joke joke = new Joke();

                    joke.set_id(cursor.getInt(cursor.getColumnIndex("col_1")));
                    joke.set_name(cursor.getString(cursor.getColumnIndex("col_2")));
                    joke.set_fileName(cursor.getString(cursor.getColumnIndex("col_5")));
                    joke.set_quote(cursor.getString(cursor.getColumnIndex("col_7")));

                    jokeList.add(joke);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        // return quote list
        return jokeList;

    }



    // Getting Favorites
    public List<Quote> getFavorites() {
        List<Quote> quoteList = new ArrayList<Quote>();
        // Select All Query
        String selectQuery = "SELECT quote._id, quote.author_name, quote.qte, quote.category_name,fav, author.file_name  FROM quote,author where author.name = quote.author_name AND fav ='1'  ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Quote quote = new Quote();
                    quote.setID(Integer.parseInt(cursor.getString(0)));
                    quote.setName(cursor.getString(1));
                    quote.setQuote(cursor.getString(2));
                    // quote.setImage(cursor.getBlob(4));
                    quote.setCategory(cursor.getString(3));
                    quote.setFav(cursor.getString(4));
                    quote.setFileName(cursor.getString(5));
                    quoteList.add(quote);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        // close inserting data from database

        return quoteList;

    }

    // Getting Quotes By Category
    public List<Quote> getQuotesByCategory(String value) {
        List<Quote> quoteList = new ArrayList<Quote>();
        // Select All Query
        String selectQuery = "SELECT quote._id, quote.author_name, quote.qte, quote.category_name,fav, author.file_name  FROM quote,author where author.name = quote.author_name AND category_name = '"
                + value + "' ORDER BY quote.qte ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Quote quote = new Quote();
                    quote.setID(Integer.parseInt(cursor.getString(0)));
                    quote.setName(cursor.getString(1));
                    quote.setQuote(cursor.getString(2));
                    // quote.setImage(cursor.getBlob(4));
                    quote.setCategory(cursor.getString(3));
                    quote.setFav(cursor.getString(4));
                    quote.setFileName(cursor.getString(5));
                    quoteList.add(quote);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        // close inserting data from database

        return quoteList;

    }

    // Getting Quotes By Author
    public List<Quote> getQuotesByAuthor(String value) {
        List<Quote> quoteList = new ArrayList<Quote>();
        // Select All Query
        String selectQuery = "SELECT quote._id, quote.author_name, quote.qte, quote.category_name,fav, author.file_name  FROM quote,author where author.name = quote.author_name AND name= '"
                + value + "' ORDER BY author_name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    Quote quote = new Quote();
                    quote.setID(Integer.parseInt(cursor.getString(0)));
                    quote.setName(cursor.getString(1));
                    quote.setQuote(cursor.getString(2));
                    // quote.setImage(cursor.getBlob(4));
                    quote.setCategory(cursor.getString(3));
                    quote.setFav(cursor.getString(4));
                    quote.setFileName(cursor.getString(5));
                    quoteList.add(quote);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        // close inserting data from database

        return quoteList;

    }

    // Updating single quote

    public int updateJoke(Joke quote) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("col_1", quote.get_id());
        values.put("col_2", quote.get_name());
        values.put("col_7", quote.get_quote());
        values.put("col_3", quote.get_category());
        values.put("col_4", quote.get_fav());
        // updating row
        return db.update(TABLE_QUOTES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(quote.get_id()) });

    }


}
