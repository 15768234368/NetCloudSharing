package com.example.netcloudsharing.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.netcloudsharing.LocalMusicBean;
import com.example.netcloudsharing.Music.HistoryMusicForUserHelper;
import com.example.netcloudsharing.User.UserBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


// 导入 ID3 标签库

public class MusicService extends Service {
    private final String TAG = MusicService.class.getSimpleName();   //TAG信息
    private List<LocalMusicBean> mData;                 //音乐数据链表
    MediaPlayer mediaPlayer;                            //音乐播放器
    //记录当前正在播放的音乐的位置（即播放到了第几首歌了）
    private int currentPlayPosition = -1;         //默认没有播放
    //记录暂停音乐时进度条哦的位置
    int currentPausePositionInSong = 0;
    MyBinder binder;
    private SQLiteDatabase db = null;

    public class MyBinder extends Binder {

        public int getCurrentPosition() {
            return currentPlayPosition;
        }

        public void setVolume(float left, float right) {
            if (mediaPlayer != null)
                mediaPlayer.setVolume(left, right);
        }

        public int getCurrentPositionInSong() {
            return mediaPlayer.getCurrentPosition();
        }

        public boolean isMediaPlay() {
            return mediaPlayer != null;
        }

        public void seekTo(int progress) {
            if (mediaPlayer != null && currentPlayPosition != -1)
                mediaPlayer.seekTo(progress);
        }

        public int getMusicDuration() {
            if (mediaPlayer != null && currentPlayPosition != -1)
                return mediaPlayer.getDuration();
            else return -1;
        }

        public String getCurrentSongAlbumPath() {
            return mData.get(currentPlayPosition).getPath();
        }

        /**
         * 根据传入的位置播放音乐
         *
         * @param position 传入的位置
         */
        public void playMusicPosition(int position) {

            if (position >= 0 && position <= mData.size())
                playMusicPosition(mData.get(position));
        }

        public void playNetMusicBySearch(String path) throws IOException {
            stopMusic();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }

        /**
         * 根据传入对象播放音乐
         *
         * @param musicBean 传入对象
         */
        public void playMusicPosition(final LocalMusicBean musicBean) {
            int i = 0;
            if (musicBean != null) {
                for (LocalMusicBean bean : mData) {
                    if (bean.getPath().equals(musicBean.getPath()))
                        break;
                    i++;
                }
            }
            Log.d(TAG, "playMusicPosition: " + i);
            currentPlayPosition = i;
            LocalMusicBean bean = mData.get(currentPlayPosition); //当前播放的音乐
            if (mData.size() == 0) return;
            if (bean == null) { //播放第一首歌
                bean = mData.get(0);
            }
            stopMusic(); //播放之前先重装
            //重装多媒体播放器
            mediaPlayer.reset();
            //设置新的播放路径
            try {
                mediaPlayer.setDataSource(bean.getPath());
                playMusic();
                writeTrackOfMusic(bean, new UserBean("zzc", "1")); //每播放一条音乐就记录入历史播放记录库
                Log.d(TAG, "run:播放当前音乐 " + musicBean.getSong());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void writeTrackOfMusic(LocalMusicBean musicBean, UserBean userBean) {
            HistoryMusicForUserHelper helper = new HistoryMusicForUserHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            String track_id = UUID.randomUUID().toString().replaceAll("-", "");
            //为每条音乐记录生成一个独一无二的id

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String listen_time = String.format("%04d/%02d/%02d/%02d:%02d:%02d", year, month, day, hour, minute, second);

            values.put(HistoryMusicForUserHelper.TRACK_ID, track_id);
            values.put(HistoryMusicForUserHelper.SONG_ID, musicBean.getId());
            values.put(HistoryMusicForUserHelper.SONG_TITLE, musicBean.getSong());
            values.put(HistoryMusicForUserHelper.SONG_SINGER, musicBean.getSinger());
            values.put(HistoryMusicForUserHelper.SONG_ALBUM, musicBean.getAlbum());
            values.put(HistoryMusicForUserHelper.USER, userBean.getUser_name());
            values.put(HistoryMusicForUserHelper.USER_ID, userBean.getUser_id());
            values.put(HistoryMusicForUserHelper.LISTEN_TIME, listen_time);
            values.put(HistoryMusicForUserHelper.SONG_PATH, musicBean.getPath());
            db.insert(HistoryMusicForUserHelper.TABLE_NAME, null, values);
            db.close();
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
        public void stopMusic() {
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
                Log.d(TAG, "playLastMusic: " + mData.get(currentPlayPosition).getSong() + " Position is " + currentPlayPosition);
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
                Log.d(TAG, "playNextMusic: " + mData.get(currentPlayPosition).getSong() + " Position is " + currentPlayPosition);
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
        Log.d(TAG, "2");
        mData = new ArrayList<>();
        //加载本地数据源
        loadLocalMusicData();
        mediaPlayer = new MediaPlayer();
        //设置播放器的音量为系统音量
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = (float) currentVolume / maxVolume;
        mediaPlayer.setVolume(volume, volume);

        //恢复上次关闭程序的播放位置
        SharedPreferences sp = getSharedPreferences("lastMusicPlayPosition", MODE_PRIVATE);
        currentPlayPosition = sp.getInt("lastMusicPlayPosition", -1);
//        设置播放完成后自动播放下一曲
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
                    Log.d(TAG, "onCompletion: ");
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
    public void onDestroy() {
        Log.d(TAG, "onDestroy: musicService is destroying");
        SharedPreferences sp = getSharedPreferences("lastMusicPlayPosition", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lastMusicPlayPosition", currentPlayPosition);
        edit.apply();//从commit修改到了apply
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "3");
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
        cursor.close();

    }


}
