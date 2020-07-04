package com.vasmash.va_smash.login.ModelClass;

public class Languages {
    private String lang_code;
    private String lang_name;
    private String callingcode;
    private String flag;

    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Languages(String lang_code, String lang_name,String callingcode, String flag) {
        this.lang_code = lang_code;
        this.lang_name = lang_name;
        this.callingcode = callingcode;
        this.flag = flag;

    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getCallingcode() {
        return callingcode;
    }

    public void setCallingcode(String callingcode) {
        this.callingcode = callingcode;
    }

    public String getFlag() {
        return getFlag();
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    @Override
    public String toString() {
        return this.lang_name ;
    }


    public Languages() {
        this.lang_code = lang_code;
        this.lang_name = lang_name;
    }
}
