package com.example.athan.raterdroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.LocaleDisplayNames;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by athan on 30.8.16.
 */
public class RaterListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Photo> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private MyDBHandler dbHandler;


    public RaterListAdapter(Activity a, ImageLoader imageLoader){
        activity = a;
        data = new ArrayList<Photo>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
        dbHandler = new MyDBHandler(activity);
    }


    public void addItem(Photo photo){
        data.add(photo);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolderRater gridViewImageHolder;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.row_listview_item, parent, false);
            gridViewImageHolder = new ViewHolderRater();
            gridViewImageHolder.imageView = (ImageView) vi.findViewById(R.id.iw_list_item_image);
            gridViewImageHolder.textViewPublisher = (TextView) vi.findViewById(R.id.tw_list_item_publisher);
            gridViewImageHolder.textViewAvgRating = (TextView) vi.findViewById(R.id.tw_avg_rating);
            gridViewImageHolder.ratingBar = (RatingBar) vi.findViewById(R.id.rb_list_item_rating);
            gridViewImageHolder.btn_vote = (Button) vi.findViewById(R.id.btn_vote);
            vi.setTag(gridViewImageHolder);
        }
        else{
            gridViewImageHolder = (ViewHolderRater) vi.getTag();
            vi.setTag(gridViewImageHolder);
        }

        imageLoader.displayImage(data.get(position).photo_url,gridViewImageHolder.imageView,options);
        gridViewImageHolder.textViewPublisher.setText(data.get(position).user_name);
        if(data.get(position).total_votes.equals("null")){
            data.get(position).total_votes = "0";
            data.get(position).avg_votes = "0";
           // data.get(position).myRating = Float.valueOf(0);
        }


        String average = String.format("%.2f", Float.parseFloat(data.get(position).avg_votes));
        gridViewImageHolder.textViewAvgRating.setText(average + " / "
                + data.get(position).total_votes);


        gridViewImageHolder.myRating = data.get(position).myRating;
        gridViewImageHolder.ratingBar.setRating(data.get(position).myRating);
        System.out.println("MY RATING: " + data.get(position).myRating);

        gridViewImageHolder.btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float vv = gridViewImageHolder.ratingBar.getRating();
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                StringRequest request = new StringRequest(Request.Method.POST,
                        "http://" + MainActivity.ipAddresServer + "/setrate.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                    //int tv = Integer.parseInt(data.get(position).total_votes);
                                    //tv++;
                                    //data.get(position).total_votes = String.valueOf(tv);
                                    //gridViewImageHolder.total_votes = tv;

                                    //float novprosek = 0;
                                    //float starprosek = Float.parseFloat(data.get(position).avg_votes);
                                    //novprosek = (((tv-1)*starprosek)+vv)/tv;
                                    //data.get(position).avg_votes = Float.toString(novprosek);

                                    //String[] ar = response.split("/");
                                    //data.get(position).avg_votes = ar[0];
                                    //data.get(position).total_votes = ar[1];
                                    gridViewImageHolder.textViewAvgRating.setText(response);
                                    Toast.makeText(activity,response, Toast.LENGTH_LONG).show();

                                    data.get(position).myRating = vv;

                                    gridViewImageHolder.myRating = vv;
                                    gridViewImageHolder.ratingBar.setRating(vv);


                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("photo_id", data.get(position).photo_id);
                        map.put("user_name", dbHandler.getLoginInformation().get_id());
                        map.put("rate_value", Float.toString(vv));
                        return map;
                    }
                };

                requestQueue.add(request);

            }
            });

        return vi;
    }
}
