package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String user;
    private int animal_askNumber,plant_askNumber;
    private String indicateHW;
    private SharedPreferences account, hwSharedPreference;
    private TextView welcomeMessage;
    private MyDbHelper database;
    private List<Item> items_all;
    private TextView animal_ask,plant_ask,animal_task, plant_task, indicate;
    private Button mail, showAll, showOnMap, findNew, searchWiki;
    private DbUpload dbUpload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        database = new MyDbHelper(this, MyDbHelper.TABLE_NAME, null , 1);
        items_all = database.getAll();

        //get user sharedPreference
        account = getSharedPreferences("account", 0);
        user = account.getString("account", "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeaderView= navigationView.getHeaderView(0);
        welcomeMessage = (TextView)navigationHeaderView.findViewById(R.id.user);
        welcomeMessage.setText("帳號 ： " + user);
        welcomeMessage.setTextSize(30);
        welcomeMessage.setTextColor(Color.BLACK);

        animal_ask = (TextView)findViewById(R.id.animal_ask);
        plant_ask = (TextView)findViewById(R.id.plant_ask);
        animal_task = (TextView)findViewById(R.id.animal_task);
        plant_task = (TextView)findViewById(R.id.plant_task);
        indicate = (TextView)findViewById(R.id.indicateHW);

        showAll = (Button)findViewById(R.id.showAll);
        showOnMap = (Button)findViewById(R.id.showOnMap);
        findNew = (Button)findViewById(R.id.findNew);
        searchWiki = (Button)findViewById(R.id.searchWiki);
        mail = (Button)findViewById(R.id.mail);

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items_all.size()>0){
                    Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alertMessage = new AlertDialog.Builder(MainMenuActivity.this);
                    alertMessage.setTitle("提示");
                    alertMessage.setMessage("圖鑒中無任何發現\n趕快發現新事物吧！");
                    alertMessage.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    alertMessage.show();
                }
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items_all.size()>0){
                    Intent intent = new Intent(MainMenuActivity.this, ShowActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alertMessage = new AlertDialog.Builder(MainMenuActivity.this);
                    alertMessage.setTitle("提示");
                    alertMessage.setMessage("圖鑒中無任何發現\n趕快發現新事物吧！");
                    alertMessage.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    alertMessage.show();
                }
            }
        });

        findNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        searchWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"teacher@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, user+"的心得");
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "選擇使用的信箱"));
            }
        });


        getAskNumber();
        sendEmail();




    }

    @Override
    protected void onResume() {

        items_all = database.getAll();
        setText();
        sendEmail();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // nothing to do in action bar


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.showAll: {
                if(items_all.size()>0){
                    Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alertMessage = new AlertDialog.Builder(MainMenuActivity.this);
                    alertMessage.setTitle("提示");
                    alertMessage.setMessage("圖鑒中無任何發現\n趕快發現新事物吧！");
                    alertMessage.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    alertMessage.show();
                }

                break;
            }
            case R.id.showOnMap: {
                if(items_all.size()>0){
                    Intent intent = new Intent(MainMenuActivity.this, ShowActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alertMessage = new AlertDialog.Builder(MainMenuActivity.this);
                    alertMessage.setTitle("提示");
                    alertMessage.setMessage("圖鑒中無任何發現\n趕快發現新事物吧！");
                    alertMessage.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    alertMessage.show();
                }

                break;
            }
            case R.id.findNew: {
                Intent intent = new Intent(MainMenuActivity.this, AddActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.searchWiki:{
                //search on wiki
                Intent intent = new Intent(MainMenuActivity.this, WebActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.observe:{
                //observe other's HW
                Intent intent = new Intent(MainMenuActivity.this, ObserveActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.observeSelf:{
                //observe other's HW
                Intent intent = new Intent(MainMenuActivity.this, IndivisualObserveActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.logout: {
                File prefFile = new File("data/data/com.example.myapplication/shared_prefs/account.xml");
                if (prefFile.exists())
                    prefFile.delete();
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainMenuActivity.this, "再見了 " + user, Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAskNumber(){
        //connect to url if needed
        dbUpload = new DbUpload(MainMenuActivity.this, user);
        String homeworkEcho = dbUpload.getHomework();
        hwSharedPreference = getSharedPreferences("homework",0);

        if(homeworkEcho != null){
            String homework[] = homeworkEcho.split(":");
            animal_askNumber = Integer.valueOf(homework[0]);
            plant_askNumber = Integer.valueOf(homework[1]);
            indicateHW = homework[2];
            hwSharedPreference.edit().putInt("animal_askNumber", animal_askNumber).commit();
            hwSharedPreference.edit().putInt("plant_askNumber", plant_askNumber).commit();
            hwSharedPreference.edit().putString("indicateHW", indicateHW).commit();
            setText();

        }
        else if(hwSharedPreference.getInt("animal_askNumber",-1)!=-1 && hwSharedPreference.getInt("plant_askNumber",-1)!=-1){
            animal_askNumber = hwSharedPreference.getInt("animal_askNumber",-1);
            plant_askNumber = hwSharedPreference.getInt("plant_askNumber",-1);
            indicateHW = hwSharedPreference.getString("indicateHW","無作業敘述");
            Toast.makeText(this, "無法讀取線上作業，將載入上一次更新作業。", Toast.LENGTH_SHORT).show();
            setText();
        }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("警告");
            alert.setMessage("無法讀取任何作業資訊，請開啟網路已進行線上更新。");
            alert.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getAskNumber();
                }
            });
            alert.show();
        }

    }

    private void setText(){
        animal_ask.setText("發現"+animal_askNumber+"種動物");
        plant_ask.setText("發現"+plant_askNumber+"種植物");
        String ANIMAL[] = {"動物","0"};
        String PLANT[] = {"植物","0"};
        animal_task.setText("("+database.getCategoryItemsNotUpload(ANIMAL).size()+"/"+animal_askNumber+")");
        plant_task.setText("("+database.getCategoryItemsNotUpload(PLANT).size()+"/"+plant_askNumber+")");
        indicate.setText("作業詳細資訊："+indicateHW);


        if(database.getCategoryItems(new String[]{"動物"}).size() >= animal_askNumber){
            animal_task.setTextColor(Color.BLUE);
        }
        else
            animal_task.setTextColor(Color.RED);

        if(database.getCategoryItems(new String[]{"植物"}).size() >= plant_askNumber){
            plant_task.setTextColor(Color.BLUE);
        }
        else
            plant_task.setTextColor(Color.RED);
    }

    private void sendEmail(){
        if(Completed()){
            //send Email 心得
            //send to database.
            mail.setVisibility(View.VISIBLE);
        }
    }

    private Boolean Completed(){
        return (database.getCategoryItems(new String[]{"動物"}).size() >= animal_askNumber )&& (database.getCategoryItems(new String[]{"植物"}).size() >= plant_askNumber);
    }
}
