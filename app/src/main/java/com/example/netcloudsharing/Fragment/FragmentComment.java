package com.example.netcloudsharing.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.CommentAdapter;
import com.example.netcloudsharing.CommentBean;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.SaveMusicCommentFromNetToMCDB;

import java.util.ArrayList;
import java.util.List;

public class FragmentComment extends Fragment {
    private List<CommentBean> hotData, normalData;
    private RecyclerView hotComment_rc, normalComment_rc;
    private View view;
    CommentAdapter hotAdapter, normalAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_comment,container,false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SaveMusicCommentFromNetToMCDB saveMusicCommentFromNetToMCDB = new SaveMusicCommentFromNetToMCDB(getContext());
        saveMusicCommentFromNetToMCDB.saveToDB();
        initView();
        hotData = new ArrayList<>();
        normalData = new ArrayList<>();

        hotAdapter = new CommentAdapter(hotData, getContext());
        normalAdapter = new CommentAdapter(normalData, getContext());
        hotComment_rc.setAdapter(hotAdapter);
        normalComment_rc.setAdapter(normalAdapter);

        LinearLayoutManager hotLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        hotComment_rc.setLayoutManager(hotLayoutManager);
        LinearLayoutManager normalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        normalComment_rc.setLayoutManager(normalLayoutManager);

        loadCommentData();
        hotAdapter.notifyDataSetChanged();
        normalAdapter.notifyDataSetChanged();
    }

    private void loadCommentData() {
        NetMusicInfoDBHelper helper = new NetMusicInfoDBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_COMMENT,null, "comment_type=?", new String[]{"hot"}, null,null,null,null);
        while(cursor.moveToNext()){
            String userImage = cursor.getString(6);
            String userName = cursor.getString(5);
            String msg = cursor.getString(2);
            String time = cursor.getString(3);
            hotData.add(new CommentBean(userImage, userName, msg, time));
        }
        cursor.close();

        cursor = db.query(NetMusicInfoDBHelper.TABLE_NAME_COMMENT,null, "comment_type=?", new String[]{"normal"}, null,null,null,null);
        while(cursor.moveToNext()){
            String userImage = cursor.getString(6);
            String userName = cursor.getString(5);
            String msg = cursor.getString(2);
            String time = cursor.getString(3);
            normalData.add(new CommentBean(userImage, userName, msg, time));
        }
        cursor.close();
    }

    private void initView() {
        hotComment_rc = view.findViewById(R.id.fragment_comment_hot_rv);
        normalComment_rc = view.findViewById(R.id.fragment_comment_normal_rv);


    }
}
