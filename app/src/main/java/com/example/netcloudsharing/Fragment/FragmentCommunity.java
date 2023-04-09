package com.example.netcloudsharing.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netcloudsharing.R;
import com.example.netcloudsharing.game.WhitePiecesView;

public class FragmentCommunity extends Fragment implements WhitePiecesView.WhitePiecesListener {
    private WhitePiecesView whitePiecesView;
    private TextView scoreText;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        whitePiecesView = view.findViewById(R.id.write_pieces);
        scoreText = (TextView) view.findViewById(R.id.score);
        whitePiecesView.setWhitePiecesListener(this);


    }

    @Override
    public void getScore(int score, int grade) {
        scoreText.setText("" + score + "  等级为" + grade);
    }

    @Override
    public void gameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Game Over")
                .setMessage("您获得的分数为" + scoreText.getText() + "是否重新开始")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whitePiecesView.restart();
                        return;
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                }).show();
    }

    @Override
    public void gameWin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Game Wining")
                .setMessage("您获得的分数为" + scoreText.getText() + " 是否重新开始")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whitePiecesView.restart();
                        return;
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                }).show();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }
}