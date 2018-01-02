package com.cet325.bg72db.ExchangeRates.Models;

/**
 * Class to hold the exchange rates for a set of given currencies. In this case, it holds the
 * Great British Pound (GDP) and Euro (EUR) serves as the base currency.
 */
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

}
