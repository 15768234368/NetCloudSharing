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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.netcloudsharing.tool.MusicUtil.closeAllHttp;

public class SaveMusicFromNetToNMDB {
    private Context mContext;
    private static final String TAG = "SaveMusicFromNetToNMDB";

    public SaveMusicFromNetToNMDB(Context mContext) {
        this.mContext = mContext;
    }

    public void saveToDB(String key) {
        KuWoMusicTask task = new KuWoMusicTask();
        task.execute(key, "0");
    }

    public void saveToDB(int artistId) {
        KuWoMusicTask task = new KuWoMusicTask();
        task.execute(String.valueOf(artistId), "1");
    }

    private class KuWoMusicTask extends AsyncTask<String, Void, Void> {

        OkHttpClient client;
        Response response;
        @Override
        protected Void doInBackground(String... params) {
            Looper.prepare();
            client = new OkHttpClient();
            String url = null;
            Request request = null;
            if (params[1].equals("0")) {

                url = "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=" + params[0] + "&pn=1&rn=20&httpsStatus=1&reqId=c8a89281-dcdc-11ed-88de-4d8ba9a2a5d9";
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", "Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681707517; _ga=GA1.2.1592305065.1681707517; _gid=GA1.2.834743108.1681707517; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1681707525; kw_token=9SHBJFMMXMO")
                        .addHeader("csrf", "9SHBJFMMXMO")
                        .addHeader("Host", "www.kuwo.cn")
                        .addHeader("Referer", "http://www.kuwo.cn/search/list?key=%E5%BC%A0%E6%9D%B0")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                        .build();
            } else if (params[1].equals("1")) {
                url = "http://www.kuwo.cn/api/www/artist/artistMusic?artistid=" + params[0] + "&pn=1&rn=20&httpsStatus=1&reqId=daf57270-e216-11ed-9c37-9578d8fef835";
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", "_ga=GA1.2.1498653801.1682182945; _gid=GA1.2.1543078478.1682282139; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681913595,1682180810,1682182943,1682282139; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682330675; kw_token=L4E2I1CR7VC; _gat=1")
                        .addHeader("csrf", "L4E2I1CR7VC")
                        .addHeader("Host", "www.kuwo.cn")
                        .addHeader("Referer", "http://www.kuwo.cn/singer_detail/" + params[0])
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                        .build();

            }
            try {
                response = client.newCall(request).execute();
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


                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME_NET
                            + " (" + NetMusicInfoDBHelper.MUSIC_RID + "," + NetMusicInfoDBHelper.RID
                            + "," + NetMusicInfoDBHelper.SONGNAME + "," + NetMusicInfoDBHelper.ARTIST
                            + "," + NetMusicInfoDBHelper.ALBUMID + "," + NetMusicInfoDBHelper.ALBUM
                            + "," + NetMusicInfoDBHelper.DURATION + "," + NetMusicInfoDBHelper.SCORE100
                            + "," + NetMusicInfoDBHelper.RELEASEDATE + "," + NetMusicInfoDBHelper.SONGTIMEMINUTES
                            + "," + NetMusicInfoDBHelper.ISLISTENFEE + "," + NetMusicInfoDBHelper.PIC
                            + "," + NetMusicInfoDBHelper.PIC120 + "," + NetMusicInfoDBHelper.ARTISTID + " ) "
                            + "VALUES ('" + musicRid + "'," + rid + ",'" + songName + "','" + artist
                            + "'," + albumId + ",'" + album + "'," + duration + "," + score100
                            + ",'" + releaseDate + "','" + songTimeMinutes + "','" + isListenFee
                            + "','" + pic + "','" + pic120 + "'," + artistId + ")";
                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + rid + ":" + songName + "保存成功");
                }
                db.close();
                helper.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                closeAllHttp(client, response);
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
