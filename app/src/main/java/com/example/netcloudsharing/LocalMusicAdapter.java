package com.example.netcloudsharing;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Music.DownloadedMusicHelper;
import com.example.netcloudsharing.service.MusicDownloadService;
import com.example.netcloudsharing.tool.MusicUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {
    private static final String TAG = "LocalMusicAdapter";
    Context context;
    List<MusicBean> mDatas;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public LocalMusicAdapter(Context context, List<MusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_music, parent, false);
        return new LocalMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, final int position) {
        //绑定viewholder，并且对每一个控件来进行赋值展示
        final MusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());
        holder.more_menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                String[] mode = {"下载", "详情", "取消"};
                ListView listView = new ListView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                listView.setLayoutParams(params);
                listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mode));

                dialog = new BottomSheetDialog(context);
                dialog.setContentView(listView);
                dialog.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positionInList, long id) {
                        switch (positionInList) {
                            case 0:
                                if (context.getClass().getSimpleName().equals("NewSong_LocalMusicList")) {
                                    Toast.makeText(context, "当前音乐已存在！", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                    break;
                                }
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);

                                final String filename = String.format("%04d-%02d-%02d_%02d-%02d-%02d", year, month, day, hour, minute, second);
                                if (musicBean.getPath() == null) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            musicBean.setPath(MusicUtil.getUrlByNetMusicRid(musicBean.getRid()));
                                            MusicDownloadService.startActionDownLoad(context, musicBean.getPath(), filename);
                                            saveToDownloadedDB(musicBean);
                                        }
                                    }).start();
                                } else {
                                    MusicDownloadService.startActionDownLoad(context, musicBean.getPath(), filename);
                                    long insertRow = saveToDownloadedDB(musicBean);
                                    if (insertRow != -1) {
                                        Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                dialog.dismiss();
                                break;
                            case 1:
                                break;
                            case 2:
                                dialog.cancel();
                        }
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v, position);
            }
        });
    }

    public long saveToDownloadedDB(MusicBean musicBean) {
        long id = -1;
        DownloadedMusicHelper musicHelper = new DownloadedMusicHelper(context);
        SQLiteDatabase db = musicHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            // 执行数据库操作
            values.put(DownloadedMusicHelper.SONG_ID, musicBean.getId());
            values.put(DownloadedMusicHelper.SONG_TITLE, musicBean.getSong());
            values.put(DownloadedMusicHelper.SONG_SINGER, musicBean.getSinger());
            values.put(DownloadedMusicHelper.SONG_ALBUM, musicBean.getAlbum());
            values.put(DownloadedMusicHelper.SONG_DURATION, musicBean.getDuration());
            values.put(DownloadedMusicHelper.SONG_PATH, musicBean.getPath());
            values.put(DownloadedMusicHelper.PIC, musicBean.getPic());
            Log.d(TAG, "saveMusicInfoToDB: song_title is " + musicBean.getSong());
            Log.d(TAG, "saveMusicInfoToDB: song_singer is " + musicBean.getSinger());
            Log.d(TAG, "saveMusicInfoToDB: song_album is " + musicBean.getAlbum());
            Log.d(TAG, "saveMusicInfoToDB: song_path is " + musicBean.getPath());
            Log.d(TAG, "saveMusicInfoToDB: song_duration is " + musicBean.getDuration());
            Log.d(TAG, "saveMusicInfoToDB: Pic is " + musicBean.getPic());
            id = db.insert(DownloadedMusicHelper.TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error executing SQL statement: " + e.getMessage());
        }
        Log.d(TAG, "saveMusicInfoToDB: insertRow is " + id);
        db.close();
        return id;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder {
        TextView idTv, songTv, singerTv, albumTv, timeTv;
        ImageView more_menuIv;

        public LocalMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            songTv = itemView.findViewById(R.id.item_local_music_song);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_durtion);
            more_menuIv = itemView.findViewById(R.id.iv_more_vert_black);
        }
    }

}
