package com.example.netcloudsharing.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.LocalMusicAdapter;
import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class NewSong_FavourMusic extends AppCompatActivity {
    List<MusicBean> mData;
    private TextView updateTimeTv;
    private RecyclerView musicRv;
    private LocalMusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song__favour_music);
        initView();
        mData = new ArrayList<>();
        //getActivity可能有问题！！！
        adapter = new LocalMusicAdapter(this, mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadFavourListData();
        adapter.notifyDataSetChanged();

        //设置每一项的点击事件
        setEventListener();
        Permission permission = new Permission();
        permission.checkPermission(this);
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
                            if(musicBean.isNetMusic())
                                binder.playNetMusicBySearch(musicBean);
                            else
                                binder.playMusicPosition(musicBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 加载历史播放记录数据库当中的音乐MP3文件到集合中
     */
    private void loadFavourListData() {
        new Runnable() {
            @Override
            public void run() {
                loadFavourListDB();
            }
        }.run();
    }

    private void loadFavourListDB() {
        FavourListDBHelper helper = new FavourListDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURLIST, null, null, null, null, null, null);
        int id = 0;
        while (cursor.moveToNext()) {
            id++;
            String song_id = String.valueOf(id);
            String songName = cursor.getString(1);
            String artist = cursor.getString(2);
            String album = cursor.getString(3);
            String duration = cursor.getString(4);
            int rid = cursor.getInt(5);
            String pic = cursor.getString(6);
            String path = cursor.getString(7);
            int lid = cursor.getInt(8);
            String isNetMusicForDB = cursor.getString(9);
            boolean isNetMusic = false;
            if (isNetMusicForDB.equals("true"))
                isNetMusic = true;

            //将一行当中的数据封装到对象中
            MusicBean bean = new MusicBean(song_id, songName, artist, album, duration, path, pic, rid, lid, isNetMusic);
            mData.add(bean);
        }
        cursor.close();
        db.close();
        helper.close();
    }


    /**
     * 初始化控件的函数
     */
    private void initView() {
        musicRv = (RecyclerView) findViewById(R.id.favour_music_rv);
        updateTimeTv = (TextView) findViewById(R.id.favourList_updateTime);



        updateTimeTv.setText("更新时间：" + getTodayTime());
    }
    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.format("%02d-%02d", month, day);
    }
}
