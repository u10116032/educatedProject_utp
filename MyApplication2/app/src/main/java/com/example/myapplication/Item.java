package com.example.myapplication;

/**
 * Created by 瀚磊 on 2016/4/12.
 */
public class Item implements java.io.Serializable {
    private long id;
    private String date;
    private String title;
    private String content;
    private String photodir;
    private double latitude;
    private double longitude;
    private String recorddir;
    private String category;
    private boolean upload;
    private boolean selected;

    public Item() {
        title = "";
        content = "";
    }

    public  Item(long id, String date, String title,
                String content, double latitude, double longitude, String photodir, String recorddir, String category) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photodir = photodir;
        this.recorddir = recorddir;
        this.category = category;

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPhotodir() {
        return photodir;
    }

    public void setPhotodir(String photodir) {
        this.photodir = photodir;
    }

    public String getRecorddir() {
        return recorddir;
    }

    public void setRecorddir(String recorddir) {
        this.recorddir = recorddir;
    }

    public String getCategory(){ return category; }

    public void setCategory(String category){this.category = category;}

    public boolean getUpload(){
        return upload;
    }

    public void setUpload(boolean upload){
        this.upload = upload;
    }
}
