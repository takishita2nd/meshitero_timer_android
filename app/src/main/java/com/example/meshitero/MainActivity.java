package com.example.meshitero;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private DownloadTask task = null;
    private ImageView imageView = null;
    private Timer timer = null;
    private String url = "https://taki-lab.site/meshitero/img/time_%02d_%02d.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 60000);
    }

    @Override
    protected void onDestroy() {
        task.setListener(null);
        super.onDestroy();
    }

    private DownloadTask.Listener createListener() {
        return new DownloadTask.Listener() {
            @Override
            public void onSuccess(Bitmap bmp) {
                imageView.setImageBitmap(bmp);
            }
        };
    }

    private String getTimer() {
        Calendar cTime = Calendar.getInstance();
        int min = cTime.get(Calendar.MINUTE);
        if(min < 30) {
            min = 0;
        } else {
            min = 30;
        }
        Log.d("debug",String.format(url, cTime.get(Calendar.HOUR_OF_DAY), min));
        return String.format(url, cTime.get(Calendar.HOUR_OF_DAY), min);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            task = new DownloadTask();
            task.setListener(createListener());
            task.execute(getTimer());
        }
    }
}
