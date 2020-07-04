package com.vasmash.va_smash.SearchClass.ModelClass;

public class Model_Searchlatest {

    private String hashtagsname;
    private String hashtagsviews;

    private String songname;
    private String songduration;
    private String songusername;
    private String songviews;
    private String songurl;
    private String songid;

    private boolean isSelected;


    public Model_Searchlatest() {
        this.hashtagsname = hashtagsname;
        this.hashtagsviews = hashtagsviews;
        this.songname = songname;
        this.songduration = songduration;
        this.songusername = songusername;
        this.songviews=songviews;
        this.songurl=songurl;
        this.isSelected=isSelected;
        this.songid=songid;
    }

    public String getHashtagsname() {
        return hashtagsname;
    }

    public void setHashtagsname(String hashtagsname) {
        this.hashtagsname = hashtagsname;
    }

    public String getHashtagsviews() {
        return hashtagsviews;
    }

    public void setHashtagsviews(String hashtagsviews) {
        this.hashtagsviews = hashtagsviews;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongduration() {
        return songduration;
    }

    public void setSongduration(String songduration) {
        this.songduration = songduration;
    }

    public String getSongusername() {
        return songusername;
    }

    public void setSongusername(String songusername) {
        this.songusername = songusername;
    }


    public String getSongviews() {
        return songviews;
    }

    public void setSongviews(String songviews) {
        this.songviews = songviews;
    }

    public String getSongurl() {
        return songurl;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }


}
