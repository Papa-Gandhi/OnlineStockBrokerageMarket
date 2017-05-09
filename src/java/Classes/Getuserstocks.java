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
public class Getuserstocks {
    private String symbol;
    private String num_of_shares;

    public Getuserstocks(String symbol, String num_of_shares) {
        this.symbol = symbol;
        this.num_of_shares = num_of_shares;
    }
    
    

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNum_of_shares() {
        return num_of_shares;
    }

    public void setNum_of_shares(String num_of_shares) {
        this.num_of_shares = num_of_shares;
    }
    
}
