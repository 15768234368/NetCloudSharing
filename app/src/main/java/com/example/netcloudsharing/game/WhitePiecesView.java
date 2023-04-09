package com.example.netcloudsharing.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;


public class WhitePiecesView extends androidx.appcompat.widget.AppCompatTextView {

    private final static String TAG = WhitePiecesView.class.getSimpleName();

    private Paint mPaint;

    private PiecesRectF[][] ovals = new PiecesRectF[5][4];//屏幕上的方格块
    private SparseArray<PiecesRectF> selectOvals = new SparseArray<PiecesRectF>();//点击时选中的方格块

    private int topOvalHeight = 0;//最上面一行方格的高度
    private int score = 0;

    private boolean isGameOver;//游戏是否结束
    private boolean isGameWin;//游戏输赢
    private boolean once = true;
    private int index;
    private int speed = 20;

    public WhitePiecesView(Context context) {
        this(context,null);
    }

    public WhitePiecesView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WhitePiecesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        initOvals();//初始化方块
    }

    private void initOvals(){

        for (int i = 0;i < 5;i++){
            for (int j = 0;j<4;j++){

                ovals[i][j] = new PiecesRectF();
                if (j == 1){
                    //如果是第二列，则若第一列的是黑块将这个方块设为白块
                    if (ovals[i][j-1].getType() == PiecesRectF.BLAKE ||
                            ovals[i][j-1].getType() == PiecesRectF.START) {
                        ovals[i][j].setType(PiecesRectF.WRITE);
                    }
                }else if (j == 3){
                    //如果是第四列，同样若第一列的是黑块将这个方块设为白块
                    if (ovals[i][j-1].getType() == PiecesRectF.BLAKE ||
                            ovals[i][j-1].getType() == PiecesRectF.START) {
                        ovals[i][j].setType(PiecesRectF.WRITE);
                    }
                    //如果是第四列，且前四列的都为白块，则设为黑块
                    else if (ovals[i][j-2].getType() == PiecesRectF.WRITE &&
                            ovals[i][j-3].getType() == PiecesRectF.WRITE) {
                        ovals[i][j].setType(PiecesRectF.BLAKE);
                    }
                }

                if (i == 4){
                    if (ovals[i][j].getType() == PiecesRectF.BLAKE)
                        ovals[i][j].setType(PiecesRectF.START);//若是最后一行，将黑块的type替换为START

                }
            }
        }
    }


    private WhitePiecesListener MyWhitePiecesListener;

    public void setWhitePiecesListener(WhitePiecesListener myWhitePiecesListener) {
        MyWhitePiecesListener = myWhitePiecesListener;
    }

    public interface WhitePiecesListener{
        void getScore(int score,int grade);
        void gameOver();
        void gameWin();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    private void drawRect(Canvas canvas){
        int w = getWidth()/4;//得到每个小方块的宽
        int h = getHeight()/4;//得到每个小方块的高
        if(isGameWin){
            MyWhitePiecesListener.gameWin();
            isGameWin = false;
        }
        if (isGameOver){
            //游戏结束
            MyWhitePiecesListener.gameOver();
            isGameOver = false;
        }
        for (int i = 0;i < 5;i++){
            for (int j = 0;j < 4;j++){

                ovals[i][j].left = w * j;
                ovals[i][j].right = w * (j + 1);
                ovals[i][j].bottom = topOvalHeight + i * h;
                ovals[i][j].top = ovals[i][j].bottom - h;

                mPaint.setStyle(Paint.Style.FILL);//绘制色块时，设置画笔为FILL
                switch (ovals[i][j].getType()){
                    case PiecesRectF.BLAKE:{
                        //绘制黑块
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(ovals[i][j],mPaint);
                        break;
                    }
                    case PiecesRectF.BLUE:{
                        //绘制蓝块
                        mPaint.setColor(Color.BLUE);
                        canvas.drawRect(ovals[i][j],mPaint);
                        break;
                    }
                    case PiecesRectF.RED:{
                        //绘制红块
                        mPaint.setColor(Color.RED);
                        canvas.drawRect(ovals[i][j],mPaint);
                        break;
                    }
                    case PiecesRectF.START:{
                        //先绘制黑块
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(ovals[i][j],mPaint);

                        //在绘制文字
                        mPaint.setColor(Color.parseColor("#ffffff"));
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTextSize(50);

                        String start = "开始";
                        Rect bounds = new Rect();
                        mPaint.getTextBounds(start,0,start.length(),bounds);
                        float x = ovals[i][j].left / 2 + ovals[i][j].right / 2;
                        float y = ovals[i][j].top / 2 + ovals[i][j].bottom / 2 + bounds.bottom / 2 - bounds.top / 2;
                        canvas.drawText(start,x,y,mPaint);
                        break;
                    }
                }
                //设置画笔为STROKE，绘制边框
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.parseColor("#ffffff"));
                mPaint.setStrokeWidth(3);
                canvas.drawRect(ovals[i][j],mPaint);
            }
        }
    }

    //@android.support.annotation.RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        switch (event.getActionMasked()){
            case ACTION_DOWN:
            case ACTION_POINTER_DOWN: {
                int id = event.getPointerId(index);
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        PiecesRectF f = ovals[i][j];
                        if (event.getX() > f.left && event.getX() < f.right
                                && event.getY() > f.top && event.getY() < f.bottom) {
                            selectOvals.put(id, f);//点击选中的方块存入SparseArray

                            switch (f.getType()){
                                case PiecesRectF.BLAKE:{
                                    if (!once) {
                                        f.setType(PiecesRectF.BLUE);
                                        score++;
                                    }
                                    break;
                                }
                                case PiecesRectF.START:{
                                    if (once){
                                        //判断第一次按中
                                        startThread();
                                        once = false;
                                    }
                                    //按黑块处理
                                    f.setType(PiecesRectF.BLUE);
                                    score++;
                                    break;
                                }
                                case PiecesRectF.WRITE:{
                                    if (!once) {
                                        //点中白块GameOver
                                        f.setType(PiecesRectF.RED);
                                        isGameOver = true;
                                        invalidate();
                                    }
                                    break;
                                }
                            }
                            MyWhitePiecesListener.getScore(score,21-speed);
                        }
                    }
                }
                break;
            }
            case ACTION_UP:
            case ACTION_POINTER_UP:{
                int id = event.getPointerId(index);
                PiecesRectF f = selectOvals.get(id,null);//得到某个手指选中的方块
                if (f != null && f.getType() == PiecesRectF.BLUE ){
                    //手指抬起后，将蓝色重新变红
                    f.setType(PiecesRectF.WRITE);
                }
                break;
            }
        }
        return true;
    }

    private void startThread() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                while(true){
                    if(speed == 5){
                        isGameWin = true;
                        postInvalidate();
                        return;
                    }
                    if(score>=10*(21-speed)){
                        speed--;
                    }
                    topOvalHeight =topOvalHeight + 20;//这里写死了滑动速度
                    if (isGameOver) {//如果游戏已经结束，结束循环
                        return;
                    }
                    if (topOvalHeight > getHeight()/4) {
                        topOvalHeight = 0;//若最顶层的方块的高，超出正常方块的的高，清零。
                        if (checkBottomOvals()){//检测是否有黑色方块漏点
                            //有漏点，游戏结束
                            isGameOver = true;
                            postInvalidate();
                            return;
                        }else
                            //没有漏点，更新方块游戏结束
                            updateRectF();
                    }
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }).start();
    }


    private boolean checkBottomOvals(){
        boolean haveBlake = false;
        for (int i = 0;i < 4;i++){
            if (ovals[4][i].getType() == PiecesRectF.BLAKE
                    || ovals[4][i].getType() == PiecesRectF.START) {
                //判断最后一行是否存在BLAKE或START方块
                //如果有将其设为红色
                ovals[4][i].setType(PiecesRectF.RED);
                haveBlake = true;
            }
        }
        return haveBlake;
    }

    /**
     * 更新方块
     */
    private void updateRectF(){

        for (int i = 4; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {//如果是第一行，重新初始化一行
                    ovals[i][j] = new PiecesRectF();

                    if (j == 1){
                        if (ovals[i][j-1].getType() == PiecesRectF.BLAKE)
                            ovals[i][j].setType(PiecesRectF.WRITE);
                    }else if (j == 3){
                        if (ovals[i][j-1].getType() == PiecesRectF.BLAKE)
                            ovals[i][j].setType(PiecesRectF.WRITE);
                        else if (ovals[i][j-2].getType() == PiecesRectF.WRITE &&
                                ovals[i][j-3].getType() == PiecesRectF.WRITE)
                            ovals[i][j].setType(PiecesRectF.BLAKE);
                    }
                }
                else//否则，将行数后移
                    ovals[i][j] = ovals[i - 1][j];
            }
        }
    }

    public void restart(){
        once = true;
        topOvalHeight = 0;//顶层高度归零
        score = 0;//分数归零
        speed = 20;
        MyWhitePiecesListener.getScore(score,21-speed);
        initOvals();//重新初始化
        invalidate();//再次绘制
    }
}
