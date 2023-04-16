package com.example.netcloudsharing.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.netcloudsharing.Music.DownloadedMusicHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MusicDownloadService extends IntentService {
    private static final String TAG = "MusicDownloadService";
    private Handler mHandler = new Handler();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD = "com.example.netcloudsharing.service.action.DOWNLOAD";

    // TODO: Rename parameters
    private static final String EXTRA_URL = "com.example.netcloudsharing.service.extra.URL";
    private static final String EXTRA_TITLE = "com.example.netcoludsharing.service.extra.TITLE";

    public MusicDownloadService() {
        super("MusicDownloadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownLoad(Context context, String url, String title) {
        Intent intent = new Intent(context, MusicDownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String url = intent.getStringExtra(EXTRA_URL);
                final String title = intent.getStringExtra(EXTRA_TITLE);
                handleActionDownload(url, title);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownload(String url, final String title) {


        // TODO: Handle action DownLoad
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsoluteFile();
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }

        File file = new File(musicDir, title + ".mp3");

        try {
            FileOutputStream output = new FileOutputStream(file);
            URLConnection urlConnection = new URL(url).openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.connect();
            int fileLength = connection.getContentLength();
            Log.d(TAG, "MusicFileLength is " + fileLength);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            byte[] content = readStream(in);
            output.write(content, 0, fileLength);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用以下代码强制媒体库更新：
        MediaScannerConnection.scanFile(getApplication(), new String[]{file.getAbsolutePath()}, null, null);
        // 将下载好的音乐信息存入数据库
        final String path = musicDir.toString() + "/" + title + ".mp3";

        Runnable saveMusicInfoRunnable = new Runnable() {
            @Override
            public void run() {
                // 将保存音乐信息的代码放在这里
                saveMusicInfoToDB(path, title);
            }
        };

        mHandler.postDelayed(saveMusicInfoRunnable, 3000); // 延时3秒执行存储音乐信息的操作


    }

    public void saveMusicInfoToDB(String path, String title) {
        long id = -1;
        String selection = MediaStore.Audio.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{path};
        String[] projection = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);


        if (cursor != null && cursor.moveToNext()) {
            String song_id = title;
            String song_title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            Log.d(TAG, "saveMusicInfoToDB: song_title is " + song_title);
            String song_singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            Log.d(TAG, "saveMusicInfoToDB: song_singer is " + song_singer);
            String song_album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            Log.d(TAG, "saveMusicInfoToDB: song_album is " + song_album);
            String song_path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.d(TAG, "saveMusicInfoToDB: song_path is " + song_path);
            long song_duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            Log.d(TAG, "saveMusicInfoToDB: song_duration is " + song_duration);
            DownloadedMusicHelper musicHelper = new DownloadedMusicHelper(this);
            SQLiteDatabase db = musicHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            try {
                // 执行数据库操作
                values.put(DownloadedMusicHelper.SONG_ID, song_id);
                values.put(DownloadedMusicHelper.SONG_TITLE, song_title);
                values.put(DownloadedMusicHelper.SONG_SINGER, song_singer);
                values.put(DownloadedMusicHelper.SONG_ALBUM, song_album);
                values.put(DownloadedMusicHelper.SONG_DURATION, song_duration);
                values.put(DownloadedMusicHelper.SONG_PATH, song_path);

                id = db.insert(DownloadedMusicHelper.TABLE_NAME, null, values);
            } catch (SQLException e) {
                Log.e(TAG, "Error executing SQL statement: " + e.getMessage());
            }
            Log.d(TAG, "saveMusicInfoToDB: insertRow is " + id);
            db.close();
        }
        if (id != -1) {
            Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null)
            cursor.close();
    }


    private byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte data[] = new byte[1024];
        long total = 0;
        int count;
        while ((count = in.read(data)) != -1) {
            total += count;
            bos.write(data, 0, count);
        }
        bos.close();
        in.close();
        return bos.toByteArray();
    }

}
