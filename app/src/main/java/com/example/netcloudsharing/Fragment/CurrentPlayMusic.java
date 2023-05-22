package com.example.netcloudsharing.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.MusicUtil;

import java.util.UUID;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class CurrentPlayMusic extends AppCompatActivity implements View.OnClickListener {

    Button btnPlay;
    SeekBar start, end;
    TextView startText, endText, songTitle, singer;
    ImageView songImage;
    ImageView isFavourIv;
    Animation animation;
    int songTotalTime;
    boolean isFavour;

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

        isFavourIv = (ImageView) findViewById(R.id.currentPlayMusic_addToFavorListIv);
        isFavourIv.setOnClickListener(this);

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
        if (binder.isMusicPlaying()) {
            songImage.startAnimation(animation);
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        } else {
            songImage.clearAnimation();
            btnPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
        MusicBean bean = null;
        if (binder != null)
            bean = binder.getMusicBean();
        if (bean != null) {
            songTitle.setText(bean.getSong());
            singer.setText(bean.getSinger());
            MusicUtil.setAlbumImage(songImage, binder.getBitmap());
        }
        if(isFavourMusic(binder.getMusicBean())){
           isFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            isFavourIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private boolean isFavourMusic(MusicBean bean) {
        FavourListDBHelper helper = new FavourListDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (!bean.isNetMusic()) {
            //当前播放的音乐是本地音乐
            int current_lid = bean.getLid();
            Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int lid = cursor.getInt(8);
                if (current_lid == lid) {
                    //是喜欢的音乐
                    cursor.close();
                    db.close();
                    helper.close();
                    return true;
                }
            }
        }else{
            int current_rid = bean.getRid();
            Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int rid = cursor.getInt(5);
                if (current_rid == rid) {
                    //已经是喜欢的音乐了，再次点击取消喜欢
                    cursor.close();
                    db.close();
                    helper.close();
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetText18n")
        @Override
        public void handleMessage(Message message) {
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
    public String createTimeText(int time) {
        String timeText;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeText = min + ":";
        if (sec < 10) timeText += "0";
        timeText += sec;
        return timeText;
    }

    public void PlayButton(View view) {
        if (!binder.isMusicPlaying()) {
            //Stopped
            binder.playMusic();
            songImage.startAnimation(animation);
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        } else {
            //Played
            binder.pauseMusic();
            songImage.clearAnimation();
            btnPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentPlayMusic_addToFavorListIv:
                addToFavourList(binder.getMusicBean());
        }
    }

    /**
     * 将歌曲添加至收藏，分为本地音乐和网络音乐
     * @param musicBean 音乐的基本信息
     */
    private void addToFavourList(MusicBean musicBean) {
        FavourListDBHelper helper = new FavourListDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (!musicBean.isNetMusic()) {
            //当前播放的音乐是本地音乐
            int current_lid = musicBean.getLid();
            Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int lid = cursor.getInt(8);
                if (current_lid == lid) {
                    //已经是喜欢的音乐了，再次点击取消喜欢
                    db.delete(FavourListDBHelper.TABLE_NAME_FAVOURLIST, "lid=?", new String[]{String.valueOf(lid)});
                    isFavourIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    cursor.close();
                    db.close();
                    helper.close();
                    return;
                }
            }
            isFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
            String fid = UUID.randomUUID().toString();
            String songName = musicBean.getSong();
            String artist = musicBean.getSinger();
            String album = musicBean.getAlbum();
            String duration = musicBean.getDuration();
            String path = musicBean.getPath();
            String isNetMusic = "false";
            ContentValues values = new ContentValues();
            values.put(FavourListDBHelper.FID, fid);
            values.put(FavourListDBHelper.SONGNAME, songName);
            values.put(FavourListDBHelper.ARTIST, artist);
            values.put(FavourListDBHelper.ALBUM, album);
            values.put(FavourListDBHelper.DURATION, duration);
            values.put(FavourListDBHelper.PATH, path);
            values.put(FavourListDBHelper.LID, current_lid);
            values.put(FavourListDBHelper.ISNETMUSIC, isNetMusic);
            db.insert(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, values);
            cursor.close();

        } else {
            //当前播放的音乐是网络音乐
            int current_rid = musicBean.getRid();
            Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int rid = cursor.getInt(5);
                if (current_rid == rid) {
                    //已经是喜欢的音乐了，再次点击取消喜欢
                    db.delete(FavourListDBHelper.TABLE_NAME_FAVOURLIST, "rid=?", new String[]{String.valueOf(rid)});
                    isFavourIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    cursor.close();
                    db.close();
                    helper.close();
                    return;
                }
            }
            isFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
            String fid = UUID.randomUUID().toString();
            String songName = musicBean.getSong();
            String artist = musicBean.getSinger();
            String album = musicBean.getAlbum();
            String duration = musicBean.getDuration();
            String pic = musicBean.getPic();
            String isNetMusic = "true";
            ContentValues values = new ContentValues();
            values.put(FavourListDBHelper.FID, fid);
            values.put(FavourListDBHelper.SONGNAME, songName);
            values.put(FavourListDBHelper.ARTIST, artist);
            values.put(FavourListDBHelper.ALBUM, album);
            values.put(FavourListDBHelper.DURATION, duration);
            values.put(FavourListDBHelper.RID, current_rid);
            values.put(FavourListDBHelper.PIC, pic);
            values.put(FavourListDBHelper.ISNETMUSIC, isNetMusic);
            db.insert(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, values);
            cursor.close();

        }
        db.close();
        helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
