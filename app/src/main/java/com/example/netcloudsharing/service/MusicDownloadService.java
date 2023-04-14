package com.example.netcloudsharing.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

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
    private void handleActionDownload(String url, String title) {


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
        MediaScannerConnection.scanFile(getApplication(), new String[] { file.getAbsolutePath() }, null, null);

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
