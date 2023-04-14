package com.example.netcloudsharing.Music;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.service.MusicDownloadService;

import java.io.IOException;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class HttpGetDemoActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView tvPath = null;
    private Button download, delete, onlinPlay, stop = null;
    private SeekBar seekBar = null;
    private static final String TAG = "HttpGetDemoActivity";
    private MusicPlayer musicPlayer = null;

    private String pathText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        findView();
        pathText = getIntent().getStringExtra("path");
        musicPlayer = new MusicPlayer(seekBar);

    }

    private void findView() {
        onlinPlay = (Button) findViewById(R.id.btn_play);
        stop = (Button) findViewById(R.id.btn_pause);
        download = (Button) findViewById(R.id.btn_netMusicDownload);

        seekBar = (SeekBar) findViewById(R.id.seekBar_playing);
        ButtonClickListener clickListener = new ButtonClickListener();
        SeekBarChangeEvent seekBarChangeEvent = new SeekBarChangeEvent();

        onlinPlay.setOnClickListener(clickListener);
        stop.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);

        seekBar.setOnSeekBarChangeListener(seekBarChangeEvent);

    }

    private final class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_play:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                binder.playNetMusicBySearch(pathText);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case R.id.btn_pause:
                    //暂停
                    musicPlayer.pause();
                    break;
                case R.id.btn_netMusicDownload:
                    MusicDownloadService.startActionDownLoad(HttpGetDemoActivity.this, pathText, "English_Song");
                    break;
                default:
                    break;
            }
        }
    }

    class SeekBarChangeEvent implements OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            /**
             * SeekBar的进度变化监听   把百分进度转变为总文件中的大小
             * progress为百分比
             * this.progress为实际播放文件当前位置的大小
             */
            this.progress = progress * musicPlayer.mediaPlayer.getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            /**
             * 开始拖动监听
             */
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            /**
             * 停止拖动监听
             * seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
             */
            musicPlayer.mediaPlayer.seekTo(progress);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        musicPlayer.stop();
    }

}