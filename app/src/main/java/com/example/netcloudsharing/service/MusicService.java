package com.example.netcloudsharing.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.helper.PlayListHelper;
import com.example.netcloudsharing.tool.BitmapListener;
import com.example.netcloudsharing.tool.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 导入 ID3 标签库

public class MusicService extends Service {
    private final String TAG = MusicService.class.getSimpleName();   //TAG信息
    private List<MusicBean> mData;                 //音乐数据链表
    MediaPlayer mediaPlayer;                            //音乐播放器
    //记录当前正在播放的音乐的位置（即播放到了第几首歌了）
//    private int currentPlayPosition = -1;         //默认没有播放
    //记录暂停音乐时进度条哦的位置
    int currentPausePositionInSong = 0;
    MyBinder binder;
    private SQLiteDatabase db = null;
    private Bitmap bitmap = null;
    MusicBean currentBean = null;

    public class MyBinder extends Binder {
        public MusicBean getCurrentNetBean() {
            return currentBean;
        }

        public void setCurrentNetBean(MusicBean bean) {
            currentBean = bean;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap map) {
            bitmap = map;
        }

//        public int getCurrentPosition() {
//            return currentPlayPosition;
//        }

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
            if (mediaPlayer != null)
                mediaPlayer.seekTo(progress);
        }

        public int getMusicDuration() {
            if (mediaPlayer != null)
                return mediaPlayer.getDuration();
            else return -1;
        }


        /**
         * 根据传入的位置播放音乐
         *
         * @param position 传入的位置
         */
//        public void playMusicPosition(int position) {
//
//            if (position >= 0 && position <= mData.size())
//                playMusicPosition(mData.get(position));
//        }

        /**
         * 播放从网络上搜索到的歌曲
         *
         * @param bean 播放歌曲的基本信息
         * @throws IOException 抛出的异常
         */
        public void playNetMusicBySearch(final MusicBean bean) throws IOException {
            if(bean == null) return ;
            binder.setCurrentNetBean(bean);
            PlayListHelper helper = new PlayListHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            // 在适当的地方添加数据到数据库
            ContentValues values = new ContentValues();
            values.put(PlayListHelper.COLUMN_SONG, bean.getSong());
            values.put(PlayListHelper.COLUMN_SINGER, bean.getSinger());
            values.put(PlayListHelper.COLUMN_ALBUM, bean.getAlbum());
            values.put(PlayListHelper.COLUMN_DURATION, bean.getDuration());
            values.put(PlayListHelper.COLUMN_RID, bean.getRid());
            values.put(PlayListHelper.COLUMN_AUDIO_ID, bean.getAudio_id());
            values.put(PlayListHelper.COLUMN_PIC, bean.getPic());
            values.put(PlayListHelper.COLUMN_REAL_URL, bean.getPath());
            long newRowId = db.insert(PlayListHelper.TABLE_NAME, null, values);

            // 关闭数据库连接
            db.close();
            helper.close();
            // 在适当的地方发送广播消息
            Intent intent = new Intent("com.example.musicplayer.PLAY_FINISHED");
            sendBroadcast(intent);
            stopMusic();
            mediaPlayer.reset(); // 先重置MediaPlayer对象
            mediaPlayer.setDataSource(bean.getPath());
            Log.d(TAG, "playNetMusicBySearch: " + bean.getPath());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared: ");
                    mp.start(); // 等位图加载完成后，开始播放音乐
                    // 在适当的地方发送广播消息
                    sendBroadcast(new Intent("com.example.musicplayer.UPDATE_MUSICDATA"));
                }
            });
            Log.d(TAG, "playNetMusicBySearch: " + bean.getPic());
            MusicUtil.getBitmapFromUrl(bean.getPic(), new BitmapListener() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    binder.setBitmap(bitmap);
                }
            });
        }

//        /**
//         * 根据传入对象播放音乐
//         *
//         * @param musicBean 传入对象
//         */
//        public void playMusicPosition(final MusicBean musicBean) {
//            int i = 0;
//            if (musicBean != null) {
//                for (MusicBean bean : mData) {
//                    if (bean.getPath().equals(musicBean.getPath()))
//                        break;
//                    i++;
//                }
//            }
//            Log.d(TAG, "playMusicPosition: " + i);
//            currentPlayPosition = i;
//            MusicBean bean = mData.get(currentPlayPosition); //当前播放的音乐
//            if (mData.size() == 0) return;
//            if (bean == null) { //播放第一首歌
//                bean = mData.get(0);
//            }
//            stopMusic(); //播放之前先重装
//            //重装多媒体播放器
//            mediaPlayer.reset();
//            //设置新的播放路径
//            try {
//                setBitmap(MusicUtil.getAlbumBitmap(bean.getPath()));
//                binder.setCurrentNetBean(bean);
//                mediaPlayer.setDataSource(bean.getPath());
//                playMusic();
//                writeTrackOfMusic(bean, new UserBean("zzc", "1")); //每播放一条音乐就记录入历史播放记录库
//                Log.d(TAG, "run:播放当前音乐 " + musicBean.getSong());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

