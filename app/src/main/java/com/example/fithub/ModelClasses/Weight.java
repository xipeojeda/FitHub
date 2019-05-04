package com.example.fithub.ModelClasses;

import java.util.Date;

public class Weight {
    private Date date;
    private double weight;

    public Weight()
    {}

    public Weight(Date d, double w)
    {
        date=d;
        weight=w;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate()
    {
        return date;
    }

    public double getWeight()
    {
        return weight;
    }

}
