package com.example.athan.raterdroid;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class RaterFragment extends Fragment {

    MyDBHandler dbHandler;
    RaterListAdapter adapter;
    ListView listView;

    static int NUM_LOADED_RATER;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_rater, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHandler = new MyDBHandler(getActivity());
        listView = (ListView) getActivity().findViewById(R.id.lw_rater_list_view);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();

        adapter = new RaterListAdapter(getActivity(), ImageLoader.getInstance());
        listView.setAdapter(adapter);

        boolean pauseOnScroll = false; // or true
        boolean pauseOnFling = false; // or false
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
        listView.setOnScrollListener(listener);


        makeRequest();



    }

    //private ArrayList<String> imageUrls = new ArrayList<String>();
    //private ArrayList<String> imageIDs = new ArrayList<String>();
    private ArrayList<Photo> images = new ArrayList<Photo>();

    public void makeRequest(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Toast.makeText(getActivity(), "Loading images", Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(
                "http://"+MainActivity.ipAddresServer+"/getphotos.php?username="+dbHandler.getLoginInformation().get_id(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSONArray(new JSONObject(response));
                            int n = images.size();
                            if(images.size() > 15) n = 15;

                            for(int i = 0; i < n; i++){
                                adapter.addItem(images.get(i));
                            }

                            NUM_LOADED_RATER = n;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    public void parseJSONArray(JSONObject response) throws JSONException {
        JSONArray items = response.getJSONArray("data");
        for (int i = 0; i < items.length(); i++) {
            JSONObject c = (JSONObject) items.get(i);
            String photo_id = c.getString("photo_id");
            String photo_name = c.getString("photo_name");
            String user_name = c.getString("user_name");
            String total_votes = c.getString("broj_glasovi");
            String avg_votes = c.getString("prosek_glasovi");
            String moj_rejt = c.getString("moj_rejting");
            Photo photo = new Photo();
            photo.photo_id = photo_id;
            photo.photo_url = "http://" + MainActivity.ipAddresServer + "/photos/" + photo_name;
            photo.user_name = user_name;
            photo.avg_votes = avg_votes;
            photo.total_votes = total_votes;
            if(moj_rejt.equals("null")) moj_rejt = "0";
            photo.myRating = Float.parseFloat(moj_rejt);
            images.add(photo);
        }
    }


    public void loadMoreImages(){

        int n = 0;
        if(images.size() >= NUM_LOADED_RATER) {
            if (images.size() - NUM_LOADED_RATER >= 10) n = 10;
            else n = images.size() - NUM_LOADED_RATER;
            for (int i = NUM_LOADED_RATER; i < NUM_LOADED_RATER + n; i++) {
                adapter.addItem(images.get(i));
            }
        }
        NUM_LOADED_RATER+= n;

    }


    @Override
    public void onDestroy()
    {
        listView.setAdapter(null);
        super.onDestroy();
    }

}
