package com.example.fithub.SensorClasses;
//interface listens to alerts abouts steps being detected
public interface StepListener {
    //every class implementing StepListener must implement the following method
    public void step(long timeNs);
}
