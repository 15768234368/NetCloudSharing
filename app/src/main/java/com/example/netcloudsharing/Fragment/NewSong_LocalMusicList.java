package com.example.netcloudsharing.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.LocalMusicAdapter;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class NewSong_LocalMusicList extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NewSong_LocalMusicList.class.getSimpleName();
    //三个播放歌曲按钮
    private ImageView nextIv, playIv, lastIv, album, songImage;
    private ImageButton backIb;
    //歌曲歌手
    private TextView singerTv, songTv;
    private RecyclerView musicRv;
    private LocalMusicAdapter adapter;
    //数据源
    List<MusicBean> mData;
    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;


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
        loadLocalMusicData();
        //设置每一项的点击事件
        setEventListener();
        Permission permission = new Permission();
        permission.checkPermission(this);
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
            MusicBean bean = new MusicBean(sid, song, singer, album, time, path);
            mData.add(bean);
        }
        //数据源发送变化,提示适配器更新
        adapter.notifyDataSetChanged();
        setMusicCount();
        cursor.close();
    }

    public void setMusicCount() {
        SharedPreferences sp = getSharedPreferences("music_local_count", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", mData.size());
        editor.apply();
    }

    /**
     * 初始化控件的函数
     */
    private void initView() {
        nextIv = findViewById(R.id.local_music_bottom_ivNext);
        playIv = findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = findViewById(R.id.local_music_bottom_ivLast);

        backIb = findViewById(R.id.activity_localMusicList_ibBack);

        singerTv = findViewById(R.id.local_music_bottom_tvSinger);
        songTv = findViewById(R.id.local_music_bottom_tvSong);

        musicRv = findViewById(R.id.local_music_rv);
        songImage = findViewById(R.id.local_music_bottom_ivIcon);

        relativeLayout = findViewById(R.id.local_music_bottomLayout);

        backIb.setOnClickListener(this);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);


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
                Intent currentMusicIntent = new Intent(NewSong_LocalMusicList.this, CurrentPlayMusic.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
