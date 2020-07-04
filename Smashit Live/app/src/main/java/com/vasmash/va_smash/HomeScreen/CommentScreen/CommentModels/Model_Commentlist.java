package com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels;

import java.util.ArrayList;

public class Model_Commentlist {

    private String name;
    private String content;
    private String profile;
   private String userid;
    private String commentid;
    private String likecount;
    private String liketype;
    private String isowner;
    private String postid;

    public ArrayList<Model_replycomment> getReplymodel() {
        return replymodel;
    }

    public void setReplymodel(ArrayList<Model_replycomment> replymodel) {
        this.replymodel = replymodel;
    }

    ArrayList<Model_replycomment> replymodel;



    public Model_Commentlist(/*String name, String content*//*, int profile*/) {
        this.name = name;
        this.content = content;
        this.userid = userid;
        this.commentid = commentid;
          this.profile = profile;
        this.likecount = likecount;
        this.liketype=liketype;
        this.isowner=isowner;
        this.postid=postid;
        this.replymodel=replymodel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String  profile) {
        this.profile = profile;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String  likecount) {
        this.likecount = likecount;
    }


    public String getLiketype() {
        return liketype;
    }

    public void setLiketype(String liketype) {
        this.liketype = liketype;
    }

    public String getIsowner() {
        return isowner;
    }

    public void setIsowner(String isowner) {
        this.isowner = isowner;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

}
