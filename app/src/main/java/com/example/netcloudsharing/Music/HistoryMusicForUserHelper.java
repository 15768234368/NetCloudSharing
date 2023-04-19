package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryMusicForUserHelper extends SQLiteOpenHelper {
    private static final String TAG = "HistoryMusicForUserHelp";
    public static final String TABLE_NAME = "track_songs";
    public static final String TRACK_ID = "track_id";           //1
    public static final String SONG_TITLE = "song_title";       //2
    public static final String SONG_ID = "song_id";             //3
    public static final String SONG_SINGER = "song_singer";     //4
    public static final String SONG_ALBUM = "song_album";       //5
    public static final String USER = "user";                   //6
    public static final String USER_ID = "user_id";             //7
    public static final String LISTEN_TIME = "listen_time";     //8
    public static final String SONG_PATH = "song_path";         //9

    public HistoryMusicForUserHelper(Context context) {
        super(context, "track_metadata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + TRACK_ID + " text not null primary key,"
                + SONG_TITLE + " text,"
                + SONG_ID + " text,"
                + SONG_SINGER + " text,"
                + SONG_ALBUM + " text,"
                + USER + " text,"
                + USER_ID + " text,"
                + LISTEN_TIME + " text,"
                + SONG_PATH + " text" + ")");
        Log.d(TAG, "onCreate: ");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
