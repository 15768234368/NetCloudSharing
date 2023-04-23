package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalMusicDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "localMusic.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME_LOCALMUSICINFO = "localMusicInfo";
    public static final String LOCAL_LID = "local_lid";
    public static final String LID = "lid";
    public static final String SONGNAME = "songName";
    public static final String ARTIST = "ARTIST";
    public static final String ALBUM = "ALBUM";
    public static final String DURATION = "duration";
    public static final String PATH = "path";

    public LocalMusicDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    private static final String DB_CREATE_TABLE_LOCALLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LOCALMUSICINFO + " ("
            + LOCAL_LID + " text not null primary key,"
            + LID + " integer not null,"
            + SONGNAME + " text,"
            + ARTIST + " text,"
            + ALBUM + " text,"
            + DURATION + " text,"
            + PATH + " text " + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_LOCALLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
