package com.example.netcloudsharing.game;

import android.graphics.RectF;

public class PiecesRectF extends RectF {
    private int type;

    public final static int BLAKE = 0;//黑块
    public final static int WRITE = 1;//白块
    public final static int BLUE = 2;//黑块按下时的显示蓝块
    public final static int START = 3;//标记有开始的黑块
    public final static int RED = 4;//按到白块，或有黑块漏按，游戏结束时的红块

    public PiecesRectF(){
        super();
        type = Math.random() > 0.5 ? 0:1;//初始化时，给type随机一个白块或黑块
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