//        /**
//         * 新增用户历史播放记录
//         *
//         * @param musicBean 播放歌曲的基本信息
//         * @param userBean  播放歌曲的用户
//         */
//        private void writeTrackOfMusic(MusicBean musicBean, UserBean userBean) {
//            HistoryMusicForUserHelper helper = new HistoryMusicForUserHelper(getApplicationContext());
//            SQLiteDatabase db = helper.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            String track_id = UUID.randomUUID().toString().replaceAll("-", "");
//            //为每条音乐记录生成一个独一无二的id
//
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//            int second = calendar.get(Calendar.SECOND);
//
//            String listen_time = String.format("%04d/%02d/%02d/%02d:%02d:%02d", year, month, day, hour, minute, second);
//
//            values.put(HistoryMusicForUserHelper.TRACK_ID, track_id);
//            values.put(HistoryMusicForUserHelper.SONG_ID, musicBean.getId());
//            values.put(HistoryMusicForUserHelper.SONG_TITLE, musicBean.getSong());
//            values.put(HistoryMusicForUserHelper.SONG_SINGER, musicBean.getSinger());
//            values.put(HistoryMusicForUserHelper.SONG_ALBUM, musicBean.getAlbum());
//            values.put(HistoryMusicForUserHelper.USER, userBean.getUser_name());
//            values.put(HistoryMusicForUserHelper.USER_ID, userBean.getUser_id());
//            values.put(HistoryMusicForUserHelper.LISTEN_TIME, listen_time);
//            values.put(HistoryMusicForUserHelper.SONG_PATH, musicBean.getPath());
//            db.insert(HistoryMusicForUserHelper.TABLE_NAME, null, values);
//            db.close();
//        }


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
//        public void playLastMusic() {
//            if (currentPlayPosition == -1) {
//                Toast.makeText(getApplicationContext(), "没有音乐正在播放中", Toast.LENGTH_SHORT).show();
//            } else if (currentPlayPosition == 0) {
//                Toast.makeText(getApplicationContext(), "已经是第一首", Toast.LENGTH_SHORT).show();
//            } else {
//                currentPlayPosition--;
//                Log.d(TAG, "playLastMusic: " + mData.get(currentPlayPosition).getSong() + " Position is " + currentPlayPosition);
//                playMusicPosition(mData.get(currentPlayPosition));
//            }
//        }
//
//        /**
//         * 播放下一首音乐
//         */
//        public void playNextMusic() {
//            if (currentPlayPosition == -1) {
//                Toast.makeText(getApplicationContext(), "没有音乐正在播放中", Toast.LENGTH_SHORT).show();
//            } else if (currentPlayPosition == mData.size() - 1) {
//                Toast.makeText(getApplicationContext(), "已经是最后一首", Toast.LENGTH_SHORT).show();
//            } else {
//                currentPlayPosition++;
//                Log.d(TAG, "playNextMusic: " + mData.get(currentPlayPosition).getSong() + " Position is " + currentPlayPosition);
//                playMusicPosition(mData.get(currentPlayPosition));
//            }
//        }
        public MusicBean getMusicBean() {
            return currentBean;
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
        mData = new ArrayList<>();
        //加载本地数据源
        mediaPlayer = new MediaPlayer();
        //设置播放器的音量为系统音量
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = (float) currentVolume / maxVolume;
        mediaPlayer.setVolume(volume, volume);
//        Log.d(TAG, "onCreate: " + currentPlayPosition);
//      设置播放完成后自动播放下一曲
//        setAutoMusic();
//        if (currentPlayPosition >= 0 && currentPlayPosition < mData.size())
//            currentBean = mData.get(currentPlayPosition);
    }


    /**
     * 播放完成后自动播放下一曲
     */
//    private void setAutoMusic() {
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                if (currentPlayPosition == mData.size() - 1) {
//                    Log.d(TAG, "onCompletion: ");
//                    Toast.makeText(getApplicationContext(), "最后一首歌曲啦，请重新播放", Toast.LENGTH_SHORT).show();
//                    currentPlayPosition = 0;
//                    binder.stopMusic();
//                } else {
//                    currentPlayPosition++;
//                    binder.playMusicPosition(currentPlayPosition);
//
//                }
//            }
//        });
//    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "3");
        this.binder = new MyBinder();
        return this.binder;
    }

}
