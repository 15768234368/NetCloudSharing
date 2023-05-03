package com.example.netcloudsharing.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netcloudsharing.Music.DownloadedMusicActivity;
import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.R;

public class FragmentMusic_NewSong extends Fragment implements View.OnClickListener {
    private TextView tvLocalMusicCount, tvRecentPlayedCount, tvMySingerCount, tvMyMusicSetCount;
    private View thisView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_newsong, container, false);
        this.thisView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        tvLocalMusicCount = thisView.findViewById(R.id.fragment_music_tvLocalMusicCount);
        tvRecentPlayedCount = thisView.findViewById(R.id.fragment_music_tvRecentPlayedCount);
        tvMySingerCount = thisView.findViewById(R.id.fragment_music_tvMySingerCount);
        tvMyMusicSetCount = thisView.findViewById(R.id.fragment_music_tvMyMusicSetCount);
        thisView.findViewById(R.id.fragment_music_newSong_llLocalSong).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newsong_ibDownloadMusic).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newSong_llRecentlyPlayed).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newsong_ibRankingList).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newsong_ibDayRecommend).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newsong_ibFavourMusic).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newSong_llMyMusicList).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newSong_llMySinger).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_music_newSong_llLocalSong:
                Intent intent_localSong = new Intent(getContext(), NewSong_LocalMusicList.class);
                startActivity(intent_localSong);
                break;
            case R.id.fragment_music_newsong_ibDownloadMusic:
                Intent intent_download = new Intent(getContext(), DownloadedMusicActivity.class);
                startActivity(intent_download);
                break;
            case R.id.fragment_music_newSong_llRecentlyPlayed:
                Intent recentPlayedIntent = new Intent(getContext(), NewSong_RecentPlay.class);
                startActivity(recentPlayedIntent);
                break;
            case R.id.fragment_music_newsong_ibRankingList:
                Intent rankingListIntent = new Intent(getContext(), NewSong_RankingList.class);
                startActivity(rankingListIntent);
                break;
            case R.id.fragment_music_newsong_ibDayRecommend:
                Intent dayRecommendIntent = new Intent(getContext(), NewSong_DayRecommend.class);
                startActivity(dayRecommendIntent);
                break;
            case R.id.fragment_music_newsong_ibFavourMusic:
                Intent favourMusicIntent = new Intent(getContext(), NewSong_FavourMusic.class);
                startActivity(favourMusicIntent);
                break;
            case R.id.fragment_music_newSong_llMyMusicList:
                Intent favourMusicListIntent = new Intent(getContext(), NewSong_MyMusicList.class);
                startActivity(favourMusicListIntent);
                break;
            case R.id.fragment_music_newSong_llMySinger:
                Intent mySingerIntent = new Intent(getContext(), NewSong_MySinger.class);
                startActivity(mySingerIntent);
                break;
        }
    }

    /**
     * 获取本地音乐数量，并显示
     */
    public void getLocalMusicCount() {
        SharedPreferences sp = getActivity().getSharedPreferences("music_local_count", Context.MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        tvLocalMusicCount.setText("(" + count + ")");
    }

    public void getRecentPlayCount() {
        SharedPreferences sp = getActivity().getSharedPreferences("music_recentPlayed_count", Context.MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        tvRecentPlayedCount.setText("(" + count + ")");
    }

    public void getMySingerCount() {
        FavourListDBHelper helper = new FavourListDBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURARTIST, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        helper.close();
        tvMySingerCount.setText("(" + count + ")");
    }
    public void getMyMusicSetCount(){
        FavourListDBHelper helper = new FavourListDBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(FavourListDBHelper.TABLE_NAME_FAVOURMUSICSET, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        helper.close();
        tvMyMusicSetCount.setText("(" + count + ")");
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocalMusicCount();
        getRecentPlayCount();
        getMySingerCount();
        getMyMusicSetCount();
    }
}
