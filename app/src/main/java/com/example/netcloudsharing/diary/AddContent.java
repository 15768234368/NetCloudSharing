package com.example.netcloudsharing.diary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddContent extends AppCompatActivity implements View.OnClickListener {
    private String val;
    private Button btnSave, btnCancel, btnBack;
    private EditText etText;
    private ImageView ivImage;
    private VideoView vvVideo;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File file = null;
    private String currentPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_add_content);
        init();
        val = getIntent().getStringExtra("flag");
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        selectView();
    }

    /**
     * 选择将图片或视频进行显示
     */
    public void selectView() {
        NoteInfo info = (NoteInfo) getIntent().getSerializableExtra("noteInfo");
        if (val.equals("1")) {
            //文字
            ivImage.setVisibility(View.GONE);
            vvVideo.setVisibility(View.GONE);
            if(info!=null){
                etText.setText(info.getContent());
            }
        } else if (val.equals("2")) {
            //图片
            ivImage.setVisibility(View.VISIBLE);
            vvVideo.setVisibility(View.GONE);
            if(info!=null){
                currentPath = info.getPath();
                etText.setText(info.getContent());
            }else{
                currentPath = getIntent().getStringExtra("path");
            }
            Bitmap bitmap = BitmapFactory.decodeFile(currentPath);
            ivImage.setImageBitmap(bitmap);
        } else if (val.equals("3")) {
            //视频
            ivImage.setVisibility(View.GONE);
            vvVideo.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        btnSave = findViewById(R.id.addContent_btnSave);
        btnCancel = findViewById(R.id.addContent_btnCancel);
        btnBack = findViewById(R.id.addContent_btnBack);
        etText = findViewById(R.id.addContent_etText);
        ivImage = findViewById(R.id.addContent_ivImage);
        vvVideo = findViewById(R.id.addContent_vvVideo);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, etText.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH, currentPath);
        cv.put(NotesDB.TYPE, val);
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date curDate = new Date();
        return format.format(curDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addContent_btnSave:
                addDB();
                finish();
                break;
            case R.id.addContent_btnCancel:
                finish();
                break;
            case R.id.addContent_btnBack:
                finish();
                break;
        }
    }
}
