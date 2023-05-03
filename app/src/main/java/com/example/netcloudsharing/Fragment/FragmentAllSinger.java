package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.netcloudsharing.ArtistBean;
import com.example.netcloudsharing.ArtistListAdapter;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.activity.ArtistActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentAllSinger extends Fragment {
    private static final String TAG = "FragmentAllSinger";
    private View view;
    private TextView updateTimeTv;
    private List<ArtistBean> mArtistData;
    private ArtistListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_allsinger, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArtistData = new ArrayList<>();
        initArtistList();
        loadArtistData();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.artistList_rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArtistListAdapter(getContext(), mArtistData);
        recyclerView.setAdapter(adapter);
        setEventListener();
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new ArtistListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                ArtistBean artistBean = mArtistData.get(position);
                Intent intent = new Intent(getContext(), ArtistActivity.class);
                intent.putExtra("artistId", artistBean.getArtistId());
                startActivity(intent);
            }

        });
    }

    private void loadArtistData() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_ARTISTLIST, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String pic = cursor.getString(12);
            mArtistData.add(new ArtistBean(id, name, pic));
        }
        cursor.close();
        db.close();
    }

    private void initArtistList() {
        updateTimeTv = (TextView) view.findViewById(R.id.artistList_updateTime);
        updateTimeTv.setText("更新时间：" + getTodayTime());

    }

    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.format("%02d-%02d", month, day);
    }
}
