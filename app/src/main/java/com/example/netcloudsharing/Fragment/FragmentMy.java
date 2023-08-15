package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.activity.EditInfo;
import com.example.netcloudsharing.diary.MainActivity;
import com.example.netcloudsharing.tool.MusicUtil;
import com.example.netcloudsharing.tool.UserDao;
import com.example.netcloudsharing.tool.Userinfo;

import static com.example.netcloudsharing.MainActivity.login_type;

public class FragmentMy extends Fragment implements View.OnClickListener {
    private Button editInfo_btn;
    private ImageView userImage_iv;
    private TextView userName_tv, userAccount_tv, gender_tv, birthday_tv, constellation_tv;
    private TextView nowLive_tv, birthplace_tv, email_tv, info_tv;
    String uid = com.example.netcloudsharing.MainActivity.uid;
    View view;
    private Userinfo bean;
    private Userinfo curBean;
    Handler handler = new Handler(Looper.getMainLooper());
    //login_type == 1为用户登录,否则为游客登录
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_my, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (login_type == 1) {
            init(view);
        }
    }


    private void init(View view) {
        editInfo_btn = (Button) view.findViewById(R.id.fragment_my_btn_editInfo);
        view.findViewById(R.id.fragment_my_ll_diary).setOnClickListener(this);
        userImage_iv = view.findViewById(R.id.fragment_my_iv_headPortrail);
        userName_tv = view.findViewById(R.id.my_userName);
        userAccount_tv = view.findViewById(R.id.my_userAccount);
        gender_tv = view.findViewById(R.id.my_gender);
        birthday_tv = view.findViewById(R.id.my_birthday);
        constellation_tv = view.findViewById(R.id.my_constellation);
        nowLive_tv = view.findViewById(R.id.my_nowLive);
        birthplace_tv = view.findViewById(R.id.my_birthplace);
        email_tv = view.findViewById(R.id.my_email);
        info_tv = view.findViewById(R.id.my_info);
        if (bean.getPic() != null)
            userImage_iv.setImageBitmap(MusicUtil.getImageThumbnail(bean.getPic(), 200, 200));
        if (bean.getName() != null)
            userName_tv.setText(bean.getName());
        else
            userName_tv.setText(bean.getUid().substring(0, 8));
        if (bean.getAccount() != null)
            userAccount_tv.setText(bean.getAccount());
        if (bean.getGender() != null)
            gender_tv.setText(bean.getGender());
        else
            gender_tv.setText("未知");
        if (bean.getBirthday() != null)
            birthday_tv.setText(bean.getGender());
        else
            birthday_tv.setText("未知");
        if (bean.getConstellation() != null)
            constellation_tv.setText(bean.getConstellation());
        else
            constellation_tv.setText("未知");
        if (bean.getNowLive() != null)
            nowLive_tv.setText(bean.getNowLive());
        else
            nowLive_tv.setText("未知");
        if (bean.getBirthplace() != null)
            birthplace_tv.setText(bean.getBirthplace());
        else
            birthplace_tv.setText("未知");
        if (bean.getEmail() != null)
            email_tv.setText(bean.getEmail());
        else
            email_tv.setText("未知");
        if (bean.getInfo() != null)
            info_tv.setText(bean.getInfo());
        else
            info_tv.setText("___________等待您补充喔___________");
        editInfo_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_my_ll_diary:
                Intent intent_diary = new Intent(getActivity(), MainActivity.class);
                startActivity(intent_diary);
                break;
            case R.id.fragment_my_btn_editInfo:
                Intent intent_editInfo = new Intent(getActivity(), EditInfo.class);
                startActivity(intent_editInfo);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao dao = new UserDao();
                curBean = dao.getUserByUid(uid);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 更新UI界面的代码
                        if (login_type == 1)
                            setInfo(curBean);
                    }
                });

            }
        }).start();
    }

    private void setInfo(Userinfo bean) {
        if (bean.getPic() != null)
            userImage_iv.setImageBitmap(MusicUtil.getImageThumbnail(bean.getPic(), 200, 200));
        if (bean.getName() != null)
            userName_tv.setText(bean.getName());
        else
            userName_tv.setText(bean.getUid().substring(0, 8));
        if (bean.getAccount() != null)
            userAccount_tv.setText(bean.getAccount());
        if (bean.getGender() != null)
            gender_tv.setText(bean.getGender());
        else
            gender_tv.setText("未知");
        if (bean.getBirthday() != null)
            birthday_tv.setText(bean.getGender());
        else
            birthday_tv.setText("未知");
        if (bean.getConstellation() != null)
            constellation_tv.setText(bean.getConstellation());
        else
            constellation_tv.setText("未知");
        if (bean.getNowLive() != null)
            nowLive_tv.setText(bean.getNowLive());
        else
            nowLive_tv.setText("未知");
        if (bean.getBirthplace() != null)
            birthplace_tv.setText(bean.getBirthplace());
        else
            birthplace_tv.setText("未知");
        if (bean.getEmail() != null)
            email_tv.setText(bean.getEmail());
        else
            email_tv.setText("未知");
        if (bean.getInfo() != null)
            info_tv.setText(bean.getInfo());
        else
            info_tv.setText("___________等待您补充喔___________");
    }
}
