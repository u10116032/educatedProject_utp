package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class ModifyActivity extends AppCompatActivity {

    Button cam,rec,loc,modify,cancle;
    EditText ed1,ed2,ed3;
    private Item item;
    double longitude=0,latitude=0;
    String photo_dir=null;
    String record_dir=null;
    private static final int PHOTO_DIR = 0;
    private static final int RECORD_DIR = 1;
    private static final int LOCATION = 2;
    private String category;
    private String CATEGORY[] = {"動物", "植物", "其他"};
    private Spinner category_spinner;



    private MyDbHelper database ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        setContentView(R.layout.activity_modify);
        database = new MyDbHelper(this, MyDbHelper.TABLE_NAME, null , 1);
        category = CATEGORY[0];

        Intent intent = getIntent();
        // 讀取Action名稱
        String action = intent.getAction();

        // 如果是修改記事
        if (action.equals("EDIT_ITEM")) {
            // 接收記事物件與設定標題、內容
            item = (Item) intent.getExtras().getSerializable(
                    "Item");
        }



        cam = (Button)findViewById(R.id.camera);
        rec = (Button)findViewById(R.id.record);
        loc = (Button)findViewById(R.id.location);
        modify = (Button)findViewById(R.id.modify);
        cancle = (Button)findViewById(R.id.cancle);
        ed1 = (EditText)findViewById(R.id.editText1); //title
        ed1.setText(item.getTitle());
        ed2 = (EditText)findViewById(R.id.editText2); //date
        ed2.setText(item.getDate());
        ed3 = (EditText)findViewById(R.id.editText3); //content
        ed3.setText(item.getContent());
        category_spinner = (Spinner)findViewById(R.id.category_spinner) ;

        category_spinner.setAdapter(new ArrayAdapter<String>(ModifyActivity.this, android.R.layout.simple_spinner_item, CATEGORY));
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

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cam_intent = new Intent();
                cam_intent.setClass(ModifyActivity.this, CamActivity.class);
                startActivityForResult(cam_intent, PHOTO_DIR);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rec_intent = new Intent();
                rec_intent.setClass(ModifyActivity.this, RecActivity.class);
                startActivityForResult(rec_intent, RECORD_DIR);
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loc_intent = new Intent();
                loc_intent.setClass(ModifyActivity.this, MapsActivity.class);
                startActivityForResult(loc_intent, LOCATION);
            }
        });


        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setTitle(ed1.getText().toString());
                item.setDate(ed2.getText().toString());
                item.setContent(ed3.getText().toString());
                if(latitude !=0 && longitude != 0){
                    item.setLatitude(latitude);
                    item.setLongitude(longitude);
                }
                else {
                    item.setLatitude(item.getLatitude());
                    item.setLongitude(item.getLongitude());
                }
                if(photo_dir!=null){
                    item.setPhotodir(photo_dir);
                }
                else{
                    item.setPhotodir(item.getPhotodir());
                }
                if(record_dir!=null){
                    item.setRecorddir(record_dir);
                }
                else{
                    item.setRecorddir(item.getRecorddir());
                }
                item.setCategory(category);
                database.update(item);
                finish();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
