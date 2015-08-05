package com.imooc.weixin6_0.net;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by apple on 15/7/27.
 */
public class JokeListData {
    private String content;
    private String hashId;
    private String unixtime;
    private String updatetime;
    private String url;

    public JokeListData() {
    }

    public JokeListData(String content, String url, String unixtime, String hashId, String updatetime) {
        this.content = content;
        this.url = url;
        this.unixtime = unixtime;
        this.hashId = hashId;
        this.updatetime = updatetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(String unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
