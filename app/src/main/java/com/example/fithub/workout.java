package com.example.fithub;

public class workout {
        private String date, type, time;

        public workout(String date,String type,String time)
        {
            this.date = date;
            this.type=type;
            this.time=time;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
