package com.example.jacaranda.Modle;

public class BusinessPartner {
    private String name;
    private String address;
    private String distance;
    private String image;

    public BusinessPartner(String name, String address, String distance, String image) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.image = image;
    }

    public String getName(){ return name; }

    public String getAddress(){ return address; }

    public String getDistance(){ return distance; }

    public String getImage(){ return image; }
}
