package com.example.netcloudsharing.tool;

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

public class SaveArtistListFromNetToALDB {
    private Context mContext;
    private static final String TAG = "SaveArtistListFromNetTo";

    public SaveArtistListFromNetToALDB(Context mContext) {
        this.mContext = mContext;
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
            String url = "http://www.kuwo.cn/api/www/artist/artistInfo?category=0&pn=1&rn=60&httpsStatus=1&reqId=b8ff9ca0-e21a-11ed-9d5d-c3ddf32bc7ac";
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", "_ga=GA1.2.1498653801.1682182945; _gid=GA1.2.1543078478.1682282139; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681913595,1682180810,1682182943,1682282139; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682283183; _gat=1; kw_token=SDJM5XQOP2K")
                    .addHeader("csrf", "SDJM5XQOP2K")
                    .addHeader("Host", "www.kuwo.cn")
                    .addHeader("Referer", "http://www.kuwo.cn/singers")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                    .build();
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray musicList = jsonObject.getJSONObject("data").getJSONArray("artistList");
                NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
                for (int i = 0; i < musicList.length(); ++i) {
                    int artistId = 0;
                    String artist = null;
                    String aartist = null;
                    int albumNum = 0;
                    int artistFans = 0;
                    String birthplace = null;
                    String country = null;
                    String gener = null;
                    String info = null;
                    String language = null;
                    int musicNum = 0;
                    int mvNum = 0;
                    String pic = null;
                    JSONObject artistList_info = musicList.getJSONObject(i);
                    artistId = artistList_info.optInt("id", 0);
                    String artistInfoUrl = "http://www.kuwo.cn/api/www/artist/artist?artistid=" + artistId + "&httpsStatus=1&reqId=301ff520-e282-11ed-9d5d-c3ddf32bc7ac";
                    Request requestArtistInfo = new Request.Builder()
                            .url(artistInfoUrl)
                            .addHeader("Cookie", "_ga=GA1.2.1498653801.1682182945; _gid=GA1.2.1543078478.1682282139; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681913595,1682180810,1682182943,1682282139; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682283183; _gat=1; kw_token=TK76OWS4DN")
                            .addHeader("csrf", "TK76OWS4DN")
                            .addHeader("Host", "www.kuwo.cn")
                            .addHeader("Referer", "http://www.kuwo.cn/singers")
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                            .build();
                    Response responseArtistInfo = client.newCall(requestArtistInfo).execute();
                    String responseDataArtistInfo = responseArtistInfo.body().string();
                    JSONObject jsonObjectArtistInfo = new JSONObject(responseDataArtistInfo);
                    JSONObject artist_info = jsonObjectArtistInfo.getJSONObject("data");
                    artist = artist_info.optString("name");
                    aartist = artist_info.optString("aartist");
                    albumNum = artist_info.optInt("albumNum", 0);
                    artistFans = artist_info.optInt("artistFans", 0);
                    birthplace = artist_info.optString("birthplace");
                    country = artist_info.optString("country");
                    gener = artist_info.optString("gener");
                    info = artist_info.optString("info");
                    language = artist_info.optString("language");
                    musicNum = artist_info.optInt("musicNum", 0);
                    mvNum = artist_info.optInt("mvNum", 0);
                    pic = artist_info.optString("pic");

                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME_ARTISTLIST
                            + " (" + NetMusicInfoDBHelper.ARTISTID + "," + NetMusicInfoDBHelper.ARTIST
                            + "," + NetMusicInfoDBHelper.AARTIST + "," + NetMusicInfoDBHelper.ALBUMNUM
                            + "," + NetMusicInfoDBHelper.ARTISTFANS + "," + NetMusicInfoDBHelper.BIRTHPLACE
                            + "," + NetMusicInfoDBHelper.COUNTRY + "," + NetMusicInfoDBHelper.GENER
                            + "," + NetMusicInfoDBHelper.INFO + "," + NetMusicInfoDBHelper.LANGUAGE
                            + "," + NetMusicInfoDBHelper.MUSICNUM + "," + NetMusicInfoDBHelper.MVNUM
                            + "," + NetMusicInfoDBHelper.PIC + " )"
                            + "VALUES (" + artistId + ",'" + artist + "','" + aartist + "' , " + albumNum
                            + "," + artistFans + ",'" + birthplace + "','" + country + "','" + gener
                            + "','" + info + "','" + language + "'," + musicNum + "," + mvNum
                            + ",'" + pic + "')";
                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + artistId + ":" + artist + "保存成功");
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
