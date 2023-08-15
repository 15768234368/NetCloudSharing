package com.example.netcloudsharing.Bean;

import java.util.List;

public class RankSetBean {
    String rankListUrl;
    String rankListPic;
    List<String> rankListPreThree;

    public RankSetBean(String rankListUrl, String rankListPic, List<String> rankListPreThree) {
        this.rankListUrl = rankListUrl;
        this.rankListPic = rankListPic;
        this.rankListPreThree = rankListPreThree;
    }

    public void setRankListUrl(String rankListUrl) {
        this.rankListUrl = rankListUrl;
    }

    public void setRankListPic(String rankListPic) {
        this.rankListPic = rankListPic;
    }

    public void setRankListPreThree(List<String> rankListPreThree) {
        this.rankListPreThree = rankListPreThree;
    }

    public String getRankListUrl() {
        return rankListUrl;
    }

    public String getRankListPic() {
        return rankListPic;
    }

    public List<String> getRankListPreThree() {
        return rankListPreThree;
    }
}
