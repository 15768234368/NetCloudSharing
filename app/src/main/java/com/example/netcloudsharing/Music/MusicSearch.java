package com.example.netcloudsharing.Music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netcloudsharing.R;

public class MusicSearch extends AppCompatActivity implements View.OnClickListener {
    private SearchView mSearchView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_search);
        init();
    }

    private void init() {
        findViewById(R.id.fragment_music_ibBackToMusicHome).setOnClickListener(this);
        mSearchView = findViewById(R.id.activity_music_search_svSearchContext);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                path = "http://10.77.169.168/test1.mp3";
                Intent intent = new Intent(MusicSearch.this,HttpGetDemoActivity.class);
                intent.putExtra("path",path);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_music_ibBackToMusicHome:
                finish();
                break;
//            case R.id.frag
        }
    }
}
