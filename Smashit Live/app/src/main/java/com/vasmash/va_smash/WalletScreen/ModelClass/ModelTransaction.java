package com.vasmash.va_smash.WalletScreen.ModelClass;

public class ModelTransaction {

    private String name;
    private String date;
    private String balance;
    private String profilepic;
    private String todousername;
    private String todoprofilepic;
    private String type;


    public ModelTransaction(/*String name, String date, String balance,String profilepic*/) {
        this.name = name;
        this.date = date;
        this.balance = balance;
        this.profilepic = profilepic;

        this.todousername = todousername;
        this.todoprofilepic = todoprofilepic;
        this.type = type;


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBalance() {
        return balance;
    }

    public String getProfilepic() {
        return profilepic;
    }
    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }


    public void setBalance(String balance) {
        this.balance = balance;
    }


    public void setTodousername(String todousername) {
        this.todousername = todousername;
    }


    public void setTodoprofilepic(String todoprofilepic) {
        this.todoprofilepic = todoprofilepic;
    }


    public String getTodousername() {
        return todousername;
    }

    public String getTodoprofilepic() {
        return todoprofilepic;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

}
