package com.example.netcloudsharing.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.diary.MainActivity;

public class FragmentMy extends Fragment implements View.OnClickListener {
    private Button btnDiary;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        view.findViewById(R.id.fragment_my_ll_diary).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_my_ll_diary:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
