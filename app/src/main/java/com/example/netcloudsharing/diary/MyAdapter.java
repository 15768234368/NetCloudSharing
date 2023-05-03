package com.example.netcloudsharing.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netcloudsharing.R;

import java.util.List;

import static com.example.netcloudsharing.tool.MusicUtil.getImageThumbnail;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private List<NoteInfo> data;

    MyAdapter(Context context, List<NoteInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.diary_list_item, parent, false);
            holder = new ViewHolder();
            holder.tv_listContent = convertView.findViewById(R.id.list_tvContent);
            holder.tv_listTime = convertView.findViewById(R.id.list_tvTime);
            holder.iv_listImage = convertView.findViewById(R.id.list_ivImg);
            holder.iv_listVideo = convertView.findViewById(R.id.list_ivVideo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_listContent.setText(data.get(position).getContent());
        holder.tv_listTime.setText(data.get(position).getTime());
        holder.iv_listImage.setImageBitmap(getImageThumbnail(data.get(position).getPath(),
                200, 200));
        return convertView;
    }

}

class ViewHolder {
    public TextView tv_listContent, tv_listTime;
    public ImageView iv_listImage, iv_listVideo;
}
