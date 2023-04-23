package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.netcloudsharing.MusicSetAdapter;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.User.MusicSetBean;
import com.example.netcloudsharing.activity.MusicSetActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentMusic_SongList extends Fragment {
    private static final String TAG = "FragmentMusic_SongList";
    private View view;
    private TextView updateTimeTv;
    private String photoSet[] = {"https://img3.kuwo.cn/star/upload/5/8/1682179821.png", "https://img3.kuwo.cn/star/upload/7/9/1682179826.png",
            "https://img3.kuwo.cn/star/upload/0/4/1682179818.png", "https://img3.kuwo.cn/star/upload/2/4/1682179815.png", "https://img3.kuwo.cn/star/upload/4/3/1682179803.png",
            "https://img3.kuwo.cn/star/upload/5/1/1682179823.png", "https://img3.kuwo.cn/star/upload/9/8/1682179813.png", "https://img3.kuwo.cn/star/upload/8/3/1682179810.png",
            "https://img3.kuwo.cn/star/upload/4/4/1682179806.png", "https://img3.kuwo.cn/star/upload/2/5/1682181750.png", "https://img3.kuwo.cn/star/upload/3/6/1682181745.png",
            "https://img3.kuwo.cn/star/upload/4/6/1682181720.png", "https://img3.kuwo.cn/star/upload/0/6/1682181671.png", "https://img3.kuwo.cn/star/upload/3/9/1682181641.png",
            "https://img3.kuwo.cn/star/upload/3/6/1682181717.png", "https://img3.kuwo.cn/star/upload/5/6/1682181734.png", "https://img3.kuwo.cn/star/upload/0/5/1682181738.png"};
    private String musicSetName[] = {"春季温柔", "车载歌曲", "跑步健身", "宝宝哄睡", "睡前放松", "熬夜修仙", "Vlog必备", "KTV点唱", "通勤路上",
            "会员热歌", "会员新歌", "音乐热评", "经典怀旧", "流行趋势", "音乐综艺", "AGC新歌", "流行铃声"};
    private int bandId[] = {279, 328, 297, 295, 283, 282, 264, 255, 281, 331, 330, 284, 26, 187, 154, 290, 292};
    private List<MusicSetBean> mSetData;
    private MusicSetAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_music_songlist, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetData = new ArrayList<>();
        initMusicSet();

        setEventListener();
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new MusicSetAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                MusicSetBean setBean = mSetData.get(position);
                Intent intent = new Intent(getContext(), MusicSetActivity.class);
                intent.putExtra("photoSet", setBean.getPhotoUrl());
                intent.putExtra("musicSetName", setBean.getSetName());
                intent.putExtra("bangId", setBean.getBangId());
                startActivity(intent);
            }

        });
    }

    private void initMusicSet() {
        for (int i = 0; i < photoSet.length; ++i) {
            MusicSetBean setBean = new MusicSetBean(photoSet[i], musicSetName[i], bandId[i]);
            mSetData.add(setBean);
        }
        updateTimeTv = (TextView) view.findViewById(R.id.musicSet_updateTime);
        updateTimeTv.setText("更新时间：" + getTodayTime());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.musicSet_rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MusicSetAdapter(getContext(), mSetData);
        recyclerView.setAdapter(adapter);

    }

    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.format("%02d-%02d", month, day);
    }
}
