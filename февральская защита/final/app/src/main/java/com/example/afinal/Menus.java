package com.example.afinal;

import android.graphics.drawable.Drawable;

public class Menus {

    private String docId, Name, Dish, Metro, Website, Price, Dish_image, Ntype;

    public Menus (String docId, String Name, String Dish, String Metro, String Website, String Price, String Dish_image, String Ntype) {
        this.docId = docId;
        this.Name = Name;
        this.Dish = Dish;
        this.Metro = Metro;
        this.Website = Website;
        this.Price = Price;
        this.Dish_image = Dish_image;
        this.Ntype = Ntype;
    }

    public void setMenu (Menus menu) {
        this.docId = menu.docId;
        this.Name = menu.Name;
        this.Dish = menu.Dish;
        this.Metro = menu.Metro;
        this.Website = menu.Website;
        this.Price = menu.Price;
        this.Dish_image = menu.Dish_image;
        this.Ntype = menu.Ntype;
    }

    public Menus() { }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDish() {
        return Dish;
    }

    public void setDish(String Dish) {
        this.Dish = Dish;
    }

    public String getMetro() {
        return Metro;
    }

    public void setMetro(String Metro) {
        this.Metro = Metro;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public Drawable getDish_image() {
        return Drawable.createFromPath(Dish_image);
    }

    public void setDish_image(String Dish_image) {
        this.Dish_image = Dish_image;
    }

    public String getNtype() {
        return Ntype;
    }

    public void setNtype(String Ntype) {
        this.Ntype = Ntype;
    }


}
