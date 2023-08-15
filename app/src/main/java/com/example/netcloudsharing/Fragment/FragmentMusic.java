package com.example.netcloudsharing.Fragment;

import static com.example.netcloudsharing.MainActivity.binder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.netcloudsharing.Bean.MusicBean;
import com.example.netcloudsharing.Bean.MusicSetBean;
import com.example.netcloudsharing.Bean.RankSetBean;
import com.example.netcloudsharing.Music.MusicSearch;
import com.example.netcloudsharing.R;
import com.example.netcloudsharing.activity.MusicSetActivity;
import com.example.netcloudsharing.task.MusicListRequestTask;
import com.example.netcloudsharing.task.MusicRankRequestTask;
import com.example.netcloudsharing.tool.BaseTool;
import com.example.netcloudsharing.tool.MusicUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class FragmentMusic extends Fragment implements View.OnClickListener {
    private static final String TAG = FragmentMusic.class.getSimpleName();


    private View thisView;
    //三个播放歌曲按钮
    public static ImageView nextIv, playIv, lastIv, songImage;
    //歌曲歌手
    public static TextView singerTv, songTv;
    private boolean firstOpen = true;
    //搜索歌曲
    private ImageButton ibSearch;
    //点击正在播放的音乐查看详细信息
    private RelativeLayout relativeLayout;
    private Banner banner;
    String musicListPic001, musicListPic002, musicListPic003;
    String musicListTitle001, musicListTitle002, musicListTitle003;
    String rankListPic001, rankListPic002, rankListPic003;
    List<String> rankListText001, rankListText002, rankListText003;
    private SearchView searchView;
    private List<String> bannerList = new ArrayList<>();
    private List<MusicSetBean> musicSetBeans = new ArrayList<>();
    private List<RankSetBean> rankSetBeans = new ArrayList<>();
    private String musicListHref001, musicListHref002, musicListHref003;
    // 在MainActivity中注册广播接收器
    private BroadcastReceiver musicPlayFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收到广播消息后进行相应的操作，例如更新适配器
            setMusicBean(binder.getMusicBean());
            if (playIv != null)
                playIv.setImageResource(R.mipmap.icon_pause);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initPlayer();
//        selectTab(0);
        initMusicList();
        initMusicRank();
    }

    private void initMusicRank() {
        ImageView iv_rankList001;
        ImageView iv_rankList002;
        ImageView iv_rankList003;
        iv_rankList001 = thisView.findViewById(R.id.iv_rankList001);
        iv_rankList002 = thisView.findViewById(R.id.iv_rankList002);
        iv_rankList003 = thisView.findViewById(R.id.iv_rankList003);

        TextView tv_rank001Text001, tv_rank001Text002, tv_rank001Text003;
        TextView tv_rank002Text001, tv_rank002Text002, tv_rank002Text003;
        TextView tv_rank003Text001, tv_rank003Text002, tv_rank003Text003;

        tv_rank001Text001 = thisView.findViewById(R.id.tv_rank001Text001);
        tv_rank002Text001 = thisView.findViewById(R.id.tv_rank002Text001);
        tv_rank003Text001 = thisView.findViewById(R.id.tv_rank003Text001);
        tv_rank001Text002 = thisView.findViewById(R.id.tv_rank001Text002);
        tv_rank002Text002 = thisView.findViewById(R.id.tv_rank002Text002);
        tv_rank003Text002 = thisView.findViewById(R.id.tv_rank003Text002);
        tv_rank001Text003 = thisView.findViewById(R.id.tv_rank001Text003);
        tv_rank002Text003 = thisView.findViewById(R.id.tv_rank002Text003);
        tv_rank003Text003 = thisView.findViewById(R.id.tv_rank003Text003);

        String url = "https://www.kugou.com/";
        MusicRankRequestTask task = new MusicRankRequestTask(new MusicRankRequestTask.MusicRankRequestListener() {
            @Override
            public void onSearchComplete(List<RankSetBean> beanList) {
                rankSetBeans = beanList;
                if (rankSetBeans != null) {
                    rankListPic001 = rankSetBeans.get(0).getRankListPic();
                    rankListPic002 = rankSetBeans.get(1).getRankListPic();
                    rankListPic003 = rankSetBeans.get(2).getRankListPic();
                    rankListText001 = rankSetBeans.get(0).getRankListPreThree();
                    rankListText002 = rankSetBeans.get(1).getRankListPreThree();
                    rankListText003 = rankSetBeans.get(2).getRankListPreThree();

                    if (rankListPic001 != null)
                        BaseTool.getBitmapFromUrlOri(getActivity(), iv_rankList001, rankListPic001);
                    if (rankListPic002 != null)
                        BaseTool.getBitmapFromUrlOri(getActivity(), iv_rankList002, rankListPic002);
                    if (rankListPic003 != null)
                        BaseTool.getBitmapFromUrlOri(getActivity(), iv_rankList003, rankListPic003);
                    if (rankListText001 != null) {
                        tv_rank001Text001.setText(rankListText001.get(0));
                        tv_rank001Text002.setText(rankListText001.get(1));
                        tv_rank001Text003.setText(rankListText001.get(2));
                    }
                    if (rankListText002 != null) {
                        tv_rank002Text001.setText(rankListText002.get(0));
                        tv_rank002Text002.setText(rankListText002.get(1));
                        tv_rank002Text003.setText(rankListText002.get(2));
                    }
                    if (rankListText003 != null) {
                        tv_rank003Text001.setText(rankListText003.get(0));
                        tv_rank003Text002.setText(rankListText003.get(1));
                        tv_rank003Text003.setText(rankListText003.get(2));
                    }
                }
            }

            @Override
            public void onSearchError() {
                Log.d(TAG, "onSearchError: ");
            }
        });
        task.execute(url);
    }

    private void initData() {
        musicListPic001 = musicListPic002 = musicListPic003 = null;
    }

    private void initMusicList() {
        ImageView iv_musicList001 = thisView.findViewById(R.id.iv_musicList001);
        ImageView iv_musicList002 = thisView.findViewById(R.id.iv_musicList002);
        ImageView iv_musicList003 = thisView.findViewById(R.id.iv_musicList003);

        iv_musicList001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicListHref001 != null && musicListPic001 != null && musicListTitle001 != null) {
                    Intent intent = new Intent(getContext(), MusicSetActivity.class);
                    intent.putExtra("href", musicListHref001);
                    intent.putExtra("pic", musicListPic001);
                    intent.putExtra("title", musicListTitle001);
                    startActivity(intent);
                }
            }
        });
        iv_musicList002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicListHref002 != null && musicListPic002 != null && musicListTitle002 != null) {
                    Intent intent = new Intent(getContext(), MusicSetActivity.class);
                    intent.putExtra("href", musicListHref002);
                    intent.putExtra("pic", musicListPic002);
                    intent.putExtra("title", musicListTitle002);
                    startActivity(intent);
                }
            }
        });
        iv_musicList003.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicListHref003 != null && musicListPic003 != null && musicListTitle003 != null) {
                    Intent intent = new Intent(getContext(), MusicSetActivity.class);
                    intent.putExtra("href", musicListHref003);
                    intent.putExtra("pic", musicListPic003);
                    intent.putExtra("title", musicListTitle003);
                    startActivity(intent);
                }
            }
        });


        TextView tv_musicList001 = thisView.findViewById(R.id.tv_musicList001);
        TextView tv_musicList002 = thisView.findViewById(R.id.tv_musicList002);
        TextView tv_musicList003 = thisView.findViewById(R.id.tv_musicList003);
        String url = "https://www.kugou.com/";
        MusicListRequestTask task = new MusicListRequestTask(new MusicListRequestTask.MusicListRequestListener() {
            @Override
            public void onSearchComplete(List<MusicSetBean> beanList) {
                musicSetBeans = beanList;
                if (musicSetBeans != null) {
                    musicListPic001 = musicSetBeans.get(0).getPhotoUrl();
                    musicListPic002 = musicSetBeans.get(1).getPhotoUrl();
                    musicListPic003 = musicSetBeans.get(2).getPhotoUrl();
                    musicListHref001 = musicSetBeans.get(0).getSetUrl();
                    musicListHref002 = musicSetBeans.get(1).getSetUrl();
                    musicListHref003 = musicSetBeans.get(2).getSetUrl();
                    musicListTitle001 = musicSetBeans.get(0).getSetName();
                    musicListTitle002 = musicSetBeans.get(1).getSetName();
                    musicListTitle003 = musicSetBeans.get(2).getSetName();
                    if (musicListPic001 != null)
                        BaseTool.getBitmapFromUrl(getActivity(), iv_musicList001, musicListPic001, 20);
                    if (musicListPic002 != null)
                        BaseTool.getBitmapFromUrl(getActivity(), iv_musicList002, musicListPic002, 20);
                    if (musicListPic003 != null)
                        BaseTool.getBitmapFromUrl(getActivity(), iv_musicList003, musicListPic003, 20);
                    if (musicListTitle001 != null)
                        tv_musicList001.setText(musicListTitle001);
                    if (musicListTitle002 != null)
                        tv_musicList002.setText(musicListTitle002);
                    if (musicListTitle003 != null)
                        tv_musicList003.setText(musicListTitle003);
                }
            }

            @Override
            public void onSearchError() {
                Log.d(TAG, "onSearchError: ");
            }
        });
        task.execute(url);


    }


    /**
     * 定义控件
     */
    private void initView() {

        singerTv = thisView.findViewById(R.id.local_music_bottom_tvSinger);
        songTv = thisView.findViewById(R.id.local_music_bottom_tvSong);


        nextIv = thisView.findViewById(R.id.local_music_bottom_ivNext);
        playIv = thisView.findViewById(R.id.local_music_bottom_ivPlay);
        lastIv = thisView.findViewById(R.id.local_music_bottom_ivLast);
        songImage = thisView.findViewById(R.id.local_music_bottom_ivIcon);

        relativeLayout = thisView.findViewById(R.id.local_music_bottomLayout);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
//        ibSearch.setOnClickListener(this);
        banner = thisView.findViewById(R.id.banner);
        searchView = thisView.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                startActivity(new Intent(getContext(), MusicSearch.class));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getContext(), MusicSearch.class);
                intent.putExtra("key", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        this.thisView = view;
        return view;
    }

    public View getThisView() {
        return thisView;
    }

    @Override
    public void onClick(View v) {
//        resetTab();
        switch (v.getId()) {
            case R.id.local_music_bottom_ivPlay:
                if (firstOpen) {
//                    if (binder.getCurrentPosition() == -1) {
//                        //如果没有音乐在播放
//                        binder.playMusicPosition(null);
//                        setMusicBean(binder.getMusicBean());
//                        playIv.setImageResource(R.mipmap.icon_pause);
//                    } else {
//                        binder.playMusicPosition(binder.getCurrentPosition());
//                        playIv.setImageResource(R.mipmap.icon_next);
//                    }
                    firstOpen = false;
                } else {
                    if (binder.isMusicPlaying()) {
                        //如果音乐正在播放，则暂停
                        binder.pauseMusic();
                        playIv.setImageResource(R.mipmap.icon_play);
                    } else {
                        //如果音乐正在暂停，则播放
                        binder.playMusic();
                        playIv.setImageResource(R.mipmap.icon_pause);
                    }
                }
                setMusicBean(binder.getMusicBean());
                Log.d(TAG, "onClick: ");

                break;
//            case R.id.local_music_bottom_ivLast:
//                binder.playLastMusic();
//                setMusicBean(binder.getMusicBean());
//                break;
//            case R.id.local_music_bottom_ivNext:
//                binder.playNextMusic();
//                setMusicBean(binder.getMusicBean());
//                break;

            case R.id.local_music_bottomLayout:
                Intent currentMusicIntent = new Intent(getActivity(), CurrentPlayMusic.class);
                startActivity(currentMusicIntent);
                break;

        }
    }

    private void initPlayer() {
        //初始化图片数据

        bannerList.add("https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/a6efce1b9d16fdfabf36882ab08f8c5495ee7b9f.jpg");
        bannerList.add("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/0824ab18972bd40797d8db1179899e510fb3093a.jpg");
        bannerList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2647888545,3751969263&fm=224&gp=0.jpg");
        banner.setAdapter(new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.itemView).load(data).apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).into(holder.imageView);
            }
        });
        banner.setIndicator(new CircleIndicator(getContext()));

    }

    public void setMusicBean(MusicBean bean) {
        singerTv.setText(bean.getSinger());
        songTv.setText(bean.getSong());
        MusicUtil.setAlbumImage(songImage, binder.getBitmap());
        if (binder.isMusicPlaying()) {
            playIv.setImageResource(R.mipmap.icon_pause);
        } else {
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (binder != null && binder.getMusicBean() != null) {
            setMusicBean(binder.getMusicBean());
        }
        // 如果SearchView中有焦点且键盘已经展开，则隐藏键盘
        SearchView searchView = getView().findViewById(R.id.searchView); // 替换为你的SearchView的id
        if (searchView != null && searchView.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.musicplayer.PLAY_FINISHED");
        getActivity().registerReceiver(musicPlayFinishedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        SearchView searchView = getView().findViewById(R.id.searchView); // 替换为你的SearchView的id
        if (searchView != null) {
            searchView.clearFocus();
        }
        // 取消注册广播接收器
        getActivity().unregisterReceiver(musicPlayFinishedReceiver);
    }

}
