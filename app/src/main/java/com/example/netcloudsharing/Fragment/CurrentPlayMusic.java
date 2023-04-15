package com.example.netcloudsharing.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.LocalMusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.MusicUtil;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class CurrentPlayMusic extends AppCompatActivity {

    Button btnPlay;
    SeekBar start, end;
    TextView startText, endText, songTitle, singer;
    ImageView songImage;
    Animation animation;
    int songTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_play_music);

        //Id implementation
        btnPlay = (Button) findViewById(R.id.play);

        startText = (TextView) findViewById(R.id.TextStart);
        endText = (TextView) findViewById(R.id.TextEnd);
        songTitle = (TextView) findViewById(R.id.currentPlayMusic_title);
        singer = (TextView) findViewById(R.id.currentPlayMusic_album);

        songImage = (ImageView) findViewById(R.id.currentPlayMusic_image);

        animation = AnimationUtils.loadAnimation(this, R.anim.rotation);

        //Song Added


        songTotalTime = binder.getMusicDuration();
        //Control Seek bar track Line / Play Line
        start = findViewById(R.id.PlayLine);
        start.setMax(songTotalTime);
        start.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binder.seekTo(progress);
                    start.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Volume control
        end = findViewById(R.id.volume);
        end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumn = progress / 100f;
                binder.setVolume(volumn, volumn);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Up data song time Line
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (binder.isMediaPlay()) {
                    try {
                        Message message = new Message();
                        message.what = binder.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        initMusicView();
    }

    private void initMusicView() {
        if(binder.isMusicPlaying()){
            songImage.startAnimation(animation);
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }else{
            songImage.clearAnimation();
            btnPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
        LocalMusicBean bean = null;
        if(binder != null)
            bean = binder.getMusicBean();
        if(bean != null){
            songTitle.setText(bean.getSong());
            singer.setText(bean.getSinger());
            MusicUtil.setAlbumImage(songImage, binder.getCurrentSongAlbumPath());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("SetText18n")
        @Override
        public void handleMessage(Message message){
            int seekBarPosition = message.what;

            //Updata song seek bar
            start.setProgress(seekBarPosition);

            //Updata Labels
            String Time = createTimeText(seekBarPosition);
            startText.setText(Time);

            //Time calculation
            String remainingTime = createTimeText(songTotalTime - seekBarPosition);
            endText.setText("-" + remainingTime);
        }
    };

    //Time Shows
    public String createTimeText(int time){
        String timeText;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeText = min + ":";
        if(sec < 10) timeText += "0";
        timeText += sec;
        return timeText;
    }

    public void PlayButton(View view){
        if(!binder.isMusicPlaying()){
            //Stopped
            binder.playMusic();
            songImage.startAnimation(animation);
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }else{
            //Played
            binder.pauseMusic();
            songImage.clearAnimation();
            btnPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }
}
