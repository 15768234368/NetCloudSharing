package com.example.netcloudsharing.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayListHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "playList.db";
    private static final int DATABASE_VERSION = 1;
    public PlayListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static final String TABLE_NAME = "music";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SONG = "song";
    public static final String COLUMN_SINGER = "singer";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_RID = "rid";
    public static final String COLUMN_AUDIO_ID = "audio_id";
    public static final String COLUMN_PIC = "pic";
    public static final String COLUMN_REAL_URL = "real_url";
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建音乐表
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SONG + " TEXT," +
                COLUMN_SINGER + " TEXT," +
                COLUMN_ALBUM + " TEXT," +
                COLUMN_DURATION + " TEXT," +
                COLUMN_RID + " TEXT UNIQUE," +
                COLUMN_AUDIO_ID + " TEXT," +
                COLUMN_PIC + " TEXT," +
                COLUMN_REAL_URL + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
