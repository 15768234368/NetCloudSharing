package com.example.netcloudsharing.Music;

import static com.example.netcloudsharing.MainActivity.binder;
import static com.example.netcloudsharing.tool.MusicUtil.DurationToString;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.MusicUtil;
import com.example.netcloudsharing.task.SearchSongTask;
import com.example.netcloudsharing.task.SearchUrlTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicSearch extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler = new Handler();
    private static final String TAG = "MusicSearch";
    //三个播放歌曲按钮
    private ImageView nextIv, playIv, lastIv, album, songImage;
    private ImageButton backIb;
    //歌曲歌手
    private TextView singerTv, songTv;
    private SearchView mSearchView;
    private String path;
    private List<MusicBean> mData;
    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;

    private RecyclerView recyclerView;
    private LocalMusicAdapter adapter;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_search);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        mData = new ArrayList<>();
        init();
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
        initData();
    }

    private void initData() {
        mSearchView.setQuery(key, true);
    }

    private void init() {
        recyclerView = findViewById(R.id.musicSearch_rvList);
        adapter = new LocalMusicAdapter(this, mData);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        mSearchView = findViewById(R.id.activity_music_search_svSearchContext);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                displaySearchedMusic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        nextIv = findViewById(R.id.local_music_bottom_ivNext);
        playIv = findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = findViewById(R.id.local_music_bottom_ivLast);

        singerTv = findViewById(R.id.local_music_bottom_tvSinger);
        songTv = findViewById(R.id.local_music_bottom_tvSong);

        songImage = findViewById(R.id.local_music_bottom_ivIcon);

        relativeLayout = findViewById(R.id.local_music_bottomLayout);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);

        relativeLayout.setOnClickListener(this);


        findViewById(R.id.fragment_music_ibBackToMusicHome).setOnClickListener(this);

    }

    private void displaySearchedMusic(final String query) {
        mData.clear();
        SearchSongTask searchSongTask = new SearchSongTask(new SearchSongTask.SearchSongListener() {
            @Override
            public void onSearchComplete(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray listsArray = dataObject.getJSONArray("lists");
                    int listsCount = listsArray.length();
                    for (int i = 0; i < listsCount; i++) {
                        JSONObject listItem = listsArray.getJSONObject(i);
                        // 对列表项进行解析
                        MusicBean bean = new MusicBean();
                        bean.setId(String.valueOf(i + 1));
                        bean.setSong(listItem.optString("SongName"));
                        bean.setSinger(listItem.optString("SingerName"));
                        bean.setAlbum(listItem.optString("AlbumName"));
                        bean.setDuration(DurationToString(listItem.optString("Duration")));
                        bean.setRid(listItem.optString("EMixSongID"));
                        bean.setAudio_id(listItem.optString("Audioid"));
                        SearchUrlTask searchUrlTask = new SearchUrlTask(new SearchUrlTask.SearchUrlListener() {
                            @Override
                            public void onSearchComplete(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    String realUrl = dataObject.getString("play_url");
                                    String pic = dataObject.getString("img");
                                    String audioId = dataObject.getString("audio_id");
                                    MusicBean bean = searchByAudio_id(audioId);
                                    if(bean != null){
                                        bean.setPath(realUrl);
                                        bean.setPic(pic);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onSearchError() {
                                Log.d(TAG, "onSearchError: ");
                            }
                        });
                        searchUrlTask.execute(bean.getRid());
                        mData.add(bean);
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSearchError() {
                Log.d(TAG, "onSearchError: ");
            }
        });
        searchSongTask.execute(query);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_music_ibBackToMusicHome:
                finish();
                break;
            case R.id.local_music_bottom_ivPlay:
                if (binder.isMusicPlaying()) {
                    binder.pauseMusic();
                    setMusicBean(binder.getCurrentNetBean());
                } else {
                    binder.playMusic();
                    setMusicBean(binder.getCurrentNetBean());
                }
        }
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


    public MusicBean searchByAudio_id(String id){
        if(mData != null){
            for(int i = 0; i < mData.size(); ++i){
                if(mData.get(i).getAudio_id().equals(id))
                    return mData.get(i);
            }
        }
        return null;
    }


}
