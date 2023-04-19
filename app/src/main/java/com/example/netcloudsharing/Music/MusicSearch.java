package com.example.netcloudsharing.Music;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.netcloudsharing.Fragment.Fragment_SearchList;
import com.example.netcloudsharing.MusicBean;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.MusicUtil;
import com.example.netcloudsharing.tool.SaveMusicFromNetToNMDB;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;

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

    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;

    private Fragment_SearchList fragment_searchList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_search);
        init();
    }

    private void init() {
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
        mSearchView = findViewById(R.id.activity_music_search_svSearchContext);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                query = "周杰伦";
                displaySearchedMusic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void displaySearchedMusic(final String query) {

        SaveMusicFromNetToNMDB saveMusicFromNetToNMDB = new SaveMusicFromNetToNMDB(getApplicationContext());
        saveMusicFromNetToNMDB.saveToDB(query);

        Runnable saveMusicInfoRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (fragment_searchList == null) {
                    fragment_searchList = new Fragment_SearchList();
                    transaction.add(R.id.fragment_music_search_fl, fragment_searchList);
                } else {
                    manager.beginTransaction().remove(fragment_searchList).commit();
                    fragment_searchList = new Fragment_SearchList();
                    transaction.add(R.id.fragment_music_search_fl, fragment_searchList);
                }
                fragment_searchList.setSearchKey(query);
                fragment_searchList.setPlayBottomInfo(lastIv, playIv, nextIv, songImage, singerTv, songTv);
                transaction.commit();
            }
        };

        mHandler.postDelayed(saveMusicInfoRunnable, 3000); // 延时3秒执行存储音乐信息的操作
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

    @Override
    protected void onResume() {
        super.onResume();
        setMusicBean(binder.getCurrentNetBean());
    }
}
