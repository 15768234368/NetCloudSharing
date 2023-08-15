package com.example.netcloudsharing.activity;

import static com.example.netcloudsharing.MainActivity.binder;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.Bean.MusicSetBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.task.MusicFromSetTask;
import com.example.netcloudsharing.tool.BaseTool;
import com.example.netcloudsharing.tool.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicSetActivity extends AppCompatActivity {
    private static final String TAG = "MusicSetActivity";
    private TextView singerTv, songTv;
    //三个播放歌曲按钮
    private ImageView nextIv, playIv, lastIv, album, songImage;
    private String href;
    private String pic;
    private String title;
    private ImageView iv_setPic;
    private TextView tv_setTitle;
    private RecyclerView recyclerView;
    private List<MusicBean> mData;
    private LocalMusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_set);
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        pic = intent.getStringExtra("pic");
        title = intent.getStringExtra("title");
        Log.d(TAG, "SetHref: " + href);
        mData = new ArrayList<>();
        initView();
        loadData();

    }

    private void loadData() {
        MusicFromSetTask musicFromSetTask = new MusicFromSetTask(new MusicFromSetTask.MusicFromSetListener() {
            @Override
            public void onSearchComplete(List<MusicBean> musicList) {
                mData = new ArrayList<>(musicList);
                adapter = new LocalMusicAdapter(getApplicationContext(), mData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        final MusicBean musicBean = mData.get(position);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    binder.playNetMusicBySearch(musicBean);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        setMusicBean(musicBean);
                    }
                });
            }

            @Override
            public void onSearchError() {
                Toast.makeText(getApplicationContext(), "网络状态不好，请重试",Toast.LENGTH_SHORT).show();
            }
        });

        musicFromSetTask.execute(href);

    }

    private void initView() {
        nextIv = findViewById(R.id.local_music_bottom_ivNext);
        playIv = findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = findViewById(R.id.local_music_bottom_ivLast);

        singerTv = findViewById(R.id.local_music_bottom_tvSinger);
        songTv = findViewById(R.id.local_music_bottom_tvSong);

        songImage = findViewById(R.id.local_music_bottom_ivIcon);

        iv_setPic = findViewById(R.id.music_set_image);
        tv_setTitle = findViewById(R.id.music_set_name);

        recyclerView = findViewById(R.id.musicSet_rv);
        adapter = new LocalMusicAdapter(this, mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                final MusicBean musicBean = mData.get(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            binder.playNetMusicBySearch(musicBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                setMusicBean(musicBean);
            }
        });
        //设置封面图片和歌单标题
        BaseTool.getBitmapFromUrl(this, iv_setPic, pic, 20);
        tv_setTitle.setText(title);
    }

    public void setMusicBean(MusicBean bean) {
        singerTv.setText(bean.getSinger());
        songTv.setText(bean.getSong());
        MusicUtil.setAlbumImage(songImage, binder.getBitmap());
        if (binder.isMusicPlaying()) {
            playIv.setImageResource(R.mipmap.icon_pause);
        } else {
            playIv.setImageResource(R.mipmap.icon_play);
        }


    }

}
