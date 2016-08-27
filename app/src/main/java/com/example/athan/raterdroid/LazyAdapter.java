package com.example.athan.raterdroid;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, String[] d, ImageLoader imageLoader) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_listview_item, null);

        TextView text=(TextView)vi.findViewById(R.id.rli_publisher);;
        ImageView image=(ImageView)vi.findViewById(R.id.rli_image);
        text.setText("item "+position);
        imageLoader.displayImage(data[position],image);
        return vi;
    }
}