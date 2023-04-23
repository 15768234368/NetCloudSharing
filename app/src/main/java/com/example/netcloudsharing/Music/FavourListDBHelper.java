package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavourListDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "favourMusic.db";
    public static final String TABLE_NAME_FAVOURLIST = "favourList";
    private static final int DB_VERSION = 1;
    public static final String FID = "fid";
    public static final String SONGNAME = "songName";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String DURATION = "duration";
    public static final String RID = "rid";
    public static final String PIC = "pic";
    public static final String PATH = "path";
    public static final String LID = "lid";
    public static final String ISNETMUSIC = "isNetMusic";

    private static final String DB_CREATE_TABLE_FAVOUR = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVOURLIST + " ("
            + FID + " text not null primary key,"
            + SONGNAME + " text,"
            + ARTIST + " text,"
            + ALBUM + " text,"
            + DURATION + " text,"
            + RID + " integer,"
            + PIC + " text,"
            + PATH + " text,"
            + LID + " integer,"
            + ISNETMUSIC + " text" + " )";

    public FavourListDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_FAVOUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
