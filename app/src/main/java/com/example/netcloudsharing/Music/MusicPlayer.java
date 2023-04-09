package com.example.netcloudsharing.Music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer implements OnBufferingUpdateListener, OnPreparedListener
{
    /* SeekBar拖动条 */
    private SeekBar seekBar = null;
    /* 播放器 */
    public MediaPlayer mediaPlayer = null;
    /* Timer定时器 */
    private Timer mTimer = new Timer();
    private static final int SHOW_SEEKBAR = 0;
    private boolean isPlaying = false;
    public MusicPlayer(SeekBar seekBar)
    {
        this.seekBar = seekBar;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);

        //使用定时器来改变seekBar的进度
        mTimer.schedule(timerTask, 0, 1000);
    }

    TimerTask timerTask = new TimerTask()
    {
        @Override
        public void run()
        {
            if(mediaPlayer == null)
                return;
            if(isPlaying && !seekBar.isPressed())
            {
                mHandler.sendEmptyMessage(SHOW_SEEKBAR);
            }
        }
    };

    Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(msg.what == SHOW_SEEKBAR)
            {
                int posttion = mediaPlayer.getCurrentPosition();//获取当前播放位置
                int duration = mediaPlayer.getDuration();//获取总长度
                int pos = (int)(100*((float)posttion/(float)duration));
                seekBar.setProgress((int)pos);
            }
        }

    };

    public void play(String url)
    {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public void stop()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();//结束后release
            mediaPlayer = null;
            isPlaying = false;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int arg1)
    {
        /**
         * 当网络stream buffer发生改变的时候调用
         * 这里设置seekBar的第二进度条
         */
        this.seekBar.setSecondaryProgress(arg1);
    }


    @Override
    public void onPrepared(MediaPlayer mp)
    {
        /**
         * 当播放文件准备好了后调用
         * 开始播放
         */
        mp.start();
        isPlaying = true;
    }
}