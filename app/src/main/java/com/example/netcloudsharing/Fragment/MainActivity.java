package com.example.netcloudsharing.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.service.MusicService;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent dataIntent = getIntent();
        login_type = dataIntent.getIntExtra("login_type", 0);
        if(login_type == 1){
            uid = dataIntent.getStringExtra("uid"); //不是游客，是用登录进来的
            account = dataIntent.getStringExtra("account");
        }
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
        SharedPreferences sp = getSharedPreferences("lastMusicPlayPosition", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lastMusicPlayPosition", binder.getCurrentPosition());
        edit.commit();

        super.onDestroy();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        return super.onKeyDown(keyCode, event);
    }

}
