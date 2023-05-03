package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavourListDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "favourMusic.db";
    public static final String TABLE_NAME_FAVOURLIST = "favourList";
    public static final String TABLE_NAME_FAVOURARTIST = "favourArtist";
    public static final String TABLE_NAME_FAVOURMUSICSET = "favourMusicSet";
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

    public static final String ARTISTID = "artistId";
    public static final String AARTIST = "aartist";
    public static final String ALBUMNUM = "albumNum";
    public static final String ARTISTFANS = "artistFans";
    public static final String BIRTHPLACE = "birthplace";
    public static final String COUNTRY = "country";
    public static final String GENER = "gener";
    public static final String INFO = "info";
    public static final String LANGUAGE = "language";
    public static final String MUSICNUM = "musicNum";
    public static final String MVNUM = "mvNum";

    public static final String MUSICSETPHOTO = "musicSetPhoto";
    public static final String MUSICSETNAME = "musicSetName";
    public static final String BANGID = "bangId";

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
    private static final String DB_CREATE_TABLE_ARTIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVOURARTIST + " ("
            + ARTISTID + " integer not null primary key,"
            + ARTIST + " text,"
            + AARTIST + " text,"
            + ALBUMNUM + " integer,"
            + ARTISTFANS + " integer,"
            + BIRTHPLACE + " text,"
            + COUNTRY + " text,"
            + GENER + " text,"
            + INFO + " text,"
            + LANGUAGE + " text,"
            + MUSICNUM + " integer,"
            + MVNUM + " integer,"
            + PIC + " text" + ")";
    private static final String DB_CREATE_TABLE_MUSICSET = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVOURMUSICSET + " ("
            + BANGID + " integer not null primary key,"
            + MUSICSETNAME + " text,"
            + MUSICSETPHOTO + " text" + ")";
    public FavourListDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_FAVOUR);
        db.execSQL(DB_CREATE_TABLE_ARTIST);
        db.execSQL(DB_CREATE_TABLE_MUSICSET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
