package com.example.netcloudsharing.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;

import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.netcloudsharing.tool.MusicUtil.closeAllHttp;

public class SaveMusicSetFromNetToMSDB {
    private Context mContext;
    private static final String TAG = "SaveMusicSetFromNetToMS";
    private int bangId;
    private String setName;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setBangId(int bangId) {
        this.bangId = bangId;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public SaveMusicSetFromNetToMSDB(Context mContext, int bangId, String setName) {
        this.mContext = mContext;
        this.bangId = bangId;
        this.setName = setName;
    }

    public void saveToDB() {
        KuWoMusicTask task = new KuWoMusicTask();
        task.execute();
    }

    private class KuWoMusicTask extends AsyncTask<Void, Void, Void> {
        OkHttpClient client;
        Response response;
        @Override
        protected Void doInBackground(Void... voids) {
            Looper.prepare();
            client = new OkHttpClient();
            String url = "http://www.kuwo.cn/api/www/bang/bang/musicList?bangId=" + bangId + "&pn=1&rn=20&httpsStatus=1&reqId=60130810-e1df-11ed-9a30-ab12ee7dc9a1";
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", "_ga=GA1.2.722000474.1681707415; _gid=GA1.2.1385012246.1682173035; _gat=1; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681888282,1681913551,1682173035,1682258452; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682258501; kw_token=THRA8I5GPK")
                    .addHeader("csrf", "THRA8I5GPK")
                    .addHeader("Host", "www.kuwo.cn")
                    .addHeader("Referer", "http://www.kuwo.cn/rankList")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.5359.95 Safari/537.36")
                    .build();
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray musicList = jsonObject.getJSONObject("data").getJSONArray("musicList");
                NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
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
                    rid = song_info.optInt("rid", -1);
                    songName = song_info.optString("name");
                    artist = song_info.optString("artist");
                    artistId = song_info.optInt("artistid", -1);
                    album = song_info.optString("album");
                    albumId = song_info.optInt("albumid", -1);
                    duration = song_info.optInt("duration", -1);
                    score100 = song_info.optInt("score100", -1);
                    releaseDate = song_info.optString("releaseDate");
                    songTimeMinutes = song_info.optString("songTimeMinutes");
                    isListenFee = song_info.optString("isListenFee");
                    pic = song_info.optString("pic");
                    pic120 = song_info.optString("pic120");
                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME_MUSICSET
                            + " (" + NetMusicInfoDBHelper.MUSIC_RID + "," + NetMusicInfoDBHelper.RID
                            + "," + NetMusicInfoDBHelper.SONGNAME + "," + NetMusicInfoDBHelper.ARTIST
                            + "," + NetMusicInfoDBHelper.ALBUMID + "," + NetMusicInfoDBHelper.ALBUM
                            + "," + NetMusicInfoDBHelper.DURATION + "," + NetMusicInfoDBHelper.SCORE100
                            + "," + NetMusicInfoDBHelper.RELEASEDATE + "," + NetMusicInfoDBHelper.SONGTIMEMINUTES
                            + "," + NetMusicInfoDBHelper.ISLISTENFEE + "," + NetMusicInfoDBHelper.PIC
                            + "," + NetMusicInfoDBHelper.PIC120 + "," + NetMusicInfoDBHelper.BANGID
                            + "," + NetMusicInfoDBHelper.ARTISTID + " ) "
                            + "VALUES ('" + musicRid + "'," + rid + ",'" + songName + "','" + artist
                            + "'," + albumId + ",'" + album + "'," + duration + "," + score100
                            + ",'" + releaseDate + "','" + songTimeMinutes + "','" + isListenFee
                            + "','" + pic + "','" + pic120 + "','" + bangId + "','" + artistId + "')";
                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + bangId + ":" + setName + "保存成功");
                }
                db.close();
                helper.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                closeAllHttp(client, response);
                CookieManager.getInstance().setCookie(url, "");
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
