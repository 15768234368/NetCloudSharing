package com.example.netcloudsharing.activity;

//import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.BaseTool;
import com.example.netcloudsharing.tool.UserDao;
import com.example.netcloudsharing.tool.Userinfo;

import com.example.netcloudsharing.Fragment.MainActivity;

//import android.os.Message;

public class LoginActivity extends AppCompatActivity {
    private EditText etAccount;
    private EditText etPassword;
    private CheckBox cbAccount;
    private CheckBox cbPassword;
    private Handler mainHandle; //主线程
    private UserDao dao; //用户数据库操作类
    private String TAG = "LoginActivity";
    private String SP_ACCOUNT = "sp_account";
    private String SP_PASSWORD = "sp_password";
    private String SP_IS_REMEMBER_ACCOUNT = "sp_is_remember_account";
    private String SP_IS_REMEMBER_Password = "sp_is_remember_password";
    private SharedPreferences sharedPreferences;
    private boolean isCheckedAccount = false;
    private boolean isCheckedPassword = false;
    private Context context = this;
    private Permission permission;
    public static Userinfo bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initUI();
        //初始化数据
        initData();
        permission = new Permission();
        permission.checkPermission(LoginActivity.this);

    }

    private void initData() {
        if (sharedPreferences == null) {
            sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        isCheckedAccount = sharedPreferences.getBoolean(SP_IS_REMEMBER_ACCOUNT, false);
        isCheckedPassword = sharedPreferences.getBoolean(SP_IS_REMEMBER_Password, false);
        //回写数据
        if (isCheckedAccount) {
            etAccount.setText(sharedPreferences.getString(SP_ACCOUNT, ""));
        }
        if (isCheckedPassword) {
            etPassword.setText(sharedPreferences.getString(SP_PASSWORD, ""));
        }
        cbAccount.setChecked(isCheckedAccount);
        cbPassword.setChecked(isCheckedPassword);
    }

    private void initUI() {
        dao = new UserDao();    //操作数据库类
        mainHandle = new Handler(getMainLooper());  //获取主线程

        etAccount = findViewById(R.id.login_et_account);
        etPassword = findViewById(R.id.login_et_password);


        //设置编辑框监听器
        login_etListener();

        cbAccount = findViewById(R.id.cb_remember_account);
        cbPassword = findViewById(R.id.cb_remember_password);

        //设置复选框监听器
        login_cbListener();

        Button btnLogin = findViewById(R.id.login_btn_login);
        //设立点击事件——登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                login(account, password);
            }
        });
        Button btnTouristLogin = findViewById(R.id.login_btn_tourist);
        btnTouristLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTool.showShortMsg(context, "欢迎进入");
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("login_type", 0);
                startActivity(intent);
            }
        });
    }

    /**
     * 复选框监听器
     */
    private void login_cbListener() {
        /**
         * 设立点击记住账号事件
         */
        cbAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "账号状态为:" + isChecked);
                isCheckedAccount = isChecked;
                //实例化sharedPreferences对象
                if (sharedPreferences == null) {
                    sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                }

                //实例化sharedPreferences的编辑者对象
                SharedPreferences.Editor edit = sharedPreferences.edit();
                //存储数据
                if (isChecked) {
                    edit.putString(SP_ACCOUNT, etAccount.getText().toString().trim());
                }
                edit.putBoolean(SP_IS_REMEMBER_ACCOUNT, isChecked);
                //提交
                edit.commit();
            }
        });
        /**
         * 设立点击记住密码事件
         */
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "密码状态为:" + isChecked);
                isCheckedPassword = isChecked;
                //实例化sharedPreferences对象
                if (sharedPreferences == null) {
                    sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                }

                //实例化sharedPreferences的编辑者对象
                SharedPreferences.Editor edit = sharedPreferences.edit();
                //存储数据
                if (isChecked) {
                    edit.putString(SP_PASSWORD, etPassword.getText().toString().trim());
                }
                edit.putBoolean(SP_IS_REMEMBER_Password, isChecked);
                //提交
                edit.commit();
            }
        });
    }

    /**
     * 编辑框监听器
     */
    private void login_etListener() {
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isCheckedAccount) {
                    //实例化sharedPreferences对象
                    if (sharedPreferences == null) {
                        sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    }
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(SP_ACCOUNT, etAccount.getText().toString().trim());
                    edit.commit();
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isCheckedPassword) {
                    //实例化sharedPreferences对象
                    if (sharedPreferences == null) {
                        sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    }
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(SP_PASSWORD, etPassword.getText().toString().trim());
                    edit.commit();
                }
            }
        });
    }

    /**
     * 执行登录操作
     *
     * @param account  账号
     * @param password 密码
     */
    private void login(final String account, final String password) {
        if (TextUtils.isEmpty(account)) {
            BaseTool.showShortMsg(this, getString(R.string.please_input_account));
            etAccount.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            BaseTool.showShortMsg(this, getString(R.string.please_input_password));
            etPassword.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Userinfo item = dao.getUserByUnameAndUpass(account, password);
                    bean = item;
                    mainHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            if (item == null) {
                                BaseTool.showDlaMsg(context, "用户名或密码错误");
                            } else {
                                BaseTool.showShortMsg(context, "登录成功");
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(context, MainActivity.class);
                                intent.putExtra("uid", item.getUid());
                                intent.putExtra("account", item.getAccount());
                                intent.putExtra("login_type", 1);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }).start();
        }

    }
}
