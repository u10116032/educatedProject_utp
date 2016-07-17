package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;


import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences account ;
    private String user;
    private MyDbHelper database;
    ListView listView;
    private ListAdapter adapter;
    private List<Item> items;
    private static String TAG = "MainActivity";
    private Spinner category_spinner;
    private String category;
    private String[] CATEGORY = {"全部", "動物", "植物", "其他"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new MyDbHelper(this, MyDbHelper.TABLE_NAME, null , 1);
        items = database.getAll();
        category = CATEGORY[0];

        setContentView(R.layout.activity_main);
        account = getSharedPreferences("account",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = account.getString("account", "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //處理listView

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ListAdapter(this, R.layout.single_item, items);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Item item = adapter.getItem(position);
                Intent intent_item = new Intent();
                // 設定記事編號與記事物件
                intent_item.putExtra("position", position);
                intent_item.putExtra("Item", item);
                intent_item.setClass(MainActivity.this, ItemActivity.class);
                startActivity(intent_item);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Item item = adapter.getItem(position);
                final String[] action = {"編輯", "刪除"};

                AlertDialog.Builder list_alert = new AlertDialog.Builder(MainActivity.this);
                list_alert.setTitle(item.getTitle());
                list_alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent_editItem = new Intent("EDIT_ITEM");
                                // 設定記事編號與記事物件
                                intent_editItem.putExtra("position", position);
                                intent_editItem.putExtra("Item", item);
                                intent_editItem.setClass(MainActivity.this, ModifyActivity.class);
                                startActivity(intent_editItem);
                                break;

                            case 1:
                                database.delete(item.getId());
                                items = database.getAll();
                                adapter = new ListAdapter(MainActivity.this, R.layout.single_item, items);
                                listView.setAdapter(adapter);
                                break;
                        }
                    }
                });
                list_alert.show();
                return true;
            }
        });

        //處理spinner
        category_spinner = (Spinner)findViewById(R.id.category_spinner);
        category_spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, CATEGORY));
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = CATEGORY[i];
                switch (i){
                    case 0:{
                        //CATEGORY[0]...全部
                        items = database.getAll();
                        adapter = new ListAdapter(MainActivity.this, R.layout.single_item, items);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case 1:{
                        //CATEGORY[1]...動物
                        String ANIMAL[] = {"動物"};
                        items = database.getCategoryItems(ANIMAL);
                        adapter = new ListAdapter(MainActivity.this, R.layout.single_item, items);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case 2:{
                        //CATEGORY[2]...植物
                        String PLANT[] = {"植物"};
                        items = database.getCategoryItems(PLANT);
                        adapter = new ListAdapter(MainActivity.this, R.layout.single_item, items);
                        listView.setAdapter(adapter);
                        break;
                    }
                    case 3:{
                        //CATEGORY[3]...其他
                        String OTHER[] = {"其他"};
                        items = database.getCategoryItems(OTHER);
                        adapter = new ListAdapter(MainActivity.this, R.layout.single_item, items);
                        listView.setAdapter(adapter);
                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = CATEGORY[0];

            }
        });




    }

    @Override
    protected  void onResume(){

        items = database.getAll();
        adapter = new ListAdapter(this, R.layout.single_item, items);
        listView.setAdapter(adapter);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id ) {

            case R.id.upload:{

                AlertDialog.Builder doubleCheck = new AlertDialog.Builder(MainActivity.this);
                doubleCheck.setTitle("確認訊息");
                doubleCheck.setMessage("確認上傳作業?\n一旦上傳無法更改唷!");
                doubleCheck.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean success = false;
                        for(Item item : items){
                            DbUpload myDbUpload = new DbUpload(getApplicationContext(), user);
                            success = myDbUpload.upLoad(item);
                            item.setUpload(true);
                            database.update(item);
                        }
                        if(success)
                            Toast.makeText(MainActivity.this,"upLoad success !!",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,"upLoad failed !!",Toast.LENGTH_SHORT).show();
                    }
                });
                doubleCheck.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                doubleCheck.show();
            }

        }

        return super.onOptionsItemSelected(menuItem);
    }




}
