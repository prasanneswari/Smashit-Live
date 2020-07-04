package com.vasmash.va_smash.WalletScreen.ModelClass;

public class Model_Wallet {

    private String balance;
    private String amount;


    public Model_Wallet(String balance,String amount) {
        this.balance = balance;
        this.amount=amount;
    }

    public String getBalance() {
        return balance;
    }
    public String getAmount() {
        return amount;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
