package com.vasmash.va_smash.createcontent.Sounds;

public class Sound_catemodel {

    private String sound_cate;
    private String sound_code;
    private boolean isSelected;

    public Sound_catemodel(String sound_cate, String sound_code, boolean isSelected) {
        this.sound_cate = sound_cate;
        this.sound_code = sound_code;
        this.isSelected = isSelected;
    }

    public Sound_catemodel() {

    }

    public String getSound_cate() {
        return sound_cate;
    }

    public void setSound_cate(String sound_cate) {
        this.sound_cate = sound_cate;
    }

    public String getSound_code() {
        return sound_code;
    }

    public void setSound_code(String sound_code) {
        this.sound_code = sound_code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
