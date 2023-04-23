package com.example.netcloudsharing.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.LocalMusicAdapter;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.MusicSetAdapter;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.SaveMusicSetFromNetToMSDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;
import static com.example.netcloudsharing.MusicSetAdapter.getBitmapFromUrl;

public class MusicSetActivity extends AppCompatActivity {
    List<MusicBean> mData;
    String photoSetUrl;
    String musicSetName;
    int bangId;
    private LocalMusicAdapter adapter;
    private RecyclerView musicRv;
    private ImageView musicSetImage_iv;
    private TextView musicSetName_tv;
    private ImageView musicSetBack_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_set);
        initView();
        Intent intent = getIntent();
        photoSetUrl = intent.getStringExtra("photoSet");
        musicSetName = intent.getStringExtra("musicSetName");
        bangId = intent.getIntExtra("bangId", 0);

        SaveMusicSetFromNetToMSDB saveMusicSetFromNetToMSDB = new SaveMusicSetFromNetToMSDB(this, bangId, musicSetName);
        saveMusicSetFromNetToMSDB.saveToDB();
        mData = new ArrayList<>();

        //getActivity可能有问题！！！
        adapter = new LocalMusicAdapter(this, mData);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源

        loadMusicSetData();
        adapter.notifyDataSetChanged();
        //设置每一项的点击事件
        setEventListener();

        loadView();
    }

    private void loadView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromUrl(photoSetUrl, new MusicSetAdapter.BitmapListener() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        if (bitmap != null) {
                            // 在这里使用Bitmap对象更新UI组件
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicSetImage_iv.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        musicSetName_tv.setText(musicSetName);
                    }
                });
            }
        }).start();
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

    private void loadMusicSetData() {
        new Runnable() {

            @Override
            public void run() {
                loadMusicSetDB();
            }
        }.run();
    }

    private void loadMusicSetDB() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_MUSICSET, null, "bangId=?", new String[]{String.valueOf(bangId)}, null, null, null, null);
        int id = 0;
        while (cursor.moveToNext()) {
            id++;
            String song_id = String.valueOf(id);
            String song_title = cursor.getString(3);
            String song_singer = cursor.getString(4);
            String song_album = cursor.getString(6);
            String time = cursor.getString(11);
            int song_rid = cursor.getInt(1);
            String pic = cursor.getString(13);
            //将一行当中的数据封装到对象中
            MusicBean bean = new MusicBean(song_id, song_title, song_singer, song_album, time, song_rid, pic, true);
            mData.add(bean);
        }
        cursor.close();
        db.close();
        helper.close();
    }

    private void initView() {
        musicRv = (RecyclerView) findViewById(R.id.searchNet_music_rv);
        musicSetImage_iv = (ImageView) findViewById(R.id.music_set_image);
        musicSetName_tv = (TextView) findViewById(R.id.music_set_name);

        musicSetBack_iv = (ImageView) findViewById(R.id.music_set_back);
        musicSetBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
