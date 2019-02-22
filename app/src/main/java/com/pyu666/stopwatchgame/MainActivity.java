package com.pyu666.stopwatchgame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable, View.OnClickListener {

    private long startTime;
    private long diffTime;
    private TextView timerText;
    private TextView goalTimerText;
    private TextView diffTimeText;
    private ImageView img;
    private Button startButton;
    private Handler handler = new Handler();
    private String diffTimeLong;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);
    private volatile boolean stopRun = false;
    private long goalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.tx_timer);
        timerText.setText(dateFormat.format(0));
        goalTimerText = findViewById(R.id.tx_goaltime);
        this.setGoalTime();
        goalTimerText.setText(dateFormat.format(goalTime));
        diffTimeText = findViewById(R.id.tx_diff);
        diffTimeText.setText("");
        startButton = findViewById(R.id.bt_button);
        startButton.setOnClickListener(this);
        img = findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        Thread thread;
        String buttonText = startButton.getText().toString();
        System.out.println(buttonText);
        if (buttonText.equals("START")) {
            stopRun = false;
            thread = new Thread(this);
            thread.start();
            startTime = System.currentTimeMillis();
            startButton.setText("STOP");
            timerText.setText("？？");

        } else if (buttonText.equals("STOP")) {
            String differ = diffTimeLong;
            stopRun = true;
            System.out.println(differ);
            timerText.setText(differ);
            this.setDiffTime();
            startButton.setText("RESET");

        } else {
            timerText.setText(dateFormat.format(0));
            startButton.setText("START");
            this.setGoalTime();
            diffTimeText.setText("");
            img.setImageResource(R.drawable.sd_c);
        }
    }

    public void setGoalTime() {
        Random random = new Random();
        goalTime = (3 + random.nextInt(3)) * 1000;
        goalTimerText.setText(dateFormat.format(goalTime));
    }

    public void setDiffTime() {
        long diff = goalTime - diffTime;
        System.out.println(goalTime + " " + diffTime + " " + diff);
        if (diff < 0) {
            diff = diffTime - goalTime;
            diffTimeText.setText("-" + dateFormat.format(diff));
        } else {
            diffTimeText.setText(dateFormat.format(diff));
        }
        if (diff < 100) {
            img.setImageResource(R.drawable.sd_a);
        } else if (diff < 700) {
            img.setImageResource(R.drawable.sd_b);
        } else if (diff < 1500) {
            img.setImageResource(R.drawable.sd_d);
        } else {
            img.setImageResource(R.drawable.sd_e);
        }
    }

    @Override
    public void run() {
        // 10 msec order
        int period = 1;
        while (!stopRun) {
            // sleep: period msec
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopRun = true;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long endTime = System.currentTimeMillis();
                    // カウント時間 = 経過時間 - 開始時間
                    diffTime = (endTime - startTime);
                    //System.out.println(diffTime);
                    diffTimeLong = dateFormat.format(diffTime);
                    //timerText.setText(diffTimeLong);

                }
            });
        }
    }
}
