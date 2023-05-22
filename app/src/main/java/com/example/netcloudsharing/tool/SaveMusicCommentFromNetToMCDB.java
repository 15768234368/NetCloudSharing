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

public class SaveMusicCommentFromNetToMCDB {
    private Context mContext;
    private static final String TAG = "SaveMusicCommentFromNet";

    public SaveMusicCommentFromNetToMCDB(Context mContext) {
        this.mContext = mContext;
    }

    public void saveToDB() {
        KuWoMusicTask task = new KuWoMusicTask();
        task.execute();
    }

    private class KuWoMusicTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Looper.prepare();
            OkHttpClient client = new OkHttpClient();
            //comment_type = "hot"
            String url = "http://www.kuwo.cn/comment?type=get_rec_comment&f=web&page=1&rows=5&digest=8&sid=1082685104&uid=0&prod=newWeb&httpsStatus=1&reqId=972086d0-e355-11ed-9b04-b58cd7128412";
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", "_ga=GA1.2.754058007.1681737334; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681737334,1681883601,1682411900; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682411900; _gid=GA1.2.1962766368.1682411900; _gat=1; kw_token=GWFU1ZP3OAT")
                    .addHeader("Host", "www.kuwo.cn")
                    .addHeader("Referer", "http://www.kuwo.cn/playlist_detail/1082685104")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Core/1.94.197.400 QQBrowser/11.7.5287.400")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray commentList = jsonObject.getJSONArray("rows");
                NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
                for (int i = 0; i < commentList.length(); ++i) {
                    JSONObject comment_info = commentList.getJSONObject(i);
                    int cid = comment_info.getInt("id");
                    int like_num = comment_info.getInt("like_num");
                    String msg = comment_info.getString("msg");
                    String time = comment_info.getString("time");
                    int u_id = comment_info.getInt("u_id");
                    String u_name = comment_info.getString("u_name");
                    String u_pic = comment_info.getString("u_pic");
                    String comment_type = "hot";

                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME_COMMENT
                            + " (" + NetMusicInfoDBHelper.CID + "," + NetMusicInfoDBHelper.LIKE_NUM
                            + "," + NetMusicInfoDBHelper.MSG + "," + NetMusicInfoDBHelper.TIME
                            + "," + NetMusicInfoDBHelper.U_ID + "," + NetMusicInfoDBHelper.U_NAME
                            + "," + NetMusicInfoDBHelper.U_PIC + "," + NetMusicInfoDBHelper.COMMENT_TYPE + " ) "
                            + "VALUES (" + cid + "," + like_num + ",'" + msg + "','" + time + "'," + u_id
                            + ",'" + u_name + "','" + u_pic + "','" + comment_type + "' )";


                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + msg);
                }
                //comment_type = "normal"
                url = "http://www.kuwo.cn/comment?type=get_comment&f=web&page=1&rows=5&digest=8&sid=1082685104&uid=0&prod=newWeb&httpsStatus=1&reqId=97332470-e355-11ed-9b04-b58cd7128412";
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", "_ga=GA1.2.754058007.1681737334; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681737334,1681883601,1682411900; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1682411900; _gid=GA1.2.1962766368.1682411900; _gat=1; kw_token=GWFU1ZP3OAT")
                        .addHeader("Host", "www.kuwo.cn")
                        .addHeader("Referer", "http://www.kuwo.cn/playlist_detail/1082685104")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Core/1.94.197.400 QQBrowser/11.7.5287.400")
                        .build();
                response = client.newCall(request).execute();
                responseData = response.body().string();
                jsonObject = new JSONObject(responseData);
                commentList = jsonObject.getJSONArray("rows");
                for (int i = 0; i < commentList.length(); ++i) {
                    JSONObject comment_info = commentList.getJSONObject(i);
                    int cid = comment_info.getInt("id");
                    int like_num = comment_info.getInt("like_num");
                    String msg = comment_info.getString("msg");
                    String time = comment_info.getString("time");
                    int u_id = comment_info.getInt("u_id");
                    String u_name = comment_info.getString("u_name");
                    String u_pic = comment_info.getString("u_pic");
                    String comment_type = "normal";

                    String insert_sql = "INSERT OR IGNORE INTO " + NetMusicInfoDBHelper.TABLE_NAME_COMMENT
                            + " (" + NetMusicInfoDBHelper.CID + "," + NetMusicInfoDBHelper.LIKE_NUM
                            + "," + NetMusicInfoDBHelper.MSG + "," + NetMusicInfoDBHelper.TIME
                            + "," + NetMusicInfoDBHelper.U_ID + "," + NetMusicInfoDBHelper.U_NAME
                            + "," + NetMusicInfoDBHelper.U_PIC + "," + NetMusicInfoDBHelper.COMMENT_TYPE + " ) "
                            + "VALUES (" + cid + "," + like_num + ",'" + msg + "','" + time + "'," + u_id
                            + ",'" + u_name + "','" + u_pic + "','" + comment_type + "' )";


                    db.execSQL(insert_sql);
                    Log.d(TAG, "doInBackground: " + msg);
                }
                db.close();
                helper.close();
                closeAllHttp(client, response);
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
