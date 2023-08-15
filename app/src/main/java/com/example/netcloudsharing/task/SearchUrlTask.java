package com.example.netcloudsharing.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.netcloudsharing.Controller;
import com.example.netcloudsharing.GlobalVariable;
import com.example.netcloudsharing.tool.RandomUserAgentGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchUrlTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "SearchUrlTask";
    private SearchUrlListener listener;

    public SearchUrlTask(SearchUrlListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String encode_album_audio_id = params[0];
        String s_url = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&encode_album_audio_id=" + encode_album_audio_id;
        Log.d(TAG, "doInBackground: " + s_url);
        try {
            URL url = new URL(s_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Add custom headers
            connection.setRequestProperty("cookie", GlobalVariable.Cookie);
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

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
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

    public interface SearchUrlListener {
        void onSearchComplete(String response);

        void onSearchError();
    }
}
