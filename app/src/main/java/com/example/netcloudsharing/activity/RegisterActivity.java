package com.example.netcloudsharing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.Fragment.MainActivity;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.tool.BaseTool;
import com.example.netcloudsharing.tool.UserDao;
import com.example.netcloudsharing.tool.Userinfo;

import java.util.UUID;

//import android.os.Message;
//import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText etAccount;
    private EditText etPassword;
    private Handler mainHandle;    //主线程
    private UserDao dao;    //用户操作数据库类
    private Context context = this;
    boolean isExist = false;
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
                String uid = UUID.randomUUID().toString();
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                register(uid, account, password);
            }
        });
    }

    /**
     * @param uid 系统自动生成的唯一标识符
     * @param account 用户账号
     * @param password 用户密码
     */
    private void register(final String uid, final String account, final String password) {
        if (TextUtils.isEmpty(account)) {
            BaseTool.showShortMsg(RegisterActivity.this, getString(R.string.please_input_account));
            etAccount.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            BaseTool.showShortMsg(RegisterActivity.this, getString(R.string.please_input_password));
            etPassword.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Userinfo item = null;
                    int result = 0;
                    if(dao.queryIsRegister(account)){
                        isExist = true;
                    }else{
                        item = new Userinfo(uid, account, password);
                        result = dao.addUser(item);
                        isExist = false;
                    }
                    final int finalResult = result;
                    mainHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            if(isExist){
                                BaseTool.showDlaMsg(RegisterActivity.this, "已经存在该用户名，请更换用户名重新注册");
                            }else if (finalResult == 0)
                                BaseTool.showDlaMsg(RegisterActivity.this, "注册失败，请等待5秒后重新注册");
                            else {
                                BaseTool.showDlaMsg(RegisterActivity.this,"注册成功");
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(context, MainActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
