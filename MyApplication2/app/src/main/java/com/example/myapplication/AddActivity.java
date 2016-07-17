package com.example.myapplication;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity {

    EditText ed1,ed2,ed3;
    Item item;
    double longitude=0,latitude=0;
    String photo_dir=null;
    String record_dir=null;
    private static final int PHOTO_DIR = 0;
    private static final int LOCATION = 1;
    private static final int RECORD_DIR = 2;
    private Button  cam,rec,loc,add,cancle;
    private Spinner category_spinner;
    private String[] CATEGORY = { "動物", "植物", "其他"};
    private String category;


    private MyDbHelper database ;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        category = CATEGORY[0];
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_DIR:
                    photo_dir = data.getExtras().getString("photo_dir");
                    break;
                case  LOCATION:
                    latitude = data.getExtras().getDouble("LATITUDE");
                    longitude = data.getExtras().getDouble("LONGITUDE");
                    break;
                case RECORD_DIR:
                    record_dir = data.getExtras().getString("REC_DIR");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);
        database = new MyDbHelper(AddActivity.this, MyDbHelper.TABLE_NAME, null , 1);

        cam = (Button)findViewById(R.id.camera);
        rec = (Button)findViewById(R.id.record);
        loc = (Button)findViewById(R.id.location);
        add = (Button)findViewById(R.id.add);
        cancle = (Button)findViewById(R.id.cancle);

        ed1 = (EditText)findViewById(R.id.editText1); //title
        ed2 = (EditText)findViewById(R.id.editText2); //date
        ed3 = (EditText)findViewById(R.id.editText3); //content

        category_spinner = (Spinner)findViewById(R.id.category_spinner) ;


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().isEmpty() || ed2.getText().toString().isEmpty() || ed3.getText().toString().isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
                    alert.setTitle("提示");
                    alert.setMessage("請輸入:標題/日期/內容");
                    alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                }
                else if(photo_dir==null||record_dir==null||longitude==0||latitude==0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
                    alert.setTitle("提示");
                    alert.setMessage("請確認:拍照/錄音/定位");
                    alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                }
                else{
                    item = new Item();
                    item.setTitle(ed1.getText().toString()) ;
                    item.setDate(ed2.getText().toString());
                    item.setContent(ed3.getText().toString());
                    item.setPhotodir(photo_dir);  //photo_dir 有可能是null或是 photoLocation
                    item.setLatitude(latitude);
                    item.setLongitude(longitude);
                    item.setRecorddir(record_dir);
                    item.setCategory(category);
                    database.insert(item);

                    Toast.makeText(AddActivity.this, "新增完成", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cam_handle = new Intent();
                cam_handle.setClass(AddActivity.this, CamActivity.class);
                startActivityForResult(cam_handle, PHOTO_DIR);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent();
                map_intent.setClass(AddActivity.this, MapsActivity.class);
                startActivityForResult(map_intent, LOCATION);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rec_intent = new Intent();
                rec_intent.setClass(AddActivity.this, RecActivity.class);
                startActivityForResult(rec_intent, RECORD_DIR);
            }
        });

        category_spinner.setAdapter(new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, CATEGORY));
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = CATEGORY[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = CATEGORY[0];

            }
        });

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        // 設定初始日期
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // 跳出日期選擇器
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 完成選擇，顯示日期
                        ed2.setText(year + "-" + (monthOfYear + 1) + "-"
                                + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


}
