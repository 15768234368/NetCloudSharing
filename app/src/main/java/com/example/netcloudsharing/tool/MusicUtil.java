package com.example.netcloudsharing.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.netcloudsharing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MusicUtil {
    private static final String TAG = MusicUtil.class.getSimpleName();

    public static Bitmap getAlbumBitmap(String Path) {
        if (Path == null || TextUtils.isEmpty(Path)) return null;
        if (!FileExists(Path)) return null;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(Path);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();

        if (picture == null) return null;

        return BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }


    public static void getBitmapFromUrl(final String imageUrl, final BitmapListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    listener.onBitmapLoaded(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        listener.onBitmapLoaded(null);
    }


    public static boolean FileExists(String targetFileAbsPath) {
        try {
            File f = new File(targetFileAbsPath);
            if (!f.exists()) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param imageView
     * @param bitmap
     */
    public static void setAlbumImage(ImageView imageView, Bitmap bitmap) {
        if (imageView == null) return;
        if (bitmap == null) imageView.setImageResource(R.mipmap.a1);
        else {
            imageView.setImageBitmap(null);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static String getUrlByNetMusicRid(String rid) {
        String url = null;
        OkHttpClient client = new OkHttpClient();
        String new_url = "http://www.kuwo.cn/api/v1/www/music/playUrl?mid=" + String.valueOf(rid) + "&type=convert_url";
        Request request = new Request.Builder()
                .url(new_url)
                .addHeader("Cookie", "Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1681707517; _ga=GA1.2.1592305065.1681707517; _gid=GA1.2.834743108.1681707517; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1681707525; kw_token=9SHBJFMMXMO")
                .addHeader("csrf", "9SHBJFMMXMO")
                .addHeader("Host", "www.kuwo.cn")
                .addHeader("Referer", "http://www.kuwo.cn/search/list?key=%E5%BC%A0%E6%9D%B0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseData = response.body().string();
            JSONObject jsonObject = new JSONObject(responseData);
            url = jsonObject.getJSONObject("data").getString("url");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获取缩略图的方法
     *
     * @param uri    路径
     * @param width  宽
     * @param height 高
     * @return 缩略图
     */
    public static Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        //获取缩略图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1 ;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static void closeAllHttp(OkHttpClient client, Response response) {
        if (response != null) {
            response.close();
        }
        if (client != null) {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            client = null;
        }
    }

    public static String DurationToString(String duration){
        if(duration == null) return null;
        int time = Integer.parseInt(duration);
        int minute = time / 60;
        int second = time % 60;
        return minute + ":" + second;
    }

    public static String DurationToStringByMiles(String duration){
        if(duration == null) return null;
        int time = Integer.parseInt(duration);
        time = time / 1000;
        int minute = time / 60;
        int second = time % 60;
        return minute + ":" + second;
    }
}
