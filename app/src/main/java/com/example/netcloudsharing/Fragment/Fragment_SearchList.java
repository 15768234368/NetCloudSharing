package com.example.netcloudsharing.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.netcloudsharing.MainActivity.binder;

public class Fragment_SearchList extends Fragment {
    private Handler mHandler = new Handler();
    //三个播放歌曲按钮
    private ImageView lastIv, nextIv, playIv, songImage;
    //歌曲歌手
    private TextView singerTv, songTv;


    private View view;
    private RecyclerView musicRv;
    private LocalMusicAdapter adapter;
    //数据源
    List<MusicBean> mData;


    private String searchKey;

    public void setPlayBottomInfo(ImageView lastIv, ImageView playIv, ImageView nextIv, ImageView songImage, TextView singerTv, TextView songTv) {
        this.lastIv = lastIv;
        this.nextIv = nextIv;
        this.playIv = playIv;
        this.songImage = songImage;
        this.singerTv = singerTv;
        this.songTv = songTv;
    }


    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchlist, container, false);
        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        mData = new ArrayList<>();
        //getActivity可能有问题！！！
        adapter = new LocalMusicAdapter(getContext(), mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadNetMusicData();
        adapter.notifyDataSetChanged();
        //设置每一项的点击事件
        setEventListener();
        Permission permission = new Permission();
        permission.checkPermission(getActivity());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 3000); // 延时3秒执行存储音乐信息的操作

    }


    /**
     * 初始化控件的函数
     */
    private void initView() {
        musicRv = view.findViewById(R.id.searchNet_music_rv);


    }

    /**
     * 加载历史播放记录数据库当中的音乐MP3文件到集合中
     */
    private void loadNetMusicData() {
        new Runnable() {
            @Override
            public void run() {
                searchSong(searchKey);
            }
        }.run();
    }

    private void searchSong(String keyword) {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        String querySong = "SELECT * FROM netMusicInfo WHERE songName LIKE ?";
        String queryArtist = "SELECT * FROM netMusicInfo WHERE artist LIKE ?";
        String[] args = new String[]{"%" + keyword + "%"};
        Cursor cursor = db.rawQuery(querySong, args);
        if (cursor.getCount() == 0) {
            cursor.close();
            cursor = db.rawQuery(queryArtist, args);
        }
        int id = 0;
        while (cursor.moveToNext()) {
            id++;
            String song_id = String.valueOf(id);
            String song_title = cursor.getString(2);
            String song_singer = cursor.getString(3);
            String song_album = cursor.getString(5);
            String time = cursor.getString(10);
            String song_rid = cursor.getString(1);
            String pic = cursor.getString(12);
            //将一行当中的数据封装到对象中
            MusicBean bean = new MusicBean(song_id, song_title, song_singer, song_album, time, song_rid, pic, true);
            mData.add(bean);
        }
        cursor.close();
        db.close();
        helper.close();
    }


    private void setEventListener() {
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
