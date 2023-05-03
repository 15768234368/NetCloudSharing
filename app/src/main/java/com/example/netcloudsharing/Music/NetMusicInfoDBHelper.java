package com.example.netcloudsharing.Music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NetMusicInfoDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "netmusic.db";
    private static final int DB_VERSION = 1;
    /**
     * table
     */
    public static final String TABLE_NAME_NET = "netMusicInfo";
    public static final String TABLE_NAME_HOTLIST = "netMusicHotList";
    public static final String TABLE_NAME_DAYRECOMMEND = "dayRecommend";
    public static final String TABLE_NAME_MUSICSET = "musicSet";
    public static final String TABLE_NAME_ARTISTLIST = "artistList";
    public static final String TABLE_NAME_COMMENT = "comment";
    /**
     * column
     */
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
    public static final String BANGID = "bangId";

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

    public static final String CID = "cid";
    public static final String LIKE_NUM = "like_num";
    public static final String MSG = "msg";
    public static final String TIME = "time";
    public static final String U_ID = "u_id";
    public static final String U_NAME = "u_name";
    public static final String U_PIC = "u_pic";
    public static final String COMMENT_TYPE = "comment_type";
    private static final String DB_CREATE_TABLE_NET = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_NET + " ("
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
            + PIC120 + " text" + ")";
    private static final String DB_CREATE_TABLE_HOTLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_HOTLIST + " ("
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
            + PIC120 + " text" + ")";
    private static final String DB_CREATE_TABLE_DAYRECOMMEND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DAYRECOMMEND + " ("
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
            + PIC120 + " text" + ")";

    private static final String DB_CREATE_TABLE_MUSICSET = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MUSICSET + " ("
            + MUSIC_RID + " text not null primary key,"
            + RID + " integer not null,"
            + BANGID + " integer not null,"
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
            + PIC120 + " text" + ")";

    private static final String DB_CREATE_TABLE_ARTISTLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ARTISTLIST + " ("
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
    private static final String DB_CREATE_TABLE_COMMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_COMMENT + " ("
            + CID + " integer not null primary key,"
            + LIKE_NUM + " integer,"
            + MSG + " text,"
            + TIME + " text,"
            + U_ID + " integer,"
            + U_NAME + " text,"
            + U_PIC + " text,"
            + COMMENT_TYPE + " text" + ")";

    public NetMusicInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_NET);
        db.execSQL(DB_CREATE_TABLE_HOTLIST);
        db.execSQL(DB_CREATE_TABLE_DAYRECOMMEND);
        db.execSQL(DB_CREATE_TABLE_MUSICSET);
        db.execSQL(DB_CREATE_TABLE_ARTISTLIST);
        db.execSQL(DB_CREATE_TABLE_COMMENT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing for now
    }
}

