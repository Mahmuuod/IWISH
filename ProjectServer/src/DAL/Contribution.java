/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Date;
import java.util.logging.Logger;



/**
 *
 * @author osama
 */
public class Contribution {
    
    private int Contribution_id;
    private int Wish_id;
    private Double Contribution_amount;
    private int Contributor_id;

    public Contribution(int Contribution_id, int Wish_id, Double Contribution_amount, int Contributor_id) {
        this.Contribution_id = Contribution_id;
        this.Wish_id = Wish_id;
        this.Contribution_amount = Contribution_amount;
        this.Contributor_id = Contributor_id;
    }
    
    public Contribution(int Wish_id, Double Contribution_amount, int Contributor_id) {
        this.Wish_id = Wish_id;
        this.Contribution_amount = Contribution_amount;
        this.Contributor_id = Contributor_id;
    }
    
    @Override
    public String toString() {
        return "{" + "Contribution_id:" + Contribution_id + ", Wish_id:" + Wish_id + ", Contribution_amount:" + Contribution_amount + ", Contributor_id:" + Contributor_id + "}";
    }

    public int getContribution_id() {
        return Contribution_id;
    }

    public void setContribution_id(int Contribution_id) {
        this.Contribution_id = Contribution_id;
    }

    public void setWish_id(int Wish_id) {
        this.Wish_id = Wish_id;
    }

    public void setContribution_amount(Double Contribution_amount) {
        this.Contribution_amount = Contribution_amount;
    }

    public void setContributor_id(int Contributor_id) {
        this.Contributor_id = Contributor_id;
    }
    
    
    public int getWish_id() {
        return Wish_id;
    }

    public Double getContribution_amount() {
        return Contribution_amount;
    }

    public int getContributor_id() {
        return Contributor_id;
    }
    
    
   

}