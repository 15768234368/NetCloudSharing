package com.example.netcloudsharing.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.netcloudsharing.MainActivity;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.Permission;
import com.example.netcloudsharing.tool.UserDao;
import com.example.netcloudsharing.tool.Userinfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditInfo extends AppCompatActivity {
    Button commit_btn;
    ImageView userImage_iv;
    EditText alterName_et, alterBirthday_et, alterConstellation_et, alterNowLive_et;
    EditText alterHometown_et, alterEmail_et, alterInfo_et, alterGender_et;
    String uid = MainActivity.uid;
    String account = MainActivity.account;
    private BottomSheetDialog dialog;
    private Permission permission;
    private final int REQUEST_CAMERA = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_PICK = 2;
    private String currentPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initView();
    }

    private void initView() {
        commit_btn = findViewById(R.id.userInfo_commit_btn);
        userImage_iv = findViewById(R.id.userInfo_image_iv);
        alterName_et = findViewById(R.id.userInfo_name_et);
        alterGender_et = findViewById(R.id.userInfo_gender_et);
        alterBirthday_et = findViewById(R.id.userInfo_birthday_et);
        alterConstellation_et = findViewById(R.id.userInfo_constellation_et);
        alterNowLive_et = findViewById(R.id.userInfo_nowLive_et);
        alterHometown_et = findViewById(R.id.userInfo_hometown_et);
        alterEmail_et = findViewById(R.id.userInfo_email_et);
        alterInfo_et = findViewById(R.id.userInfo_info_et);


        String[] mode = {"相机", "相册", "取消"};
        ListView listView = new ListView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mode));

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(listView);
        userImage_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        //选择图文模块后顶部弹出listView选择框
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                permission = new Permission();
                permission.checkPermission(EditInfo.this);
                switch (position) {
                    case 0:
                        dispatchTakePictureIntent();
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent1.setType("image/*");
                        startActivityForResult(intent1, REQUEST_PICK);
                        dialog.dismiss();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String name = alterName_et.getText().toString().trim();
                        final String gender = alterGender_et.getText().toString().trim();
                        final String birthday = alterBirthday_et.getText().toString().trim();
                        final String constellation = alterConstellation_et.getText().toString().trim();
                        final String nowLive = alterNowLive_et.getText().toString().trim();
                        final String hometown = alterHometown_et.getText().toString().trim();
                        final String email = alterEmail_et.getText().toString().trim();
                        final String info = alterInfo_et.getText().toString().trim();
                        UserDao dao = new UserDao();
                        dao.editUser(new Userinfo(uid, currentPath, name, account, null, gender, birthday, constellation, nowLive, hometown, email, info));
                        finish();
                    }
                }).start();
            }
        });
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //必须确保手机上有相机，才可以继续，否则会一直闪退，故加判断语句
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.netcloudsharing.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String imageFileName = getTimeToPath();
        Log.d("imageFileName", imageFileName);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPath = image.getAbsolutePath();
        Log.d("imageFileName", currentPath);
        return image;
    }

    public String getTimeToPath() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date curDate = new Date();
        return format.format(curDate);
    }

    /**
     * 选择图文按钮中的拍照或选择图库中的图片返回函数
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA: {
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPath);
                    userImage_iv.setImageBitmap(bitmap);
                    break;
                }
                case REQUEST_PICK: {
                    Uri uri = null;
                    // try 是有可能未选择图片就进行返回，会造成空指针
                    try {
                        uri = data.getData(); //获取系统返回的照片uri
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } finally {
                        if (uri != null) {
                            // 使用ContentResolve 进行读取选择的图片路径
                            String[] strings = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex(strings[0]);
                            String path = cursor.getString(index); //获取图片路径
                            cursor.close();
                            currentPath = path;
                            Bitmap bitmap = BitmapFactory.decodeFile(currentPath);
                            userImage_iv.setImageBitmap(bitmap);
                        }
                    }
                    break;
                }
            }
        }
    }

}
