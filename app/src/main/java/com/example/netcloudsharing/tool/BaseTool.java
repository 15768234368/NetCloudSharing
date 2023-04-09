package com.example.netcloudsharing.tool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BaseTool{
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
}
