/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author jkira
 */
public class SearchingStocks {
    private String symbol;
    private String companyname;
    private String recenttrade;
    private String highbuy;
    private String lowsell;

    public SearchingStocks(String symbol, String companyname, String recenttrade, String highbuy, String lowsell) {
        this.symbol = symbol;
        this.companyname = companyname;
        this.recenttrade = recenttrade;
        this.highbuy = highbuy;
        this.lowsell = lowsell;
    }

    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getRecenttrade() {
        return recenttrade;
    }

    public void setRecenttrade(String recenttrade) {
        this.recenttrade = recenttrade;
    }

    public String getHighbuy() {
        return highbuy;
    }

    public void setHighbuy(String highbuy) {
        this.highbuy = highbuy;
    }

    public String getLowsell() {
        return lowsell;
    }

    public void setLowsell(String lowsell) {
        this.lowsell = lowsell;
    }
    
    
}
