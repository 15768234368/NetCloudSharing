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
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.SaveDayRecommendFromNetToDRDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

public class NewSong_DayRecommend extends AppCompatActivity {

    List<MusicBean> mData;
    private TextView updateTimeTv;
    private RecyclerView musicRv;
    private LocalMusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song__day_recommend);

        initView();
        SaveDayRecommendFromNetToDRDB saveDayRecommendFromNetToDRDB = new SaveDayRecommendFromNetToDRDB(this);
        saveDayRecommendFromNetToDRDB.saveToDB();

        mData = new ArrayList<>();
        //getActivity可能有问题！！！
        adapter = new LocalMusicAdapter(this, mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadDayRecommendData();
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
                            binder.playNetMusicBySearch(musicBean);
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
    private void loadDayRecommendData() {
        new Runnable() {
            @Override
            public void run() {
                loadDayRecommendDB();
            }
        }.run();
    }

    private void loadDayRecommendDB() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_DAYRECOMMEND, null, null, null, null, null, null);

        int id = 0;
        while (cursor.moveToNext()) {
            id++;
            String song_id = String.valueOf(id);
            String song_title = cursor.getString(2);
            String song_singer = cursor.getString(3);
            String song_album = cursor.getString(5);
            String time = cursor.getString(10);
            int song_rid = cursor.getInt(1);
            String pic = cursor.getString(12);
            //将一行当中的数据封装到对象中
            MusicBean bean = new MusicBean(song_id, song_title, song_singer, song_album, time, song_rid, pic, true);
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
        musicRv = (RecyclerView) findViewById(R.id.searchNet_music_rv);
        updateTimeTv = (TextView) findViewById(R.id.hotList_updateTime);


        updateTimeTv.setText("更新时间：" + getTodayTime());
    }

    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.format("%02d-%02d", month, day);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
