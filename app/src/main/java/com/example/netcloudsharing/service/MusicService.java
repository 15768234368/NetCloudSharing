package com.example.netcloudsharing.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.netcloudsharing.LocalMusicBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private List<LocalMusicBean> mData;
    MediaPlayer mediaPlayer;
    //记录当前正在播放的音乐的位置
    public static int currentPlayPosition = -1;//默认没有播放
    //记录暂停音乐时进度条哦的位置
    int currentPausePositionInSong = 0;
    MyBinder binder;

    public class MyBinder extends Binder {


        /**
         * 根据传入的位置播放音乐
         *
         * @param position 传入的位置
         */
        public void playMusicPosition(int position) {
            if (position >= 0 && position <= mData.size())
                playMusicPosition(mData.get(position));
        }

        /**
         * 根据传入对象播放音乐
         *
         * @param musicBean 传入对象
         */
        public void playMusicPosition(LocalMusicBean musicBean) {
            if (mData.size() == 0) return;
            if (musicBean == null) {
                musicBean = mData.get(0);
                currentPlayPosition = 0;
            }
            stopMusic();
            //重装多媒体播放器
            mediaPlayer.reset();
            //设置新的播放路径
            try {
                mediaPlayer.setDataSource(musicBean.getPath());
                playMusic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 播放音乐的函数 点击播放按钮音乐，或者从暂停从新播放
         * 播放的音乐有两种
         * 1.从暂停到播放
         * 2.从停止到播放
         */
        public void playMusic() {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                if (currentPausePositionInSong == 0) {
                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //从暂停到播放
                    mediaPlayer.seekTo(currentPausePositionInSong);
                    mediaPlayer.start();
                }
            }

        }

        /**
         * 暂停音乐的操作
         */
        public void pauseMusic() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPausePositionInSong = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        }

        /**
         * 停止音乐的操作
         */
        void stopMusic() {
            if (mediaPlayer != null) {
                currentPausePositionInSong = 0;
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();
            }
        }

        /**
         * 播放上一首音乐
         */
        public void playLastMusic() {
            if (currentPlayPosition == -1) {
                Toast.makeText(getApplicationContext(), "没有音乐正在播放中", Toast.LENGTH_SHORT).show();
            } else if (currentPlayPosition == 0) {
                Toast.makeText(getApplicationContext(), "已经是第一首", Toast.LENGTH_SHORT).show();
            } else {
                currentPlayPosition--;
                playMusicPosition(mData.get(currentPlayPosition));
            }
        }

        /**
         * 播放下一首音乐
         */
        public void playNextMusic() {
            if (currentPlayPosition == -1) {
                Toast.makeText(getApplicationContext(), "没有音乐正在播放中", Toast.LENGTH_SHORT).show();
            } else if (currentPlayPosition == mData.size() - 1) {
                Toast.makeText(getApplicationContext(), "已经是最后一首", Toast.LENGTH_SHORT).show();
            } else {
                currentPlayPosition++;
                playMusicPosition(mData.get(currentPlayPosition));
            }
        }

        public LocalMusicBean getMusicBean() {
            return mData.get(currentPlayPosition);
        }

        /**
         * 音乐是否在播放
         *
         * @return 是或否
         */
        public boolean isMusicPlaying() {
            return mediaPlayer.isPlaying();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainActivity", "2");
        mData = new ArrayList<>();
        //加载本地数据源
        loadLocalMusicData();
        mediaPlayer = new MediaPlayer();
        //恢复上次关闭程序的播放位置
        SharedPreferences sp = getSharedPreferences("lastMusicPlayPosition", MODE_PRIVATE);
        currentPlayPosition = sp.getInt("lastMusicPlayPosition", -1);
        //设置播放完成后自动播放下一曲
        setAutoMusic();
    }

    /**
     * 播放完成后自动播放下一曲
     */
    private void setAutoMusic() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (currentPlayPosition == mData.size() - 1) {
                    Toast.makeText(getApplicationContext(), "最后一首歌曲啦，请重新播放", Toast.LENGTH_SHORT).show();
                    currentPlayPosition = 0;
                    binder.stopMusic();
                } else {
                    currentPlayPosition++;
                    binder.playMusicPosition(currentPlayPosition);

                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MainActivity", "3");
        this.binder = new MyBinder();
        return this.binder;
    }

    /**
     * 加载本地存储当中的音乐MP3文件到集合中
     */
    private void loadLocalMusicData() {
        //1.获取ContentResolver对象
        ContentResolver contentResolver = getContentResolver();
        //2.获取本地音乐的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //3.开始查询
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        //4.遍历
        int id = 0;
        while (cursor.moveToNext()) {
            //内存里面存的是毫秒
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int minute = (int) (duration / 1000 / 60);
            int second = (int) (duration / 1000 % 60);
            if (minute < 1) continue;
            id++;
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String time = String.valueOf(minute) + ":" + String.valueOf(second);
            //将一行当中的数据封装到对象中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path);
            mData.add(bean);
        }
    }


}
