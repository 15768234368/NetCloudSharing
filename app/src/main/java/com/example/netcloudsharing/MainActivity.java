package com.example.netcloudsharing;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.Fragment.FragmentAllSinger;
import com.example.netcloudsharing.Fragment.FragmentComment;
import com.example.netcloudsharing.Fragment.FragmentMusic;
import com.example.netcloudsharing.Fragment.FragmentMy;
import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.adapter.PlayListAdapter;
import com.example.netcloudsharing.helper.PlayListHelper;
import com.example.netcloudsharing.service.MusicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private FragmentMusic fragmentMusic;
    private FragmentAllSinger fragmentAllSinger;
    private FragmentComment fragmentComment;
    private FragmentMy fragmentMy;


    private ImageButton mImg1;
    private ImageButton mImg2;
    private ImageButton mImg3;
    private ImageButton mImg4;


    public static MusicService.MyBinder binder;
    private myConn conn;
    /**
     * value = 0 ： 游客
     * value = 1 : 用户
     */
    public static int login_type;

    public static String uid = null;
    public static String account = null;
    private LocalMusicAdapter adapter;
    // 在MainActivity中注册广播接收器
    private BroadcastReceiver musicPlayFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadDataToDrawerLayout();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initEvent();//初始化事件
        if (binder == null) {
            conn = new myConn();
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            Log.d("MainActivity", "1");
        }
        selectTab(0);
        initDrawerLayout();
        loadDataToDrawerLayout();
    }

    private void loadDataToDrawerLayout() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<MusicBean> dataList = new ArrayList<>();
        // 在适当的地方创建数据库实例
        PlayListHelper helper = new PlayListHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        // 执行查询
        Cursor cursor = db.query(
                PlayListHelper.TABLE_NAME,  // 表名
                null,                          // 要查询的列
                null,                           // 查询条件
                null,                       // 查询条件参数
                null,                                // 不进行分组
                null,                                // 不进行过滤
                null                                 // 不进行排序
        );

        // 遍历结果集
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_ID));
            String song = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_SONG));
            String singer = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_SINGER));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_ALBUM));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_DURATION));
            String rid = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_RID));
            String audio_id = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_AUDIO_ID));
            String pic = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_PIC));
            String real_url = cursor.getString(cursor.getColumnIndexOrThrow(PlayListHelper.COLUMN_REAL_URL));
            dataList.add(new MusicBean(String.valueOf(id), song, singer, album, duration, real_url, pic, rid, audio_id));
        }

        // 关闭游标和数据库连接
        cursor.close();
        db.close();
        // 添加你的数据到 dataList 中
        adapter = new LocalMusicAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                final MusicBean musicBean = dataList.get(position);
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
                // 在适当的地方发送广播消息
                Intent intent = new Intent("com.example.musicplayer.UPDATE_MUSICDATA");
                sendBroadcast(intent);
            }
        });
    }


    private void initDrawerLayout() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mImg1.setOnClickListener(this);
        mImg2.setOnClickListener(this);
        mImg3.setOnClickListener(this);
        mImg4.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //初始化ImageButton布局文件
        mImg1 = findViewById(R.id.ib_tab_home);
        mImg2 = findViewById(R.id.ib_tab_allSinger);
        mImg3 = findViewById(R.id.ib_tab_message);
        mImg4 = findViewById(R.id.ib_tab_my);

    }

    //处理点击事件
    @Override
    public void onClick(View v) {
        resetImages();
        switch (v.getId()) {
            case R.id.ib_tab_home:
                selectTab(0);
                break;
            case R.id.ib_tab_allSinger:
                selectTab(1);
                break;
            case R.id.ib_tab_message:

                selectTab(2);
                break;
            case R.id.ib_tab_my:
                selectTab(3);
                break;
        }
    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏好所有的Fragment
        hideFragment(transaction);
        switch (i) {
            //将选中的Tab实例化并关联起来
            case 0:
                mImg1.setImageResource(R.drawable.select_home);
                if (fragmentMusic == null) {
                    fragmentMusic = new FragmentMusic();
                    transaction.add(R.id.fl_content, fragmentMusic);
                } else {
                    transaction.show(fragmentMusic);
                }
                break;
            case 1:
                mImg2.setImageResource(R.drawable.select_community);
                if (fragmentAllSinger == null) {
                    fragmentAllSinger = new FragmentAllSinger();
                    transaction.add(R.id.fl_content, fragmentAllSinger);
                } else {
                    transaction.show(fragmentAllSinger);
                }
                break;
            case 2:
                mImg3.setImageResource(R.drawable.select_message);
                if (fragmentComment == null) {
                    fragmentComment = new FragmentComment();
                    transaction.add(R.id.fl_content, fragmentComment);
                } else {
                    transaction.show(fragmentComment);
                }
                break;
            case 3:
                mImg4.setImageResource(R.drawable.select_my);
                if (fragmentMy == null) {
                    fragmentMy = new FragmentMy();
                    transaction.add(R.id.fl_content, fragmentMy);
                } else {
                    transaction.show(fragmentMy);
                }
                break;
        }
        //提交事务
        transaction.commit();
        manager.executePendingTransactions();
        if (i == 0 && fragmentMusic != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View view = fragmentMusic.getView();
                    if (view != null) {
                        view.findViewById(R.id.fragment_music_ivPlayList).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                                drawer.openDrawer(GravityCompat.START);
                            }
                        });
                    }
                }
            }, 100); // 在此处设置适当的延迟时间，例如100ms
        }
    }

    //将Fragment进行隐藏
    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentMusic != null) {
            transaction.hide(fragmentMusic);
        }
        if (fragmentAllSinger != null) {
            transaction.hide(fragmentAllSinger);
        }
        if (fragmentComment != null) {
            transaction.hide(fragmentComment);
        }
        if (fragmentMy != null) {
            transaction.hide(fragmentMy);
        }

    }

    //重置导航图片
    private void resetImages() {
        mImg1.setImageResource(R.drawable.unselect_home);
        mImg2.setImageResource(R.drawable.unselect_community);
        mImg3.setImageResource(R.drawable.unselect_message);
        mImg4.setImageResource(R.drawable.unselect_my);
    }


    private class myConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MyBinder) service;
            Log.d("MainActivity", "4");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序",
                            Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            outState.remove(FRAGMENTS_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.musicplayer.PLAY_FINISHED");
        registerReceiver(musicPlayFinishedReceiver, filter);
        loadDataToDrawerLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册广播接收器
        unregisterReceiver(musicPlayFinishedReceiver);
    }
}
