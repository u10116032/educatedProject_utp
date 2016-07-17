package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by 瀚磊 on 2016/4/12.
 */
public class ListAdapter extends ArrayAdapter<Item> {

    private int resource;
    private List<Item> items;


    public ListAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.items = items;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        final Item item = getItem(position);

        if (convertView == null) {
            // 建立項目畫面元件
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(inflater);
            li.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        //ImageView pic = (ImageView)itemView.findViewById(R.id.pic);
        ImageView item_icon = (ImageView)itemView.findViewById(R.id.item_icon);
        TextView title = (TextView)itemView.findViewById(R.id.title);
        TextView date = (TextView)itemView.findViewById(R.id.date);

        title.setText(item.getTitle());
        date.setText(item.getDate());
        if(item.getPhotodir()!=null){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(item.getPhotodir(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
            item_icon.setImageBitmap(bitmap);
        }

        return itemView;
    }


}
