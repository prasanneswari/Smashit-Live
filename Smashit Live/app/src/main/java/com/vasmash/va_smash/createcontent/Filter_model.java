package com.vasmash.va_smash.createcontent;

public class Filter_model {
    private String filtername;
    private int filterimage;

    public Filter_model(String filtername, int filterimage) {
        this.filtername = filtername;
        this.filterimage = filterimage;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public int getFilterimage() {
        return filterimage;
    }

    public void setFilterimage(int filterimage) {
        this.filterimage = filterimage;
    }
}
