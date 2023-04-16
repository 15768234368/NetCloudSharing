package com.example.netcloudsharing.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.service.MusicService;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentMusic fragmentMusic;
    private FragmentCommunity fragmentCommunity;
    private FragmentMessage fragmentMessage;
    private FragmentMy fragmentMy;


    private ImageButton mImg1;
    private ImageButton mImg2;
    private ImageButton mImg3;
    private ImageButton mImg4;


    public static MusicService.MyBinder binder;
    private myConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initEvent();//初始化事件
        if(binder == null){
            conn = new myConn();
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            Log.d("MainActivity", "1");
        }
        selectTab(0);
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
        mImg2 = findViewById(R.id.ib_tab_community);
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
            case R.id.ib_tab_community:
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
                if (fragmentCommunity == null) {
                    fragmentCommunity = new FragmentCommunity();
                    transaction.add(R.id.fl_content, fragmentCommunity);
                } else {
                    transaction.show(fragmentCommunity);
                }
                break;
            case 2:
                mImg3.setImageResource(R.drawable.select_message);
                if (fragmentMessage == null) {
                    fragmentMessage = new FragmentMessage();
                    transaction.add(R.id.fl_content, fragmentMessage);
                } else {
                    transaction.show(fragmentMessage);
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
    }

    //将Fragment进行隐藏
    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentMusic != null) {
            transaction.hide(fragmentMusic);
        }
        if (fragmentCommunity != null) {
            transaction.hide(fragmentCommunity);
        }
        if (fragmentMessage != null) {
            transaction.hide(fragmentMessage);
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
        SharedPreferences sp = getSharedPreferences("lastMusicPlayPosition", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lastMusicPlayPosition", binder.getCurrentPosition());
        edit.commit();

        super.onDestroy();
    }

}
