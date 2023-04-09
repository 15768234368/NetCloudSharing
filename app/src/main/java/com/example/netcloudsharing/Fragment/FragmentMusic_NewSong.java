package com.example.netcloudsharing.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netcloudsharing.R;

public class FragmentMusic_NewSong extends Fragment implements View.OnClickListener {
    private TextView tvLocalMusicCount;
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

        thisView.findViewById(R.id.fragment_music_newSong_llLocalSong).setOnClickListener(this);
        thisView.findViewById(R.id.fragment_music_newsong_ibDownloadMusic).setOnClickListener(this);
        getMusicCount();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_music_newSong_llLocalSong:
                Intent intent = new Intent(getContext(), ActivityMusicHome.class);
                startActivity(intent);
                break;
//            case R.id.fragment_music_newsong_ibDownloadMusic:
//                Intent intent1 = new Intent(getContext(), MusicDownload.class);
//                startActivity(intent1);
//                break;
        }
    }

    /**
     * 获取本地音乐数量，并显示
     */
    public void getMusicCount(){
        SharedPreferences sp = getActivity().getSharedPreferences("music_count", Context.MODE_PRIVATE);
        int count = sp.getInt("count",0);
        tvLocalMusicCount.setText("("+count+")");
    }
}
