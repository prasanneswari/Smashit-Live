package com.vasmash.va_smash.createcontent.Sounds;

public class Sound_modelclass {
    private String sound_code;
    private String sound_name;
    private boolean isSelected;

    public Sound_modelclass() {
        this.sound_code = sound_code;
        this.sound_name = sound_name;
        this.isSelected = isSelected;
    }

    public String getSound_code() {
        return sound_code;
    }

    public void setSound_code(String sound_code) {
        this.sound_code = sound_code;
    }

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
