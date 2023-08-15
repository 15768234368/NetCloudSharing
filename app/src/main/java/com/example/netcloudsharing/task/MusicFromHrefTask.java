package com.example.netcloudsharing.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.Controller;
import com.example.netcloudsharing.tool.MusicUtil;
import com.example.netcloudsharing.tool.RandomUserAgentGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicFromHrefTask extends AsyncTask<List<String>, Void, List<String>> {
    private static final String TAG = "MusicFromHrefTask";
    private MusicFromHrefListener musicFromHrefListener;

    public MusicFromHrefTask(MusicFromHrefListener musicFromHrefListener) {
        this.musicFromHrefListener = musicFromHrefListener;
    }

    @Override
    protected List<String> doInBackground(List<String>... strings) {
        if (strings == null || strings[0] == null) return null;
        List<String> jsonDatas = new ArrayList<>();
        try {
            for (String href : strings[0]) {
                // 找到最后一个斜杠的位置
                int lastSlashIndex = href.lastIndexOf("/");

                // 截取从最后一个斜杠位置开始到 .html 之前的部分
                String encodeUrl = href.substring(lastSlashIndex + 1, href.indexOf(".html"));

                String encode_album_audio_id = href.substring(lastSlashIndex + 1, href.indexOf(".html"));
                String s_url = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&encode_album_audio_id=" + encode_album_audio_id;
                Log.d(TAG, "doInBackground: " + s_url);

                URL url = new URL(s_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // Add custom headers
                connection.setRequestProperty("cookie", "kg_mid=a8f54eaf7effd382b4db936d2902cf20; kg_dfid=1txtXp0lYJ0c0r1pV11MtP5s; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1691400405,1691729457,1691842117,1691950832; kg_mid_temp=a8f54eaf7effd382b4db936d2902cf20; KuGoo=KugooID=479645634&KugooPwd=444FE012E566631BA91F207B60E044A0&NickName=%u8721%u7b14%u91cc%u6ca1%u6709%u5c0f%u65b0&Pic=http://imge.kugou.com/kugouicon/165/20170118/20170118105914853342.jpg&RegState=1&RegFrom=&t=cb1790e5e42f6d5b109e62fa077d4d15ac06215389521ae0672afb0e37fb3e1e&a_id=1014&ct=1692020612&UserName=%u006b%u0067%u006f%u0070%u0065%u006e%u0034%u0037%u0039%u0036%u0034%u0035%u0036%u0033%u0034&t1=; KugooID=479645634; t=cb1790e5e42f6d5b109e62fa077d4d15ac06215389521ae0672afb0e37fb3e1e; a_id=1014; UserName=kgopen479645634; mid=a8f54eaf7effd382b4db936d2902cf20; dfid=1txtXp0lYJ0c0r1pV11MtP5s; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1692020630");
                connection.setRequestProperty("authority", "wwwapi.kugou.com");
                connection.setRequestProperty("referer", "https://www.kugou.com/");
                connection.setRequestProperty("user-agent", RandomUserAgentGenerator.getRandomUserAgent());

                StringBuilder response = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                jsonDatas.add(response.toString());
            }
        } catch (IOException e) {
            this.musicFromHrefListener.onSearchError();
            return null;
        }

        return jsonDatas;
    }


    @Override
    protected void onPostExecute(List<String> s) {
        if (s == null) return;
        super.onPostExecute(s);
        int i = 0;
        List<MusicBean> mData = new ArrayList<>();
        for (String response : s) {
            String jsonResponse = null;
            if (s != null) {
                int startIndex = response.indexOf("{"); // 查找第一个左花括号的索引

                int endIndex = response.lastIndexOf("}"); // 查找最后一个右花括号的索引
                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    jsonResponse = response.substring(startIndex, endIndex + 1); // 提取有效的JSON字符串
                }
            }
            JSONObject jsonObject = null;
            if (jsonResponse != null) {
                try {
                    jsonObject = new JSONObject(jsonResponse);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String id = String.valueOf(++i);
                    String song = dataObject.optString("song_name");
                    String singer = dataObject.optString("author_name");
                    String album = dataObject.optString("album_name");
                    String duration = MusicUtil.DurationToStringByMiles(dataObject.optString("timelength"));
                    String rid = dataObject.optString("encode_album_audio_id");
                    String audio_id = dataObject.optString("audio_id");
                    String pic = dataObject.optString("img");
                    String realUrl = dataObject.getString("play_url");
                    mData.add(new MusicBean(id, song, singer, album, duration, realUrl, pic, rid, audio_id));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        musicFromHrefListener.onSearchComplete(mData);
    }

    public interface MusicFromHrefListener {
        void onSearchComplete(List<MusicBean> musicList);

        void onSearchError();
    }
}
