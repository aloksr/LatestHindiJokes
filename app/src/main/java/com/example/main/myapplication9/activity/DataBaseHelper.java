package com.example.main.myapplication9.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    String DB_PATH = null;
    public static  String DB_NAME = "";
    public static final String TABLE_FAVOURITE = "Favourite";
    public static final String FAVOURITE_COLUMN_ID = "id";
    public static final String FAVOURITE_COLUMN_FAV = "fav";
    public static final String TABLE_JOKES = "jokes";

    SQLiteDatabase db;
    private final Context mycontext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 10);
        this.mycontext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path1", DB_PATH);
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error Copying Database");

            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase db = null;

        try {
            String mydbpath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(mydbpath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            e.printStackTrace();

        }
        if (db != null) {
            db.close();

        }
        return db != null ? true : false;
    }

    private void copyDatabase() throws IOException {

        InputStream is = mycontext.getAssets().open(DB_NAME);
        String out = DB_PATH + DB_NAME;
        OutputStream os = new FileOutputStream(out);
        byte[] buff = new byte[10];
        int length;
        while ((length = is.read(buff)) > 0) {
            os.write(buff, 0, length);
        }
        os.flush();
        os.close();
        is.close();
    }

    public void openDataBase() throws SQLiteException {

        String path = DB_PATH + DB_NAME;
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }

    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return
                db.rawQueryWithFactory(null, sql, selectionArgs, null, null);

    }

    public void insertFavourite(String fav) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fav", fav);
        db.insert("Favourite", null, values);
        db.close();
    }

    public void delete(String id) {
        String[] args = {id};
        getWritableDatabase().delete(TABLE_FAVOURITE, "fav=?", args);

    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Favourite where id=" + id + "", null);
        return res;
    }

    public Cursor getFavourite(String fav) {
        db = DataBaseHelper.this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_FAVOURITE + " where fav = ? ", new String[]{fav});
        return res;
    }
    public void deleteAllFavourites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVOURITE);
        db.close();
    }

    public ArrayList<String> getAllFavourite() {
        ArrayList<String> list_fav = new ArrayList<String>();
        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + TABLE_FAVOURITE;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                String favourite = cursor.getString(1);
                list_fav.add(favourite);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }
        return list_fav;
    }

    public ArrayList<String> getAllJokes() {
        ArrayList<String> list_jokes = new ArrayList<String>();
        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + TABLE_JOKES;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                String favourite = cursor.getString(0);
                list_jokes.add(favourite);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }
        return list_jokes;
    }



    }
