package com.example.fithub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mProgress = (ProgressBar) findViewById(R.id.progressbar_splash);

        new Thread(new Runnable(){
            public void run(){
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork(){
        for (int progress = 0; progress < 100; progress += 20){
            try{
                Thread.sleep(500);
                mProgress.setProgress(progress);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void startApp(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}
