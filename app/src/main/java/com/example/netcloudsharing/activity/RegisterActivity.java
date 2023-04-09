package com.example.netcloudsharing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.BaseTool;
import com.example.netcloudsharing.tool.UserDao;
import com.example.netcloudsharing.tool.Userinfo;

import java.util.Date;

import com.example.netcloudsharing.Fragment.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etAccount;
    private EditText etPassword;
    private Handler mainHandle;    //主线程
    private UserDao dao;    //用户操作数据库类
    private Context context = this;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            //super.handleMessage(msg);
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAccount = findViewById(R.id.register_et_account);
        etPassword = findViewById(R.id.register_et_password);
        Button btnRegister = findViewById(R.id.register_btn_register);

        dao = new UserDao();
        mainHandle = new Handler(getMainLooper());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String createDt = String.format("%tY-%<tm-%<td",new Date());
                register(account,password,createDt);
            }
        });
    }
    private void register(final String account, final String password,final String createDt){
        if(TextUtils.isEmpty(account)){
            BaseTool.showShortMsg(RegisterActivity.this,getString(R.string.please_input_account));
            etAccount.requestFocus();
        }else  if(TextUtils.isEmpty(password)){
            BaseTool.showShortMsg(RegisterActivity.this,getString(R.string.please_input_password));
            etPassword.requestFocus();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Userinfo item = new Userinfo(0,account,password,createDt);
                    final int result = dao.addUser(item);
                    mainHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            if(result==0)
                                BaseTool.showDlaMsg(RegisterActivity.this,"注册失败，请等待5秒后重新注册");
                            else{
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(context,MainActivity.class);
                                context.startActivity(intent);
                            }
//                                BaseTool.showDlaMsg(RegisterActivity.this,"注册成功");
                        }
                    });
                }
            }).start();
        }
    }
}
