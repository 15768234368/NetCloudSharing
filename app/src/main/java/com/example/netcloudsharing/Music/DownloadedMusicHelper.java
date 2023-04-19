package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadedMusicHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "music_info";
    public static final String SONG_ID = "song_id";
    public static final String SONG_TITLE = "song_title";
    public static final String SONG_SINGER = "song_singer";
    public static final String SONG_ALBUM = "song_album";
    public static final String SONG_DURATION = "song_duration";
    public static final String SONG_PATH = "song_path";
    public DownloadedMusicHelper(Context context){
        super(context, "music_downloaded.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + SONG_ID + " text not null primary key,"
                + SONG_TITLE + " text,"
                + SONG_SINGER +" text,"
                + SONG_ALBUM + " text,"
                + SONG_DURATION + " real,"
                + SONG_PATH + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
