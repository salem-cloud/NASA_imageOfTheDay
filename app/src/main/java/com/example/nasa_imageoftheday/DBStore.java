package com.example.nasa_imageoftheday;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DBStore extends SQLiteOpenHelper {

    protected final static String DB_NAME = "FavImageDB";
    protected final static int VNUM = 2;
    public final static String TB_NAME = "FAVS";
    public final static String ID = "_id";
    public final static String TITLE = "TITLE";
    public final static String DATE = "DATE";
    public final static String FILENAME = "FILENAME";
    public final static String EXPLAIN = "EXPLANATION";
    public final static String HOUR = "HDURL";



    public DBStore(Context activity) {
        super(activity, DB_NAME, null, VNUM);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE " + TB_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT,"
                + DATE + " TEXT,"
                + FILENAME + " TEXT,"
                + EXPLAIN + " TEXT,"
                + HOUR + " TEXT);"
        );
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldV, int newV) {
        database.execSQL( "DROP TABLE IF EXISTS " + TB_NAME);

        onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldV, int newV) {

        database.execSQL( "DROP TABLE IF EXISTS " + TB_NAME);

        onCreate(database);
    }

}
