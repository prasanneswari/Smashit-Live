package com.vasmash.va_smash.login.ModelClass;

public class Modelcategory extends Languages {
    private String categoryname;

    public Modelcategory(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
