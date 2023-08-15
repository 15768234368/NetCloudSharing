package com.example.netcloudsharing.tool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BaseTool{
    private static final String TAG = "BaseTool";
    /**
     * 给button设置监听器
     * @param context   上下文
     * @param btn   按钮
     * @param cls   导向的class
     * @return
     */
    public static Button openActivity(final Context context, Button btn, final Class cls){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTool.navigateTo(context,cls);
            }
        });
        return btn;
    }

    /**
     * 导航到另一个Activity中
     * @param context   上下文
     * @param cls   导向的class
     */
    public static void navigateTo(Context context,Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }
    /**
     * 显示的短消息
     * @param context 上下文
     * @param msg   要显示的短消息
     */
    public static void showShortMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长消息
     * @param context   上下文
     * @param msg   要显示的消息
     */
    public static void showLongMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    /**
     * 显示消息对话框
     * @param context 上下文
     * @param msg 要显示的消息
     */
    public static void showDlaMsg(Context context,String msg){
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage(msg)
                .setPositiveButton("确定",null)
                .setNegativeButton("取消",null)
                .create().show();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerRadius) {
        // 创建一个正方形的Bitmap
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 创建一个Canvas对象，将output作为绘制的目标
        Canvas canvas = new Canvas(output);

        // 创建一个Paint对象，并设置其属性
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left = 0;
        float top = 0;
        float right = width;
        float bottom = height;
        // 创建一个RectF对象，用于定义圆角的矩形区域
        RectF rectF = new RectF(left, top, right, bottom);


        // 将Bitmap绘制到Canvas上，并通过RectF对象定义圆角
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        // 设置Paint的Xfermode为PorterDuff.Mode.SRC_IN，以保留与圆角重叠的部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 将原始的Bitmap绘制到Canvas上
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    public static void getBitmapFromUrl(Activity mActivity, ImageView imageView, String videoPicUrl, int radius) {
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

// 创建请求对象
        Request request = new Request.Builder()
                .url(videoPicUrl)
                .build();

// 发送异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    // 获取响应的数据流
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        // 将响应的数据流转换为字节数组
                        byte[] imageBytes;
                        try {
                            imageBytes = responseBody.bytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(getRoundedCornerBitmap(bitmap, radius));
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void getBitmapFromUrlOri(Activity mActivity, ImageView imageView, String videoPicUrl) {
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

// 创建请求对象
        Request request = new Request.Builder()
                .url(videoPicUrl)
                .addHeader("cookie", "kg_mid=a8f54eaf7effd382b4db936d2902cf20; kg_dfid=1txtXp0lYJ0c0r1pV11MtP5s; KuGooRandom=66541691400414345; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1691400405,1691729457,1691842117,1691950832; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1691959884")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .build();

// 发送异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: ");
                if (response.isSuccessful()) {
                    // 获取响应的数据流
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        // 将响应的数据流转换为字节数组
                        byte[] imageBytes;
                        try {
                            imageBytes = responseBody.bytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}
