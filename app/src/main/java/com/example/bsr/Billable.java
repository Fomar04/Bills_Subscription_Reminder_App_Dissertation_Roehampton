package com.example.bsr;


public class Billable {

    private String description;
    private String date;
    private String amount;
    private String id;



    public Billable(String s, String description, String date, String amount) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.id = id;

    }

    public Billable( String provider, String date, String amount) {
    }


    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }
}



