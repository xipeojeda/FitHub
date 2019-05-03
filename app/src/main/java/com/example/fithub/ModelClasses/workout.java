package com.example.fithub.ModelClasses;

public class workout {
        private String date, type, exercise;
        private int reps;

        public workout()
        {}
//time = exercise
        public workout(String date,String type,String time,int reps)
        {
            this.date = date;
            this.type=type;
            this.exercise=time;
            this.reps=reps;
        }

        public String getDate() {
            return date;
        }

        public String getExercise() {
            return exercise;
        }

        public String getType() {
            return type;
        }

    public int getReps() {
        return reps;
    }

    public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String exercise) {
            this.exercise = exercise;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setReps(int reps) {this.reps=reps;};

    }
