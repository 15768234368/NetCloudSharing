package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.netcloudsharing.LocalMusicBean;
import com.example.netcloudsharing.Music.MusicSearch;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.MusicUtil;

import static com.example.netcloudsharing.Fragment.MainActivity.binder;
import static com.example.netcloudsharing.service.MusicService.currentPlayPosition;

public class FragmentMusic extends Fragment implements View.OnClickListener {
    private static final String TAG = FragmentMusic.class.getSimpleName();

    FragmentMusic_NewSong fragmentMusic_newSong;
    FragmentMusic_RankingList fragmentMusic_rankingList;
    FragmentMusic_SongList fragmentMusic_songList;

    private Button btnNewSong;
    private Button btnRankingList;
    private Button btnSongList;

    private TextView tvNewSong;
    private TextView tvRankingList;
    private TextView tvSongList;

    private View thisView;
    //三个播放歌曲按钮
    private ImageView nextIv, playIv, lastIv, songImage;
    //歌曲歌手
    private TextView singerTv, songTv;
    private boolean firstOpen = true;
    //搜索歌曲
    private ImageButton ibSearch;
    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        selectTab(0);
    }

    /**
     * 定义控件
     */
    private void initView() {
        btnNewSong = thisView.findViewById(R.id.fragment_music_btnNewSong);
        btnRankingList = thisView.findViewById(R.id.fragment_music_btnRankingList);
        btnSongList = thisView.findViewById(R.id.fragment_music_btnSongList);

        ibSearch = thisView.findViewById(R.id.fragment_music_ibSearch);

        tvNewSong = thisView.findViewById(R.id.fragment_music_tvNewSong);
        tvRankingList = thisView.findViewById(R.id.fragment_music_tvRankingList);
        tvSongList = thisView.findViewById(R.id.fragment_music_tvSongList);
        singerTv = thisView.findViewById(R.id.local_music_bottom_tvSinger);
        songTv = thisView.findViewById(R.id.local_music_bottom_tvSong);

        btnNewSong.setOnClickListener(this);
        btnRankingList.setOnClickListener(this);
        btnSongList.setOnClickListener(this);

        nextIv = thisView.findViewById(R.id.local_music_bottom_ivNext);
        playIv = thisView.findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = thisView.findViewById(R.id.local_music_bottom_ivLast);
        songImage = thisView.findViewById(R.id.local_music_bottom_ivIcon);

        relativeLayout = thisView.findViewById(R.id.local_music_bottomLayout);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        ibSearch.setOnClickListener(this);


        Log.d(TAG, "initView: test");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        this.thisView = view;
        return view;
    }

    @Override
    public void onClick(View v) {
        resetTab();
        switch (v.getId()) {
            case R.id.fragment_music_btnNewSong:
                selectTab(0);
                break;
            case R.id.fragment_music_btnSongList:
                selectTab(1);
                break;
            case R.id.fragment_music_btnRankingList:
                selectTab(2);
                break;
            case R.id.local_music_bottom_ivPlay:
                if (firstOpen) {
                    if (currentPlayPosition == -1) {
                        //如果没有音乐在播放
                        binder.playMusicPosition(null);
                        setMusicBean(binder.getMusicBean());
                        playIv.setImageResource(R.mipmap.icon_pause);
                    } else {
                        binder.playMusicPosition(currentPlayPosition);
                        playIv.setImageResource(R.mipmap.icon_next);
                    }
                    firstOpen = false;
                } else {
                    if (binder.isMusicPlaying()) {
                        //如果音乐正在播放，则暂停
                        binder.pauseMusic();
                        playIv.setImageResource(R.mipmap.icon_play);
                    } else {
                        //如果音乐正在暂停，则播放
                        binder.playMusic();
                        playIv.setImageResource(R.mipmap.icon_pause);
                    }
                }
                setMusicBean(binder.getMusicBean());
                Log.d(TAG, "onClick: ");

                break;
            case R.id.local_music_bottom_ivLast:
                binder.playLastMusic();
                setMusicBean(binder.getMusicBean());
                break;
            case R.id.local_music_bottom_ivNext:
                binder.playNextMusic();
                setMusicBean(binder.getMusicBean());
                break;
            case R.id.fragment_music_ibSearch:
                Intent searchIntent = new Intent(getActivity(), MusicSearch.class);
                startActivity(searchIntent);
                break;

            case R.id.local_music_bottomLayout:
                Intent currentMusicIntent = new Intent(getActivity(), CurrentPlayMusic.class);
                startActivity(currentMusicIntent);
                break;

        }
    }

    public void setMusicBean(LocalMusicBean bean) {
        singerTv.setText(bean.getSinger());
        songTv.setText(bean.getSong());
        MusicUtil.setAlbumImage(songImage, binder.getCurrentSongAlbumPath());
        if (binder.isMusicPlaying()) {
            playIv.setImageResource(R.mipmap.icon_pause);
        } else {
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    private void resetTab() {
        tvNewSong.setBackgroundColor(Color.WHITE);
        tvSongList.setBackgroundColor(Color.WHITE);
        tvRankingList.setBackgroundColor(Color.WHITE);
    }

    private void selectTab(int i) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                tvNewSong.setBackgroundColor(Color.RED);
                if (fragmentMusic_newSong == null) {
                    fragmentMusic_newSong = new FragmentMusic_NewSong();
                    transaction.add(R.id.fragment_music_fl, fragmentMusic_newSong);
                } else {
                    transaction.show(fragmentMusic_newSong);
                }
                break;
            case 1:
                tvSongList.setBackgroundColor(Color.RED);
                if (fragmentMusic_songList == null) {
                    fragmentMusic_songList = new FragmentMusic_SongList();
                    transaction.add(R.id.fragment_music_fl, fragmentMusic_songList);
                } else {
                    transaction.show(fragmentMusic_songList);
                }
                break;
            case 2:
                tvRankingList.setBackgroundColor(Color.RED);
                if (fragmentMusic_rankingList == null) {
                    fragmentMusic_rankingList = new FragmentMusic_RankingList();
                    transaction.add(R.id.fragment_music_fl, fragmentMusic_rankingList);
                } else {
                    transaction.show(fragmentMusic_rankingList);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentMusic_newSong != null) {
            transaction.hide(fragmentMusic_newSong);
        }
        if (fragmentMusic_songList != null) {
            transaction.hide(fragmentMusic_songList);
        }
        if (fragmentMusic_rankingList != null) {
            transaction.hide(fragmentMusic_rankingList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (binder != null) {
            setMusicBean(binder.getMusicBean());
        }
    }
}
