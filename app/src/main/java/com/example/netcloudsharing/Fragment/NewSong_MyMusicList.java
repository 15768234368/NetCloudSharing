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

import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.MusicSetAdapter;
import com.example.netcloudsharing.MusicSetBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.activity.MusicSetActivity;

import java.util.ArrayList;
import java.util.List;

public class NewSong_MyMusicList extends AppCompatActivity {
    List<MusicSetBean> mData;
    private RecyclerView musicSetRv;
    private MusicSetAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song__my_music_list);

        mData = new ArrayList<>();
        loadMusicSet();

        musicSetRv = (RecyclerView) findViewById(R.id.myMusicSet_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicSetRv.setLayoutManager(layoutManager);
        adapter = new MusicSetAdapter(this, mData);
        musicSetRv.setAdapter(adapter);

        setEventListener();
    }
    private void setEventListener() {
        adapter.setOnItemClickListener(new MusicSetAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                MusicSetBean setBean = mData.get(position);
                Intent intent = new Intent(getApplicationContext(), MusicSetActivity.class);
                intent.putExtra("photoSet", setBean.getPhotoUrl());
                intent.putExtra("musicSetName", setBean.getSetName());
                intent.putExtra("bangId", setBean.getBangId());
                startActivity(intent);
            }

        });
    }
    private void loadMusicSet() {
        FavourListDBHelper helper = new FavourListDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURMUSICSET, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            int bangId = cursor.getInt(0);
            String musicSetName = cursor.getString(1);
            String musicSetPhoto = cursor.getString(2);
            mData.add(new MusicSetBean(musicSetPhoto, musicSetName, bangId));
        }

        cursor.close();
        db.close();
        helper.close();
    }
}
