package com.example.dormmamu.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dormmamu.Converters;

import java.io.Serializable;

@Entity(tableName = "dorms")
@TypeConverters({Converters.class})
public class Dorm implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String location;
    private String formattedPrice;
    private String deposit;
    private String bathroomType;
    private String addressDetail;
    private String contactNumber;
    private int[] imageGallery;
    private double latitude;
    private double longitude;
    private float rating;
    private int priceValue;

    public Dorm(String name, String location, String formattedPrice, String deposit,
                String bathroomType, String addressDetail, String contactNumber,
                int[] imageGallery, double latitude, double longitude,
                float rating, int priceValue) {

        this.name = name;
        this.location = location;
        this.formattedPrice = formattedPrice;
        this.deposit = deposit;
        this.bathroomType = bathroomType;
        this.addressDetail = addressDetail;
        this.contactNumber = contactNumber;
        this.imageGallery = imageGallery;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.priceValue = priceValue;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getFormattedPrice() { return formattedPrice; }
    public String getDeposit() { return deposit; }
    public String getBathroomType() { return bathroomType; }
    public String getAddressDetail() { return addressDetail; }
    public String getContactNumber() { return contactNumber; }
    public int[] getImageGallery() { return imageGallery; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public float getRating() { return rating; }
    public int getPriceValue() { return priceValue; }

    public int getImageResId() {
        if (imageGallery != null && imageGallery.length > 0) return imageGallery[0];
        return 0;
    }
}
