package com.vasmash.va_smash.VaContentScreen.ModeClass;

public class Vastore_content_model {
    //    String tags;
    String productname;
    //    String productdescp;
    String productimg;
    String pruducturl;

    public String getPruducturl() {
        return pruducturl;
    }

    public void setPruducturl(String pruducturl) {
        this.pruducturl = pruducturl;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public Vastore_content_model() {
//        this.tags = tags;
        this.productname = productname;
//        this.productdescp = productdescp;
        this.productimg = productimg;
        this.pruducturl = pruducturl;
    }

//    public String getTags() {
//        return tags;
//    }
//
//    public void setTags(String tags) {
//        this.tags = tags;
//    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

//    public String getProductdescp() {
//        return productdescp;
//    }
//
//    public void setProductdescp(String productdescp) {
//        this.productdescp = productdescp;
//    }


}
