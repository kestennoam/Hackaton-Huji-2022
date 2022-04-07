package com.example.hujihackaton47.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Item implements Serializable {
    private final String id;
    private final String name;
    private String image;
    private int price;
    private ArrayList<String> tags;
    private String description;
    private String ownerId;
    private String renterId;


    public Item(String name, String image, int price, ArrayList<String> tags, String description, String ownerId, String renterId){
        this.renterId = renterId;
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.tags = new ArrayList<String>();
        this.description = description;
        this.ownerId = ownerId;
    }

    public Item() {
        this("default item", "image", 100, null, "", "", "");
    }

    public Item(Item o) {
        this.id = o.id;
        this.name = o.name;
        this.image = o.image;
        this.price = o.price;
        this.tags = new ArrayList<String>(tags);
        this.description = o.description;
        this.ownerId = o.ownerId;
        this.renterId = o.renterId;

    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", tags=" + tags +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
