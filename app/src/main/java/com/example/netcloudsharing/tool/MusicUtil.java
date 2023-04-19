package com.example.netcloudsharing.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.netcloudsharing.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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


}
