package com.wemakeprice.wmputilweb;

public class Log {

    private String date;
    private String userCd;

    private String jikmooCd;
    private String jikchakCd;
    private String url;
    private String urlName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserCd() {
        return userCd;
    }

    public void setUserCd(String userCd) {
        this.userCd = userCd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJikmooCd() {
        return jikmooCd;
    }

    public void setJikmooCd(String jikmooCd) {
        this.jikmooCd = jikmooCd;
    }

    public String getJikchakCd() {
        return jikchakCd;
    }

    public void setJikchakCd(String jikchakCd) {
        this.jikchakCd = jikchakCd;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String toString() {
        return "Log [date=" + date + ", userCd=" + userCd + ", jikmooCd=" + jikmooCd + ", url=" + url + "]";
    }

}
