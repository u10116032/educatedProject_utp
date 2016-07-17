package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 瀚磊 on 2016/4/9.
 */
public class MyDbHelper extends SQLiteOpenHelper {
    public final  static String TABLE_NAME = "mydata";
    public final  static String ID_FIELD = "_id";
    public final  static String DATE_FIELD = "date";
    public final  static String TITLE_FIELD = "title";
    public final  static String CONTENT_FIELD = "content";
    public final  static String LONGITUDE_FIELD = "longitude";
    public final  static String LATITUDE_FIELD = "latitude";
    public final  static String PHOTODIR_FIELD = "photo_dir";
    public final  static String RECORDDIR_FIELD = "rec_dir";
    public final  static String CATEGORY_FIELD = "category";
    public final  static String UPLOAD_FIELD = "upload";
    private SharedPreferences account;
    private String user;

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.account = context.getSharedPreferences("account", 0);
        this.user = account.getString("account", "");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + user);
        // 呼叫onCreate建立新版的表格
        createTable(db);
        onCreate(db);
    }

    long insert(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);
        ContentValues values = new ContentValues();

        values.put(DATE_FIELD, item.getDate());
        values.put(TITLE_FIELD, item.getTitle());
        values.put(CONTENT_FIELD, item.getContent());
        values.put(LONGITUDE_FIELD, item.getLongitude());
        values.put(LATITUDE_FIELD, item.getLatitude());
        values.put(PHOTODIR_FIELD, item.getPhotodir());
        values.put(RECORDDIR_FIELD, item.getRecorddir());
        values.put(CATEGORY_FIELD, item.getCategory());
        values.put(UPLOAD_FIELD, 0);

        long result = db.insert(user, null, values);
        item.setId(result);
        return  result;
    }

    long update(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);
        ContentValues values = new ContentValues();

        values.put(DATE_FIELD, item.getDate());
        values.put(TITLE_FIELD, item.getTitle());
        values.put(CONTENT_FIELD, item.getContent());
        values.put(LONGITUDE_FIELD, item.getLongitude());
        values.put(LATITUDE_FIELD, item.getLatitude());
        values.put(PHOTODIR_FIELD, item.getPhotodir());
        values.put(RECORDDIR_FIELD, item.getRecorddir());
        values.put(CATEGORY_FIELD, item.getCategory());
        values.put(UPLOAD_FIELD, item.getUpload());



        String where = ID_FIELD + "=" + item.getId();
        long result = db.update(user, values, where, null);
        return  result;
    }

    long delete(long _id){
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);
        int result = db.delete(user, ID_FIELD + " =" + _id, null);
        db.close();
        return result;
    }







    public List<Item> getAll() {
        List<Item> result = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);
        Cursor cursor = db.query(
                user, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item result = new Item();

        result.setId(cursor.getLong(0));
        result.setDate(cursor.getString(1));
        result.setTitle(cursor.getString(2));
        result.setContent(cursor.getString(3));
        result.setLongitude(cursor.getDouble(4));
        result.setLatitude(cursor.getDouble(5));
        result.setPhotodir(cursor.getString(6));
        result.setRecorddir(cursor.getString(7));
        result.setCategory(cursor.getString(8));
        result.setUpload(cursor.getInt(9) == 1);
        // 回傳結果
        return result;
    }

    public List<Item> getCategoryItems(String[] selectionArg){
        List<Item> result = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);


        Cursor cursor = db.query(user, null, "category=?", selectionArg, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();

        return  result;
    }

    public List<Item> getCategoryItemsNotUpload(String[] selectionArg){
        List<Item> result = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);


        Cursor cursor = db.query(user, null, "category=? AND upload=?", selectionArg, null, null, null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();

        return  result;
    }

    public void createTable(SQLiteDatabase db){
        String CREATE_TABLE = "create table if not exists "+user+" ("+ID_FIELD+" integer primary key autoincrement, "+DATE_FIELD+" text not null , "+TITLE_FIELD+" text not null , "+CONTENT_FIELD+ " text, "
                +LONGITUDE_FIELD+" real, "+LATITUDE_FIELD+" real, "+PHOTODIR_FIELD+" text, " + RECORDDIR_FIELD + " text, " + CATEGORY_FIELD +" text, "+UPLOAD_FIELD+ " boolean ) ";
        db.execSQL(CREATE_TABLE);
    }
}
