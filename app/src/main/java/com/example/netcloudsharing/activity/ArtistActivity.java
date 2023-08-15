package com.example.netcloudsharing.activity;

import android.content.ContentValues;
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

import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.adapter.MusicSetAdapter;
import com.example.netcloudsharing.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.netcloudsharing.MainActivity.binder;
import static com.example.netcloudsharing.adapter.MusicSetAdapter.getBitmapFromUrl;

public class ArtistActivity extends AppCompatActivity {
    List<MusicBean> mData;
    int artistId;
    private LocalMusicAdapter adapter;
    private RecyclerView musicRv;
    private ImageView backIv, artistImage, artistFavourIv;
    private TextView artistNameTv, artistFansTv, musicNumTv, albumNumTv, mvNumTv;
    private TextView aartistTv, countryIv, artistInfoTv, clickAllInfoTv;
    boolean isFavourArtist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        initView();
        Intent intent = getIntent();
        artistId = intent.getIntExtra("artistId", 0);
        mData = new ArrayList<>();
        adapter = new LocalMusicAdapter(this, mData);
        musicRv.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);

        loadArtistMusicData();
        adapter.notifyDataSetChanged();

        setEventListener();
        setIsFavour();
        loadView();
    }

    private void initView() {
        musicRv = (RecyclerView) findViewById(R.id.artist_homePage_rv);
        backIv = (ImageView) findViewById(R.id.artist_homePage_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //显示头像
        artistImage = (ImageView) findViewById(R.id.artist_homePage_image);
        //显示其他信息
        artistNameTv = (TextView) findViewById(R.id.artist_homePage_name);
        artistFansTv = (TextView) findViewById(R.id.artist_homePage_artistFans);
        musicNumTv = (TextView) findViewById(R.id.artist_homePage_musicNum);
        albumNumTv = (TextView) findViewById(R.id.artist_homePage_albumNum);
        mvNumTv = (TextView) findViewById(R.id.artist_homePage_mvNum);
        aartistTv = (TextView) findViewById(R.id.artist_homePage_aartist);
        countryIv = (TextView) findViewById(R.id.artist_homePage_country);
        artistInfoTv = (TextView) findViewById(R.id.artist_homePage_info);

        clickAllInfoTv = (TextView) findViewById(R.id.artist_homePage_allInfo);
        artistFavourIv = (ImageView) findViewById(R.id.artist_homePage_favour);
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

        artistFavourIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavourListDBHelper helper_favour = new FavourListDBHelper(getApplicationContext());
                SQLiteDatabase db_favour = helper_favour.getWritableDatabase();
                if (isFavourArtist) {
                    db_favour.delete(FavourListDBHelper.TABLE_NAME_FAVOURARTIST, "artistId=?", new String[]{String.valueOf(artistId)});
                    artistFavourIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                } else {
                    NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(getApplicationContext());
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_ARTISTLIST, null, "artistId=?", new String[]{String.valueOf(artistId)}, null, null, null, null);
                    cursor.moveToNext();
                    ContentValues values = new ContentValues();
                    values.put("artistId", artistId);
                    final String name = cursor.getString(1);
                    values.put("artist", name);
                    final String aartist = cursor.getString(2);
                    values.put("aartist", aartist);
                    final int albumNum = cursor.getInt(3);
                    values.put("albumNum", albumNum);
                    final int artistFans = cursor.getInt(4);
                    values.put("artistFans", artistFans);
                    final String birthplace = cursor.getString(5);
                    values.put("birthplace", birthplace);
                    final String country = cursor.getString(6);
                    values.put("country", country);
                    final String gener = cursor.getString(7);
                    values.put("gener",gener);
                    final String info = cursor.getString(8);
                    values.put("info",info);
                    final String language = cursor.getString(9);
                    values.put("language",language);
                    final int musicNum = cursor.getInt(10);
                    values.put("musicNum",musicNum);
                    final int mvNum = cursor.getInt(11);
                    values.put("mvNum", mvNum);
                    final String pic = cursor.getString(12);
                    values.put("pic", pic);
                    db_favour.insert(FavourListDBHelper.TABLE_NAME_FAVOURARTIST, null, values);
                    cursor.close();
                    db.close();
                    isFavourArtist = true;
                    artistFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                db_favour.close();
                helper_favour.close();
            }
        });
    }

    private void loadArtistMusicData() {
        new Runnable() {

            @Override
            public void run() {
                loadArtistMusicDB();
            }
        }.run();
    }

    private void loadArtistMusicDB() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_NET, null, "artistId=?", new String[]{String.valueOf(artistId)}, null, null, null, null);
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

    private void loadView() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_ARTISTLIST, null, "artistId=?", new String[]{String.valueOf(artistId)}, null, null, null, null);
        cursor.moveToNext();
        final String name = cursor.getString(1);
        final String aartist = cursor.getString(2);
        final int albumNum = cursor.getInt(3);
        final int artistFans = cursor.getInt(4);
        final String country = cursor.getString(6);
        final String info = cursor.getString(8);
        final int musicNum = cursor.getInt(10);
        final int mvNum = cursor.getInt(11);
        final String pic = cursor.getString(12);
        cursor.close();
        db.close();


        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromUrl(pic, new MusicSetAdapter.BitmapListener() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        if (bitmap != null) {
                            // 在这里使用Bitmap对象更新UI组件
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    artistImage.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        artistNameTv.setText(name);
                        artistFansTv.setText("粉丝：" + artistFans);
                        musicNumTv.setText("单曲：" + musicNum);
                        albumNumTv.setText("专辑：" + albumNum);
                        mvNumTv.setText("MV：" + mvNum);
                        aartistTv.setText("英文名：" + aartist);
                        countryIv.setText("国籍：" + country);
                        artistInfoTv.setText("个人简介：" + info);
                        if (isFavourArtist)
                            artistFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                });
            }
        }).start();
    }

    private void setIsFavour() {
        FavourListDBHelper helper_favour = new FavourListDBHelper(getApplicationContext());
        SQLiteDatabase db_favour = helper_favour.getReadableDatabase();
        Cursor cursor = db_favour.query(FavourListDBHelper.TABLE_NAME_FAVOURARTIST, null, "artistId=?", new String[]{String.valueOf(artistId)}, null, null, null, null);
        isFavourArtist = cursor.getCount() == 1;
        cursor.close();
        db_favour.close();
        helper_favour.close();
    }

    @Override
    protected void onResume() {
        if (isFavourArtist)
            artistFavourIv.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            artistFavourIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        super.onResume();
    }
}
