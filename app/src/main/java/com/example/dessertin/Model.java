package com.example.dessertin;

public class Model {
    private int id;
    private String name;
    private String resep;
    private byte[] image;

    public Model(int id, String name, String resep, byte[] image) {
        this.id = id;
        this.name = name;
        this.resep = resep;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResep() {
        return resep;
    }

    public void setResep(String resep) {
        this.resep = resep;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
