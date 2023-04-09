package com.example.netcloudsharing.diary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.netcloudsharing.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xujiaji.happybubble.BubbleDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NotesDB notesDB;
    private Button btnText, btnImg, btnVideo;
    private ListView lv;
    private Intent intent;
    private MyAdapter adapter;
    private BottomSheetDialog dialog;
    private final int REQUEST_CAMERA = 1;
    private final int REQUEST_PICK = 2;
    private final int REQUEST_ALTER = 3;
    private String currentPath = null;
    private Permission permission;
    private List<NoteInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);
        init();
        mData = new ArrayList<>();
        notesDB = new NotesDB(this);
        Log.d("Main", "onCreate");
    }

    private void init() {
        btnText = findViewById(R.id.btn_text);
        btnImg = findViewById(R.id.btn_img);
        btnVideo = findViewById(R.id.btn_video);
        lv = findViewById(R.id.lv);

        btnVideo.setOnClickListener(this);
        btnText.setOnClickListener(this);
        btnImg.setOnClickListener(this);

        String[] mode = {"相机", "相册", "取消"};
        ListView listView = new ListView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mode));

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(listView);

        //选择图文模块后顶部弹出listView选择框
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                permission = new Permission();
                permission.checkPermission(MainActivity.this);
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

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

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

    @Override
    public void onClick(View v) {
        intent = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.btn_text:
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case R.id.btn_img:
                intent.putExtra("flag", "2");
                dialog.show();
                break;
            case R.id.btn_video:
                intent.putExtra("flag", "3");
                startActivity(intent);
                break;
        }
    }

    public void deleteDB(int position) {
        String id = mData.get(position).getId();
        SQLiteDatabase dbWrite = notesDB.getWritableDatabase();
        dbWrite.delete(NotesDB.TABLE_NAME, NotesDB.ID + "=?", new String[]{id + ""});
        dbWrite.close();
        selectDB();
    }


    public void selectDB() {
        //将链表清空
        mData.clear();
        SQLiteDatabase dbReader = notesDB.getReadableDatabase();
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(NotesDB.ID));
            String content = cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT));
            String path = cursor.getString(cursor.getColumnIndex(NotesDB.PATH));
            String video = cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO));
            String time = cursor.getString(cursor.getColumnIndex(NotesDB.TIME));
            int type = cursor.getInt(cursor.getColumnIndex(NotesDB.TYPE));
            NoteInfo info = new NoteInfo(content, path, video, id, time, type);
            mData.add(info);
        }
        cursor.close();
        dbReader.close();
        adapter = new MyAdapter(this, mData);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddContent.class);
                NoteInfo noteInfo = mData.get(position);
                intent.putExtra("noteInfo", noteInfo);
                intent.putExtra("flag", noteInfo.getType() + "");
                Log.d("flag", noteInfo.getType() + "");
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                View viewDelete = LayoutInflater.from(MainActivity.this).inflate(R.layout.diary_button_delete, null);
                //设置长按删除按钮
                final BubbleDialog bubbleDialog = new BubbleDialog(MainActivity.this);
                bubbleDialog.addContentView(viewDelete).setClickedView(view).calBar(true).calBar(true).show();
                Button btnDelete = viewDelete.findViewById(R.id.diary_btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDB(position);
                        bubbleDialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    /**
     * 重写每次与屏幕连接的函数
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main", "OnResume");
        selectDB();
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
                    Intent intent = new Intent(this, AddContent.class);
                    intent.putExtra("path", currentPath);
                    intent.putExtra("flag", "2");
                    startActivity(intent);
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
                            Intent intent = new Intent(this, AddContent.class);
                            intent.putExtra("path", path);
                            intent.putExtra("flag", "2");
                            startActivity(intent);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 判断权限返回是否正确
     *
     * @param requestCode  返回字段
     * @param permissions  权限集
     * @param grantResults 结果集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permission.REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("permission", permissions[i] + "not");
                    Log.e("permission", grantResults[i] + "not");
                } else {
                    Log.e("permission", permissions[i] + "");
                    Log.e("permission", grantResults[i] + "");
                }
            }
        }
    }
}
