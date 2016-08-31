package com.example.athan.raterdroid;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class ProfileGridAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Photo> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public ProfileGridAdapter(Activity a, ImageLoader imageLoader) {
        activity = a;
        data=new ArrayList<Photo>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
    }

    public int getCount(){
        return data.size();
    }

    public void initList(ArrayList<Photo> arrayList){
        data.addAll(arrayList);
    }

    public void addItem(Photo photo){
        data.add(photo);
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
            //.cacheInMemory()
            .cacheOnDisc()
            .imageScaleType(ImageScaleType.EXACTLY) // default
            .bitmapConfig(Bitmap.Config.RGB_565) // default
            //.delayBeforeLoading(1000)
            //.displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final ViewHolder gridViewImageHolder;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.square_gridview_item, parent, false);
            gridViewImageHolder = new ViewHolder();
            gridViewImageHolder.imageView = (ImageView) vi.findViewById(R.id.rli_image);
            gridViewImageHolder.textView = (TextView) vi.findViewById(R.id.rli_publisher);
            gridViewImageHolder.ratingBar = (RatingBar) vi.findViewById(R.id.grid_photo_rating_bar);
            //gridViewImageHolder.imageView.setMaxHeight(200);
            //gridViewImageHolder.imageView.setMaxWidth(200);
            vi.setTag(gridViewImageHolder);
        }
        else{
            gridViewImageHolder = (ViewHolder) vi.getTag();
            vi.setTag(gridViewImageHolder);
        }

        if(data.get(position).total_votes.equals("null")){
            data.get(position).total_votes = "0";
            data.get(position).avg_votes = "0";
        }

        String average = String.format("%.2f", Float.parseFloat(data.get(position).avg_votes));

        imageLoader.displayImage(data.get(position).photo_url,gridViewImageHolder.imageView,options);
        gridViewImageHolder.ratingBar.setRating(Float.parseFloat(average));

        gridViewImageHolder.textView.setText(average + " / "
                + data.get(position).total_votes);


        return vi;
    }



}

