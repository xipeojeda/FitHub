package com.example.fithub.ModelClasses;

/**
 * Class used to to input user info about weight. User will input current weight, and date recorded
 */
public class Weight {
    private double weight;

    /**
     * Instantiates a new Weight.
     */
    public Weight()
    {}

    /**
     * Instantiates a new Weight.
     * @param w current weight
     */
    public Weight(double w)
    {
        weight=w;
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
     * Gets weight.
     *
     * @return the weight
     */
    public double getWeight()
    {
        return weight;
    }

}
