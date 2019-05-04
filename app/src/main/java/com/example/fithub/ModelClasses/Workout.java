package com.example.fithub.ModelClasses;

/**
 * The type Workout.
 */
public class Workout {
        private String date, type, exercise;
        private int reps;

    /**
     * Instantiates a new Workout.
     */
    public Workout()
        {}

    /**
     * Instantiates a new Workout.
     *
     * @param date the date
     * @param type the type
     * @param time the exercise
     * @param reps the reps
     */
//time = exercise
        public Workout(String date, String type, String time, int reps)
        {
            this.date = date;
            this.type=type;
            this.exercise=time;
            this.reps=reps;
        }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
            return date;
        }

    /**
     * Gets exercise.
     *
     * @return the exercise
     */
    public String getExercise() {
            return exercise;
        }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
            return type;
        }

    /**
     * Gets reps.
     *
     * @return the reps
     */
    public int getReps() {
        return reps;
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
     * Sets exercise.
     *
     * @param exercise the exercise
     */
    public void setTime(String exercise) {
            this.exercise = exercise;
        }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
            this.type = type;
        }

    /**
     * Sets reps.
     *
     * @param reps the reps
     */
    public void setReps(int reps) {this.reps=reps;};

    }
