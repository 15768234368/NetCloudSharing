package com.example.netcloudsharing.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.Bean.MusicSetBean;
import com.example.netcloudsharing.tool.HtmlParser;
import com.example.netcloudsharing.tool.RandomUserAgentGenerator;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicFromSetTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "MusicFromSetTask";
    private MusicFromSetListener musicFromSetListener;

    public MusicFromSetTask(MusicFromSetListener musicFromSetListener) {
        this.musicFromSetListener = musicFromSetListener;
    }

    @Override
    protected String doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urls[0])
                .addHeader("cookie", "kg_mid=a8f54eaf7effd382b4db936d2902cf20; kg_dfid=1txtXp0lYJ0c0r1pV11MtP5s; KuGooRandom=66541691400414345; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1691400405,1691729457,1691842117,1691950832; ACK_SERVER_10017={\"list\":[[\"bjverifycode.service.kugou.com\"]]}; ACK_SERVER_10015={\"list\":[[\"bjlogin-user.kugou.com\"]]}; ACK_SERVER_10016={\"list\":[[\"bjreg-user.kugou.com\"]]}; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1691992691")
                .addHeader("referer", "https://www.kugou.com/")
                .addHeader("user-agent", RandomUserAgentGenerator.getRandomUserAgent())
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String responseData) {
        super.onPostExecute(responseData);
        if (responseData != null) {
//            Log.d(TAG, "requestMusicListHtml: " + responseData);
            // 在这里可以处理解析HTML的逻辑或其他操作
            List<String> musicListHref = HtmlParser.parseHtmlByMusicList(responseData);
            MusicFromHrefTask musicFromHrefTask = new MusicFromHrefTask(new MusicFromHrefTask.MusicFromHrefListener() {
                @Override
                public void onSearchComplete(List<MusicBean> musicList) {
                    musicFromSetListener.onSearchComplete(musicList);
                }

                @Override
                public void onSearchError() {

                }
            });
            musicFromHrefTask.execute(musicListHref);
        } else {
            Log.e(TAG, "Failed to fetch music list HTML");
        }
    }
    public interface MusicFromSetListener {
        void onSearchComplete(List<MusicBean> beanList);
        void onSearchError();
    }
}
