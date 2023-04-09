package com.example.netcloudsharing;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}
