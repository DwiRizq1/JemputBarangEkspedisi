package com.example.jemputbarangekspedisi.model;

public class User {
    private String id, nama, barang, berat;

    public User(){

    }

    public User(String namaPengirim, String namaBarang, String berat){
        this.nama = namaPengirim;
        this.barang = namaBarang;
        this.berat = berat;

    }

    public String getId(){
        return id;
    }

    public void setId(String id){

        this.id = id;
    }
    public String getNama(){

        return nama;
    }
    public void setNama(String nama){

        this.nama = nama;
    }
    public String getBarang(){

        return barang;
    }
    public void setBarang(String barang){

        this.barang = barang;
    }
    public String getBerat(){
        return berat;
    }
    public void setBerat(String berat){

        this.berat = berat;
    }

}
