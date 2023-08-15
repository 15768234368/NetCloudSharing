package com.example.netcloudsharing.tool;

import android.util.Log;

import com.example.netcloudsharing.Bean.MusicSetBean;
import com.example.netcloudsharing.Bean.RankSetBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {
    private static final String TAG = "HtmlParser";

    public static List<MusicSetBean> parseHtmlForMusicList(String html) {
        List<MusicSetBean> mData = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        // 选择<body>下的<div class="mainPage">
        Element mainPageElem = doc.selectFirst("body > div.mainPage");

        // 选择<div class="content">下的<div class="homep_d1">下的<div class=homep_d1_d1>
        Element homepD1D1Elem = mainPageElem.selectFirst("div.content > div.homep_d1 > div.homep_d1_d1");

        // 选择<div class="homep_cm_title">下的<div class="homep_d1_d1_d1">
        Element homepD1D1D2Elem = doc.selectFirst("div.homep_d1_d1_d2");
        String songlistUrl1 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a1").get(0).attr("href");
        String imageUrl1 = homepD1D1D2Elem.select("img.homep_cm_item_st1_i1").get(0).attr("_src");
        String title1 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a2").get(0).text();
        mData.add(new MusicSetBean(imageUrl1, title1, songlistUrl1));
        String songlistUrl2 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a1").get(1).attr("href");
        String imageUrl2 = homepD1D1D2Elem.select("img.homep_cm_item_st1_i1").get(1).attr("_src");
        String title2 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a2").get(1).text();
        mData.add(new MusicSetBean(imageUrl2, title2, songlistUrl2));
        String songlistUrl3 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a1").get(2).attr("href");
        String imageUrl3 = homepD1D1D2Elem.select("img.homep_cm_item_st1_i1").get(2).attr("_src");
        String title3 = homepD1D1D2Elem.select("a.homep_cm_item_st1_a2").get(2).text();
        mData.add(new MusicSetBean(imageUrl3, title3, songlistUrl3));
        return mData;
//        Log.d(TAG, "parseHtml: " + songlistUrl1);
//        Log.d(TAG, "parseHtml: " + imageUrl1);
//        Log.d(TAG, "parseHtml: " + title1);
//        Log.d(TAG, "parseHtml: " + songlistUrl2);
//        Log.d(TAG, "parseHtml: " + imageUrl2);
//        Log.d(TAG, "parseHtml: " + title2);
//        Log.d(TAG, "parseHtml: " + songlistUrl3);
//        Log.d(TAG, "parseHtml: " + imageUrl3);
//        Log.d(TAG, "parseHtml: " + title3);
    }
    public static List<RankSetBean> parseHtmlForMusicRank(String html){
        List<RankSetBean> rankSetBeans = new ArrayList<>();
        // 使用Jsoup解析HTML
        Document document = Jsoup.parse(html);

        // 获取所有的<a>元素，这些元素包含了每个项目的链接
        Elements links = document.select("a.homep_d1_d2_d1_a1");

        for (Element link : links) {
            // 获取链接
            String linkHref = link.attr("href");

            // 获取图片链接
            Element imgElement = link.selectFirst("img");
            String imgSrc = imgElement.attr("src");
            List<String> text= new ArrayList<>();
            // 获取文字信息
            Elements textElements = link.select("span.homep_d1_d2_d1_d1_sp2");
            for (Element textElement : textElements) {
                text.add(textElement.text());
            }
            rankSetBeans.add(new RankSetBean(linkHref, imgSrc, text));
        }
        return  rankSetBeans;
    }

    public static List<String> parseHtmlByMusicList(String html){
        List<String> validSongHrefList = null;
        try{
            List<String> songHrefList = new ArrayList<>();
            Document document = Jsoup.parse(html);
            // 使用 selectFirst 方法筛选出 class 为 list1 的元素
            Element list1Element = document.selectFirst(".list1");
//        Log.d(TAG, "parseHtmlByMusicList: " + list1Element);
            // 查找所有包含歌曲链接的<a>标签
            Elements linkElements = list1Element.select("a[href]");

            // 提取歌曲链接
            for (Element linkElement : linkElements) {
                String songHref = linkElement.attr("href");
                songHrefList.add(songHref);
            }
            validSongHrefList = new ArrayList<>();
            for (String href : songHrefList) {
                if (href.startsWith("http")) {
                    validSongHrefList.add(href);
                }
            }
        }catch (Exception e){
            return null;
        }

//        Log.d(TAG, "parseHtmlByMusicList: " + validSongHrefList.size());
        return validSongHrefList;
    }
}
