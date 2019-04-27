package com.example.fithub;

public class Weight {
    private String date;
    private double weight;

    public Weight()
    {}

    public Weight(String d, double w)
    {
        date=d;
        weight=w;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate()
    {
        return date;
    }

    public double getWeight()
    {
        return weight;
    }

}
