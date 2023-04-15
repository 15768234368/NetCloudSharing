package com.example.netcloudsharing.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.netcloudsharing.R;

import java.io.File;

public class MusicUtil {
    public static Bitmap getAlbumBitmap(String Path) {
        if (Path == null || TextUtils.isEmpty(Path)) return null;
        if(!FileExists(Path)) return null;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(Path);
        byte[] picture= mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();

        if(picture == null) return null;

        return BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }

    public static boolean FileExists(String targetFileAbsPath) {
        try {
            File f = new File(targetFileAbsPath);
            if (!f.exists()) return false;
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public static void setAlbumImage(ImageView imageView, String path){
        if(imageView == null) return ;
        Bitmap bitmap = getAlbumBitmap(path);
        if(bitmap == null) imageView.setImageResource(R.mipmap.a1);
        else {
            imageView.setImageBitmap(null);
            imageView.setImageBitmap(bitmap);
        }
    }
}
