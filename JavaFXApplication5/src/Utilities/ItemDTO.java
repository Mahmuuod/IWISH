/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author LENOVO
 */
 

public class ItemDTO {
    private int Item_id;
    private String Name;
    private double Price;
    private String Category;

    public ItemDTO(int Item_id, String Name, double Price, String Category) {
        this.Item_id = Item_id;
        this.Name = Name;
        this.Price = Price;
        this.Category = Category;
    }

   public ItemDTO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setItem_id(int Item_id) {
        this.Item_id = Item_id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }
    

    public int getItemId() {
        return Item_id;
    }

    public String getName() {
        return Name;
    }

    public double getPrice() {
        return Price;
    }

    public String getCategory() {
        return Category;
    }
}

