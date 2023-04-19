package com.example.netcloudsharing.Music;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Fragment.CurrentPlayMusic;
import com.example.netcloudsharing.LocalMusicAdapter;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class DownloadedMusicActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DownloadedMusicActivity.class.getSimpleName();
    //三个播放歌曲按钮
    private ImageView nextIv, playIv, lastIv, songImage;
    //歌曲歌手
    private TextView singerTv, songTv;
    private RecyclerView musicRv;
    private LocalMusicAdapter adapter;
    //数据源
    List<MusicBean> mData;
    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;

    private ImageButton downloadMusicBackIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_list);
        initView();

        mData = new ArrayList<>();

        //getActivity可能有问题！！！
        adapter = new LocalMusicAdapter(this, mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadDownloadedMusicData();
        //设置每一项的点击事件
        setEventListener();
        Permission permission = new Permission();
        permission.checkPermission(this);
    }

    private void loadDownloadedMusicData() {
        DownloadedMusicHelper musicHelper = new DownloadedMusicHelper(this);
        SQLiteDatabase db = musicHelper.getReadableDatabase();
        Cursor cursor = db.query(DownloadedMusicHelper.TABLE_NAME, null, null, null, null, null, null);

        int id = 0;
        while (cursor.moveToNext()) {
            id++;
            String song_id = String.valueOf(id);
            String song_title = cursor.getString(1);
            String song_singer = cursor.getString(2);
            String song_album = cursor.getString(3);
            long song_duration = (long)cursor.getDouble(4);
            String song_path = cursor.getString(5);
            int minute = (int) (song_duration / 1000 / 60);
            int second = (int) (song_duration / 1000 % 60);
            String time = String.valueOf(minute) + ":" + String.valueOf(second);
            //将一行当中的数据封装到对象中
            MusicBean bean = new MusicBean(song_id, song_title, song_singer, song_album, time, song_path);
            mData.add(bean);
        }

        cursor.close();
        db.close();
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                MusicBean musicBean = mData.get(position);
                binder.playMusicPosition(musicBean);
                setMusicBean(binder.getMusicBean());
            }
        });
    }

    /**
     * 初始化控件的函数
     */
    private void initView() {
        nextIv = findViewById(R.id.local_music_bottom_ivNext);
        playIv = findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = findViewById(R.id.local_music_bottom_ivLast);
        downloadMusicBackIb = findViewById(R.id.activity_localMusicList_ibBack);

        singerTv = findViewById(R.id.local_music_bottom_tvSinger);
        songTv = findViewById(R.id.local_music_bottom_tvSong);

        musicRv = findViewById(R.id.local_music_rv);
        songImage = findViewById(R.id.local_music_bottom_ivIcon);

        relativeLayout = findViewById(R.id.local_music_bottomLayout);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        downloadMusicBackIb.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_localMusicList_ibBack:
                finish();
                break;
            case R.id.local_music_bottom_ivPlay:
                if (binder.getCurrentPosition() == -1) {
                    //如果没有音乐在播放
                    binder.playMusicPosition(null);
                    setMusicBean(binder.getMusicBean());
                    playIv.setImageResource(R.mipmap.icon_pause);
                    return;
                }
                if (binder.isMusicPlaying()) {
                    //如果音乐正在播放，则暂停
                    binder.pauseMusic();
                    playIv.setImageResource(R.mipmap.icon_play);
                } else {
                    //如果音乐正在暂停，则播放
                    binder.playMusic();
                    playIv.setImageResource(R.mipmap.icon_pause);
                }
                setMusicBean(binder.getMusicBean());
                break;
            case R.id.local_music_bottom_ivLast:
                binder.playLastMusic();
                setMusicBean(binder.getMusicBean());
                break;
            case R.id.local_music_bottom_ivNext:
                binder.playNextMusic();
                setMusicBean(binder.getMusicBean());
                break;
            case R.id.local_music_bottomLayout:
                Intent currentMusicIntent = new Intent(DownloadedMusicActivity.this, CurrentPlayMusic.class);
                startActivity(currentMusicIntent);
                break;
        }
    }

    private void setMusicBean(MusicBean bean) {
        singerTv.setText(bean.getSinger());
        songTv.setText(bean.getSong());
        MusicUtil.setAlbumImage(songImage, binder.getBitmap());
        if (binder.isMusicPlaying()) {
            playIv.setImageResource(R.mipmap.icon_pause);
        } else {
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //第二个判断条件是如果还没有播放，则会返回一个错误的值
        if (binder != null && binder.getCurrentPosition() != -1) {
            setMusicBean(binder.getMusicBean());
        }
    }
}
