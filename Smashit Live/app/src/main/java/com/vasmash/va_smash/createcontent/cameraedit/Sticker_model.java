package com.vasmash.va_smash.createcontent.cameraedit;

public class Sticker_model {

    private String stickerurl;
    private String sticker_code;
    private boolean isSelected;

    public Sticker_model(String stickerurl, String sticker_code, boolean isSelected) {
        this.stickerurl = stickerurl;
        this.sticker_code = sticker_code;
        this.isSelected = isSelected;
    }

    public Sticker_model() {

    }

    public String getStickerurl() {
        return stickerurl;
    }

    public void setStickerurl(String stickerurl) {
        this.stickerurl = stickerurl;
    }

    public String getSticker_code() {
        return sticker_code;
    }

    public void setSticker_code(String sticker_code) {
        this.sticker_code = sticker_code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
