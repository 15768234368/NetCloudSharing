package com.example.netcloudsharing.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaveMusicFromNetToNMDB {
    private Context mContext;
    private static final String TAG = "SaveMusicFromNetToNMDB";

    public SaveMusicFromNetToNMDB(Context mContext) {
        this.mContext = mContext;
    }

    public void saveToDB(String key) {
        KuWoMusicTask task = new KuWoMusicTask();
        task.execute(key);
    }

    private class KuWoMusicTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Looper.prepare();
            OkHttpClient client = new OkHttpClient();
            String url = "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=" + Arrays.toString(params) + "&pn=1&rn=20&httpsStatus=1&reqId=c8a89281-dcdc-11ed-88de-4d8ba9a2a5d9";
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", "Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681707517; _ga=GA1.2.1592305065.1681707517; _gid=GA1.2.834743108.1681707517; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1681707525; kw_token=9SHBJFMMXMO")
                    .addHeader("csrf", "9SHBJFMMXMO")
                    .addHeader("Host", "www.kuwo.cn")
                    .addHeader("Referer", "http://www.kuwo.cn/search/list?key=%E5%BC%A0%E6%9D%B0")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray musicList = jsonObject.getJSONObject("data").getJSONArray("list");
                NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                for (int i = 0; i < musicList.length(); i++) {
                    String musicRid = null;
                    int rid = -1;
                    String songName = null;
                    String artist = null;
                    int artistId = -1;
                    String album = null;
                    int albumId = -1;
                    int duration = -1;
                    int score100 = 0;
                    String releaseDate = null;
                    String songTimeMinutes = null;
                    String isListenFee = null;
                    String pic = null;
                    String pic120 = null;
                    JSONObject song_info = musicList.getJSONObject(i);
                    musicRid = song_info.optString("musicrid");
                    values.put(NetMusicInfoDBHelper.MUSIC_RID, musicRid);
                    rid = song_info.optInt("rid", -1);
                    values.put(NetMusicInfoDBHelper.RID, rid);
                    songName = song_info.optString("name");
                    values.put(NetMusicInfoDBHelper.SONGNAME, songName);
                    artist = song_info.optString("artist");
                    values.put(NetMusicInfoDBHelper.ARTIST, artist);
                    artistId = song_info.optInt("artistid", -1);
                    values.put(NetMusicInfoDBHelper.ALBUMID, artistId);
                    album = song_info.optString("album");
                    values.put(NetMusicInfoDBHelper.ALBUM, album);
                    albumId = song_info.optInt("albumid", -1);
                    values.put(NetMusicInfoDBHelper.ALBUMID, albumId);
                    duration = song_info.optInt("duration", -1);
                    values.put(NetMusicInfoDBHelper.DURATION, duration);
                    score100 = song_info.optInt("score100", -1);
                    values.put(NetMusicInfoDBHelper.SCORE100, score100);
                    releaseDate = song_info.optString("releaseDate");
                    values.put(NetMusicInfoDBHelper.RELEASEDATE, releaseDate);
                    songTimeMinutes = song_info.optString("songTimeMinutes");
                    values.put(NetMusicInfoDBHelper.SONGTIMEMINUTES, songTimeMinutes);
                    isListenFee = song_info.optString("isListenFee");
                    values.put(NetMusicInfoDBHelper.ISLISTENFEE, isListenFee);
                    pic = song_info.optString("pic");
                    values.put(NetMusicInfoDBHelper.PIC, pic);
                    pic120 = song_info.optString("pic120");
                    values.put(NetMusicInfoDBHelper.PIC120, pic120);


                    String new_url = "http://www.kuwo.cn/api/v1/www/music/playUrl?mid=" + rid + "&type=convert_url";
                    request = new Request.Builder()
                            .url(new_url)
                            .addHeader("Cookie", "Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681707517; _ga=GA1.2.1592305065.1681707517; _gid=GA1.2.834743108.1681707517; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1681707525; kw_token=9SHBJFMMXMO")
                            .addHeader("csrf", "9SHBJFMMXMO")
                            .addHeader("Host", "www.kuwo.cn")
                            .addHeader("Referer", "http://www.kuwo.cn/search/list?key=%E5%BC%A0%E6%9D%B0")
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                            .build();

                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    jsonObject = new JSONObject(responseData);
                    String song_url = jsonObject.getJSONObject("data").getString("url");
                    values.put(NetMusicInfoDBHelper.URL, song_url);
                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME
                            + " (" + NetMusicInfoDBHelper.MUSIC_RID + "," + NetMusicInfoDBHelper.RID
                            + "," + NetMusicInfoDBHelper.SONGNAME + "," + NetMusicInfoDBHelper.ARTIST
                            + "," + NetMusicInfoDBHelper.ALBUMID + "," + NetMusicInfoDBHelper.ALBUM
                            + "," + NetMusicInfoDBHelper.DURATION + "," + NetMusicInfoDBHelper.SCORE100
                            + "," + NetMusicInfoDBHelper.RELEASEDATE + "," + NetMusicInfoDBHelper.SONGTIMEMINUTES
                            + "," + NetMusicInfoDBHelper.ISLISTENFEE + "," + NetMusicInfoDBHelper.PIC
                            + "," + NetMusicInfoDBHelper.PIC120 + "," + NetMusicInfoDBHelper.URL + " ) "
                            + "VALUES ('" + musicRid + "'," + rid + ",'" + songName + "','" + artist
                            + "'," + albumId + ",'" + album + "'," + duration + "," + score100
                            + ",'" + releaseDate + "','" + songTimeMinutes + "','" + isListenFee
                            + "','" + pic + "','" + pic120 + "','" + song_url + "')";
                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + rid + ":" + songName + "保存成功");

                }
                db.close();
                helper.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Looper.loop();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Looper.myLooper().quit();

        }
    }

}
