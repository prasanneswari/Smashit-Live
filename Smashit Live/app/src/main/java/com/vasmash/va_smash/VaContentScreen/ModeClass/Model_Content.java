package com.vasmash.va_smash.VaContentScreen.ModeClass;

public class Model_Content {
    private int profileimg;
    private String contentname;
    private String contenttime;
    private String contentdata;
    private String contentimage;
    private String contentlike;
    private String contentcatogry;


    public Model_Content(/*int profileimg,*/ String contentname, String contenttime,String contentcatogryS/*, String contentdata*/, String contentimage/*, String contentlike*/) {
        this.profileimg = profileimg;
        this.contentname = contentname;
        this.contenttime = contenttime;
        this.contentdata = contentdata;
        this.contentimage = contentimage;
        this.contentcatogry=contentcatogryS;
      //  this.contentlike = contentlike;
    }

    public int getProfileimg() {
        return profileimg;
    }

    public String getContentname() {
        return contentname;
    }
    public String getContentcatogry() {
        return contentcatogry;
    }

    public String getContenttime() {
        return contenttime;
    }

    public String getContentdata() {
        return contentdata;
    }

    public String getContentimage() {
        return contentimage;
    }

    public String getContentlike() {
        return contentlike;
    }

    public void setProfileimg(int profileimg) {
        this.profileimg = profileimg;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public void setContenttime(String contenttime) {
        this.contenttime = contenttime;
    }

    public void setContentdata(String contentdata) {
        this.contentdata = contentdata;
    }

    public void setContentimage(String contentimage) {
        this.contentimage = contentimage;
    }

    public void setContentlike(String contentlike) {
        this.contentlike = contentlike;
    }
}
