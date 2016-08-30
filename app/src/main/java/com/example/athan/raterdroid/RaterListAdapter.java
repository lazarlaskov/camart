package com.example.athan.raterdroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
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

/**
 * Created by athan on 30.8.16.
 */
public class RaterListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;


    public RaterListAdapter(Activity a, ImageLoader imageLoader){
        activity = a;
        data = new ArrayList<String>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
    }


    public void addItem(String s){
        data.add(s);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolderRater gridViewImageHolder;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.row_listview_item, parent, false);
            gridViewImageHolder = new ViewHolderRater();
            gridViewImageHolder.imageView = (ImageView) vi.findViewById(R.id.iw_list_item_image);
            gridViewImageHolder.textViewPublisher = (TextView) vi.findViewById(R.id.tw_list_item_publisher);
            gridViewImageHolder.textViewAvgRating = (TextView) vi.findViewById(R.id.tw_avg_rating);
            gridViewImageHolder.ratingBar = (RatingBar) vi.findViewById(R.id.rb_list_item_rating);
            vi.setTag(gridViewImageHolder);
        }
        else{
            gridViewImageHolder = (ViewHolderRater) vi.getTag();
            vi.setTag(gridViewImageHolder);
        }

        imageLoader.displayImage(data.get(position),gridViewImageHolder.imageView,options);
        gridViewImageHolder.textViewPublisher.setText(new String(position + " "));
        gridViewImageHolder.textViewAvgRating.setText(new String("4,8 / 16"));
        gridViewImageHolder.ratingBar.setRating(position%5);

        return vi;
    }
}
