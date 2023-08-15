package com.example.netcloudsharing.Music;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.service.MusicDownloadService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static com.example.netcloudsharing.MainActivity.binder;

public class HttpGetDemoActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView tvPath = null;
    private Button download, delete, onlinPlay, stop = null;
    private static final String TAG = "HttpGetDemoActivity";

    private String pathText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        findView();
        pathText = getIntent().getStringExtra("path");
        new CheckUrlTask().execute(pathText);


    }
    private class CheckUrlTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            int responseCode = -1;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "当前资源不存在，请搜索其他音乐", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void findView() {
        onlinPlay = (Button) findViewById(R.id.btn_play);
        stop = (Button) findViewById(R.id.btn_pause);
        download = (Button) findViewById(R.id.btn_netMusicDownload);

        ButtonClickListener clickListener = new ButtonClickListener();
//        SeekBarChangeEvent seekBarChangeEvent = new SeekBarChangeEvent();

        onlinPlay.setOnClickListener(clickListener);
        stop.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);


    }

    private final class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
//                case R.id.btn_play:
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                binder.playNetMusicBySearch(pathText);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                    break;
                case R.id.btn_pause:
                    //暂停
                    binder.pauseMusic();
                    break;
                case R.id.btn_netMusicDownload:
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    String filename = String.format("%04d-%02d-%02d_%02d-%02d-%02d", year, month, day, hour, minute, second);

                    MusicDownloadService.startActionDownLoad(HttpGetDemoActivity.this, pathText, filename);
                    break;
                default:
                    break;
            }
        }
    }
//
//    class SeekBarChangeEvent implements OnSeekBarChangeListener {
//        int progress;
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress,
//                                      boolean fromUser) {
//            /**
//             * SeekBar的进度变化监听   把百分进度转变为总文件中的大小
//             * progress为百分比
//             * this.progress为实际播放文件当前位置的大小
//             */
//            this.progress = progress * musicPlayer.mediaPlayer.getDuration() / seekBar.getMax();
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            /**
//             * 开始拖动监听
//             */
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            /**
//             * 停止拖动监听
//             * seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
//             */
//            musicPlayer.mediaPlayer.seekTo(progress);
//        }
//    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        binder.stopMusic();
    }

}