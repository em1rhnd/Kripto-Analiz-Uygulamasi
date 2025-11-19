package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Coin {


    private String id;

    private String name;
    private String symbol;

    @SerializedName("current_price")
    private double price;

    @SerializedName("price_change_percentage_24h")
    private double change;

    @SerializedName("image")
    private String logoUrl;


    private boolean isFavorite = false;


    public Coin() {}

    public Coin(String id, String name, String symbol, double price, double change, String logoUrl) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.logoUrl = logoUrl;
    }



    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange() { return change; }
    public String getLogoUrl() { return logoUrl; }

    public void setName(String name) { this.name = name; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setPrice(double price) { this.price = price; }
    public void setChange(double change) { this.change = change; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }


    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }
}
