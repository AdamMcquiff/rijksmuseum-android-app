package com.cet325.bg72db.ExchangeRates.Models;

public class ExchangeRates {

    private String base;
    private double GBP;

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }

    public double getGBP() {
        return GBP;
    }
    public void setGBP(double GBP) {
        this.GBP = GBP;
    }

    @Override
    public String toString() {
        return "BALL" + getBase() + " : " + getGBP();
    }
}
