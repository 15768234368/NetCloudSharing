package com.example.netcloudsharing;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.Music.FavourListDBHelper;
import com.example.netcloudsharing.Music.LocalMusicDBHelper;
import com.example.netcloudsharing.Music.NetMusicInfoDBHelper;
import com.example.netcloudsharing.activity.LoginActivity;
import com.example.netcloudsharing.activity.RegisterActivity;
import com.example.netcloudsharing.tool.BaseTool;

public class NetStart extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_start);
        Button btnLogin = BaseTool.openActivity(this,(Button)findViewById(R.id.btn_login),LoginActivity.class);
        Button btnRegister = BaseTool.openActivity(this,(Button)findViewById(R.id.btn_register), RegisterActivity.class);
        NetMusicInfoDBHelper netMusicInfoDBHelper = new NetMusicInfoDBHelper(NetStart.this);
        SQLiteDatabase db = netMusicInfoDBHelper.getWritableDatabase();
        db.close();
        netMusicInfoDBHelper.close();

        LocalMusicDBHelper localMusicDBHelper = new LocalMusicDBHelper(this);
        db = localMusicDBHelper.getWritableDatabase();
        db.close();
        localMusicDBHelper.close();

        FavourListDBHelper favourListDBHelper = new FavourListDBHelper(this);
        db = favourListDBHelper.getWritableDatabase();
        db.close();
        favourListDBHelper.close();
    }
}
