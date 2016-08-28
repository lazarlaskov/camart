package com.example.athan.raterdroid;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ImageLoader imageLoader) {
        activity = a;
        data=new ArrayList<String>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
    }

    public int getCount(){
        return data.size();
    }

    public void initList(ArrayList<String> arrayList){
        data.addAll(arrayList);
    }

    public void addItem(String s){
        data.add(s);
        this.notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
         //   .showStubImage(R.drawable.imgholder)
          //  .showImageForEmptyUri(R.drawable.imgholder)
            .resetViewBeforeLoading(true)
            .cacheInMemory()
            .cacheOnDisc()
          //  .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
          //  .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            //.delayBeforeLoading(1000)
            //.displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder gridViewImageHolder;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.row_listview_item, parent, false);
            gridViewImageHolder = new ViewHolder();
            gridViewImageHolder.imageView = (ImageView) vi.findViewById(R.id.rli_image);
            //gridViewImageHolder.textView = (TextView) vi.findViewById(R.id.rli_publisher);
            //gridViewImageHolder.textView.setText(position);
            //gridViewImageHolder.imageView.setMaxHeight(200);
            //gridViewImageHolder.imageView.setMaxWidth(200);
            vi.setTag(gridViewImageHolder);
        }
        else{
            gridViewImageHolder = (ViewHolder) vi.getTag();
            vi.setTag(gridViewImageHolder);
        }

        ;
        //ImageView image=(ImageView) vi.findViewById(R.id.rli_image);

        imageLoader.displayImage(data.get(position),gridViewImageHolder.imageView,options);

        return vi;
    }



}

