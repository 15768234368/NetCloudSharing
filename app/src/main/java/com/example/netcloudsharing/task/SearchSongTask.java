package com.example.netcloudsharing.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SearchSongTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "SearchSongTask";
    private static final String API_URL = "https://complexsearch.kugou.com/v2/search/song";
    private static final String ENCODE_ALBUM_AUDIO_API_URL = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata";
    private static final String APP_ID = "1014";
    private static final String MID = "8a6709b0f4f0674f12dabeb3a710313a";
    private static final String DFID = "4XSnWz14ZQos2PYFIl2MiDLH";
    private static final String PLATFORM = "WebFilter";
    private static final String CALLBACK = "callback123";
    private SearchSongListener listener;
    private String time;
    public SearchSongTask(SearchSongListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        time = String.valueOf(System.currentTimeMillis());
        String keyword = params[0];
        String signature = generateSignature(keyword);

        try {
            URL url = new URL(API_URL + "?callback=" + CALLBACK + "&srcappid=2919&clientver=1000&clienttime=" + time +
                    "&mid=" + MID + "&uuid=" + MID + "&dfid=" + DFID + "&keyword=" + keyword + "&page=1&pagesize=30&bitrate=0&isfuzzy=0&inputtype=0" +
                    "&platform=" + PLATFORM + "&userid=0&iscorrection=1&privilege_filter=0&filter=10&token=&appid=" + APP_ID + "&signature=" + signature);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error searching song: " + e.getMessage());
            return null;
        }
    }


    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            int startIndex = response.indexOf("{"); // 查找第一个左花括号的索引

            int endIndex = response.lastIndexOf("}"); // 查找最后一个右花括号的索引
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                String jsonResponse = response.substring(startIndex, endIndex + 1); // 提取有效的JSON字符串
                listener.onSearchComplete(jsonResponse);
            } else {
                listener.onSearchError();
            }
        } else {
            listener.onSearchError();
        }
    }

    private String generateSignature(String keyword) {
        String data = "NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt" + "appid=" + APP_ID + "bitrate=0" + "callback=" + CALLBACK +
                "clienttime=" + time + "clientver=1000" + "dfid=" + DFID + "filter=10" +
                "inputtype=0" + "iscorrection=1" + "isfuzzy=0" + "keyword=" + keyword + "mid=" + MID + "page=1" +
                "pagesize=30" + "platform=" + PLATFORM + "privilege_filter=0" + "srcappid=2919" + "token=" +
                "userid=0" + "uuid=" + MID + "NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt";

        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5Digest.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                sb.append(Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error generating MD5 signature: " + e.getMessage());
            return "";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public interface SearchSongListener {
        void onSearchComplete(String response);
        void onSearchError();
    }
}