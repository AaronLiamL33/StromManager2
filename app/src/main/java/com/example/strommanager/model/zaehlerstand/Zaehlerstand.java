package com.example.strommanager.model.zaehlerstand;

import com.google.gson.annotations.SerializedName;

public class Zaehlerstand {

    @SerializedName("id")
    private int id;
    @SerializedName("bezeichnung")
    private String bezeichnung;
    @SerializedName("date")
    private String date;
    @SerializedName("zaehlerstand")
    private int zaehlerstand;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getZaehlerstand() {
        return zaehlerstand;
    }

    public void setZaehlerstand(int zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }





    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", date='" + date + '\'' +
                ", zaehlerstand='" + zaehlerstand + '\'' +
                '}';
    }


}
