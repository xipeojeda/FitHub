package com.example.fithub.SensorClasses;
//Uses teh accelerometer sensor and deploys SensorFilter to detect a step
public class StepDetector {
    private static final int ACCEL_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;

    // change this threshold according to your sensitivity preferences
    private static final float STEP_THRESHOLD = 50f;

    private static final int STEP_DELAY_NS = 250000000;
    //class variables
    private int accelRingCounter = 0;
    private float[] accelRingX = new float[ACCEL_RING_SIZE];
    private float[] accelRingY = new float[ACCEL_RING_SIZE];
    private float[] accelRingZ = new float[ACCEL_RING_SIZE];
    private int velRingCounter = 0;
    private float[] velRing = new float[VEL_RING_SIZE];
    private long lastStepTimeNs = 0;
    private float oldVelocityEstimate = 0;

    private StepListener listener;
    //registers a user listener
    public void registerListener(StepListener listener) {
        this.listener = listener;
    }


    public void updateAccel(long timeNs, float x, float y, float z) {//takes in steps for three dimensions
        float[] currentAccel = new float[3];
        currentAccel[0] = x;//x array values
        currentAccel[1] = y;//y array values
        currentAccel[2] = z;//z array values

        // First step is to update our guess of where the global z vector is.
        accelRingCounter++;
        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

        float[] worldZ = new float[3];
        worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
        worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
        worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

        float normalization_factor = SensorFilter.norm(worldZ);// normalizes the data in the array

        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;

        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;//Dot product world z data and current accel data and subtracts them by a normalization factor
        velRingCounter++;
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

        float velocityEstimate = SensorFilter.sum(velRing);// estimates velocity by summing the velRing

        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD //determines what is a step or not
                && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        oldVelocityEstimate = velocityEstimate;
    }
}
