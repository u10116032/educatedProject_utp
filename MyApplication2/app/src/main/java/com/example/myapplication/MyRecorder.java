package com.example.myapplication;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.photo.Photo;

import java.io.IOException;

/**
 * Created by 瀚磊 on 2016/5/6.
 */
public class MyRecorder {
    private MediaRecorder mediaRecorder = null;
    private String output;
    public double mEMA = 0.0;
    private Handler handler=new Handler();
    private Runnable runnable;

    MyRecorder(String output){
        this.output = output;
    }

    public void start(){
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(output);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }


            mediaRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    public void setRecTime( final TextView timer, final ProgressBar progressBar){


        final long startTime = System.currentTimeMillis();
        runnable = new Runnable() {
            @Override
            public void run() {
                Long spentTime = (System.currentTimeMillis() - startTime)/1000;
                //計算目前已過分鐘數
                Long minius = spentTime / 60;
                //計算目前已過秒數
                Long seconds = spentTime % 60;
                timer.setText(minius + " : " + seconds +" / 02 : 00" );
                progressBar.setProgress(spentTime.intValue());
                handler.postDelayed(runnable, 1000);
            }
        };
        runnable.run();
    }

    public void stopRecTime(){
        handler.removeCallbacks(runnable);
    }
}
