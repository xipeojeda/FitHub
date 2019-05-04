package com.example.fithub.ModelClasses;

/**
 * Class used to to input user info about weight. User will input current weight, and date recorded
 */
public class Weight {
    private String date;
    private double weight;

    /**
     * Instantiates a new Weight.
     */
    public Weight()
    {}

    /**
     * Instantiates a new Weight.
     *
     * @param d the day recorded
     * @param w current weight
     */
    public Weight(String d, double w)
    {
        date=d;
        weight=w;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public double getWeight()
    {
        return weight;
    }

}
