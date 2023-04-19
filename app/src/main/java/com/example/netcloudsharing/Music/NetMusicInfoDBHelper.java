package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

public class NetMusicInfoDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "netmusic.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "netMusicInfo";
    public static final String MUSIC_RID = "musicRid";
    public static final String RID = "rid";
    public static final String SONGNAME = "songName";
    public static final String ARTIST = "artist";
    public static final String ARTISTID = "artistId";
    public static final String ALBUM = "album";
    public static final String ALBUMID = "albumId";
    public static final String DURATION = "duration";
    public static final String SCORE100 = "score100";
    public static final String RELEASEDATE = "releaseDate";
    public static final String SONGTIMEMINUTES = "songTimeMinutes";
    public static final String ISLISTENFEE = "isListenFee";
    public static final String PIC = "pic";
    public static final String PIC120 = "pic120";
    public static final String URL = "url";

    private Handler mHandler = new Handler();

    private static final String DB_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + MUSIC_RID + " text not null primary key,"
            + RID + " integer not null,"
            + SONGNAME + " text,"
            + ARTIST + " text,"
            + ARTISTID + " integer,"
            + ALBUM + " text,"
            + ALBUMID + " integer,"
            + DURATION + " integer,"
            + SCORE100 + " integer,"
            + RELEASEDATE + " text,"
            + SONGTIMEMINUTES + " text,"
            + ISLISTENFEE + " text,"
            + PIC + " text,"
            + PIC120 + " text,"
            + URL + " text not null" + ")";


    private final Context mContext;

    public NetMusicInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE);


    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing for now
    }
}

