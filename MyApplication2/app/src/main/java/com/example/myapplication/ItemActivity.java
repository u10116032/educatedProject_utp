package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

public class ItemActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private Item item;
    private String title;
    private String date;
    private String content;
    private double longitude,latitude;
    private String photo_dir=null;
    private String record_dir=null;
    private TextView  TIME, DATE, CONTENT, CATEGORY;
    private ImageButton PLAY, PAUSE, STOP;
    private ImageView PHOTO;
    private ProgressBar PROGRESSBAR;
    private MediaPlayer mediaPlayer;
    private Handler handler=new Handler();
    private Runnable runnable;
    private int minute=0, second=0;
    private Button finish,modify;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_show);
        Intent intent = getIntent();
        item = (Item)intent.getExtras().getSerializable("Item");

        title = item.getTitle();
        date = item.getDate();
        content = item.getContent();
        longitude = item.getLongitude();
        latitude = item.getLatitude();
        photo_dir = item.getPhotodir();
        record_dir = item.getRecorddir();
        setTitle(title);
        if(record_dir !=null){
            mediaPlayer = MediaPlayer.create(ItemActivity.this, Uri.fromFile(new File(record_dir)));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer = mp;
                    mediaPlayer.seekTo(0);
                    PROGRESSBAR.setProgress(0);
                }
            });
            minute = (mediaPlayer.getDuration()/1000)/60;
            second = (mediaPlayer.getDuration()/1000)%60;
        }
        setupView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        title = item.getTitle();
        date = item.getDate();
        content = item.getContent();
        longitude = item.getLongitude();
        latitude = item.getLatitude();
        photo_dir = item.getPhotodir();
        record_dir = item.getRecorddir();
        setTitle(title);
        if(record_dir !=null){
            mediaPlayer = MediaPlayer.create(ItemActivity.this, Uri.fromFile(new File(record_dir)));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer = mp;
                    mediaPlayer.seekTo(0);
                    PROGRESSBAR.setProgress(0);
                }
            });
            minute = (mediaPlayer.getDuration()/1000)/60;
            second = (mediaPlayer.getDuration()/1000)%60;
        }


        setupView();
    }

    private void setupView(){

        DATE = (TextView)findViewById(R.id.date);
        CONTENT = (TextView)findViewById(R.id.content);
        TIME = (TextView)findViewById(R.id.rec_time);
        CATEGORY = (TextView)findViewById(R.id.category);
        PLAY = (ImageButton)findViewById(R.id.play);
        PAUSE = (ImageButton)findViewById(R.id.pause);
        STOP = (ImageButton)findViewById(R.id.stop);
        PHOTO = (ImageView)findViewById(R.id.photo);
        PROGRESSBAR = (ProgressBar)findViewById(R.id.progressBar);
        finish = (Button)findViewById(R.id.button2);
        modify = (Button)findViewById(R.id.modify);



        DATE.setText(date);
        CONTENT.setText(content);
        CATEGORY.setText(item.getCategory());
        TIME.setText("0 : 0 / "+ minute+" : "+second);
        PLAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        PAUSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });
        STOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_editItem = new Intent("EDIT_ITEM");
                // 設定記事編號與記事物件
                intent_editItem.putExtra("Item", item);
                intent_editItem.setClass(ItemActivity.this, ModifyActivity.class);
                startActivity(intent_editItem);
            }
        });
        if(record_dir!=null){PROGRESSBAR.setMax(mediaPlayer.getDuration());}

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(item.getPhotodir(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/5, bitmap.getHeight()/5, true);
        PHOTO.setImageBitmap(bitmap);
    }

    private void play(){

        if(!mediaPlayer.isPlaying() && mediaPlayer != null){
             if(mediaPlayer.getCurrentPosition() != 0){
                 mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
             }
             setProgressbar();
             mediaPlayer.start();
        }

    }

    private void pause(){
        mediaPlayer.pause();
        handler.removeCallbacks(runnable);
    }

    private void stop(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer=null;
            mediaPlayer = MediaPlayer.create(ItemActivity.this, Uri.fromFile(new File(record_dir)));
            handler.removeCallbacks(runnable);
            PROGRESSBAR.setProgress(0);
            TIME.setText("0 : 0 / "+minute+" : "+second);
        }
    }

    private void setProgressbar(){
        runnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = (mediaPlayer.getCurrentPosition() / 1000) / 60+" : "+(mediaPlayer.getCurrentPosition() / 1000) % 60;
                TIME.setText(currentTime + " / "+minute+" : "+second);
                PROGRESSBAR.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(runnable, 1000);
            }
        };
        runnable.run();
    }



}
