package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecActivity extends AppCompatActivity {

    private File recordfile;
    private  MyRecorder myRecorder;
    private TextView timer;
    private ProgressBar progressBar;
    private ImageButton recButtom;
    private Boolean pressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);


        if (ActivityCompat.checkSelfPermission(RecActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String recordFileName = "AUDIO_" + timeStamp + ".mp3";
        File recordpath = new File(Environment.getExternalStorageDirectory().getPath() + "/MyApplication/Record");
        if (!recordpath.exists()) {
            recordpath.mkdir();
        }

        recordfile = new File(recordpath.getPath() + "/" + recordFileName);
        myRecorder = new MyRecorder(recordfile.getAbsolutePath());

        final View recView = LayoutInflater.from(RecActivity.this).inflate(R.layout.my_rec, null);
        final AlertDialog.Builder recordBuilder = new AlertDialog.Builder(RecActivity.this);
        recordBuilder.setView(recView);
        recordBuilder.setTitle("錄音");
        timer = (TextView) recView.findViewById(R.id.timer);
        progressBar = (ProgressBar) recView.findViewById(R.id.progressBar);
        progressBar.setMax(120);
        progressBar.setProgress(0);

        recButtom = (ImageButton) recView.findViewById(R.id.recButton);
        recButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pressed) {
                    pressed = !pressed;
                    recButtom.setBackgroundResource(R.drawable.micing);
                    myRecorder.start();
                    myRecorder.setRecTime(timer, progressBar);
                } else {
                    pressed = !pressed;
                    recButtom.setBackgroundResource(R.drawable.mic);
                    try {
                        myRecorder.stop();
                        myRecorder.stopRecTime();
                    } catch (RuntimeException e) {
                        Toast.makeText(RecActivity.this, "錄音時間過短", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recordBuilder.setPositiveButton("確定", null);
        recordBuilder.setNegativeButton("取消", null);
        AlertDialog recAlert = recordBuilder.create();
        recAlert.show();
        Button yes = recAlert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button no = recAlert.getButton(DialogInterface.BUTTON_NEGATIVE);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pressed) {
                    Toast.makeText(RecActivity.this, "正在錄音，請勿關閉。", Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();
                    data.putExtra("REC_DIR", recordfile.getAbsolutePath());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pressed) {
                    Toast.makeText(RecActivity.this, "正在錄音，請勿關閉。", Toast.LENGTH_SHORT).show();
                } else {
                    recordfile.delete();
                    finish();
                }
            }
        });

    }
}
