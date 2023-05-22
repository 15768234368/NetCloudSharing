package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.ArtistBean;
import com.example.netcloudsharing.LocalMusicAdapter;
import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.User.MySingerAdapter;
import com.example.netcloudsharing.activity.ArtistActivity;
import com.example.netcloudsharing.diary.Permission;

import java.util.ArrayList;
import java.util.List;

public class NewSong_MySinger extends AppCompatActivity {
    List<ArtistBean> mData;
    private RecyclerView artistRv;
    MySingerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song__my_singer);
        initView();
        mData = new ArrayList<>();
        adapter = new MySingerAdapter(this, mData);
        artistRv.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        artistRv.setLayoutManager(layoutManager);

        loadMySingerData();
        //设置每一项的点击事件
        setEventListener();
        Permission permission = new Permission();
        permission.checkPermission(this);
    }
    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(NewSong_MySinger.this, "ok", Toast.LENGTH_SHORT).show();
                ArtistBean artistBean = mData.get(position);
                Intent intent = new Intent(NewSong_MySinger.this, ArtistActivity.class);
                intent.putExtra("artistId", artistBean.getArtistId());
                startActivity(intent);
            }
        });
    }
    private void loadMySingerData() {
        FavourListDBHelper favourListDBHelper = new FavourListDBHelper(this);
        SQLiteDatabase db = favourListDBHelper.getReadableDatabase();
        Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURARTIST, null,null,null,null,null,null);
        while(cursor.moveToNext()){
            final int artistId = cursor.getInt(0);
            final String name = cursor.getString(1);
            final String aartist = cursor.getString(2);
            final int albumNum = cursor.getInt(3);
            final int artistFans = cursor.getInt(4);
            final String country = cursor.getString(6);
            final String info = cursor.getString(8);
            final int musicNum = cursor.getInt(10);
            final int mvNum = cursor.getInt(11);
            final String pic = cursor.getString(12);
            mData.add(new ArtistBean(artistId, name, pic, aartist,albumNum, artistFans, country, info, musicNum,mvNum));
        }
        cursor.close();
        db.close();
        favourListDBHelper.close();
    }

    private void initView() {
        artistRv = (RecyclerView) findViewById(R.id.mySinger_rv);
    }
}
