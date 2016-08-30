package com.example.athan.raterdroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    TextView tw_profile_user;
    MyDBHandler dbHandler;
    GridView list;
    ProfileGridAdapter adapter;
    ProgressBar progressBar;
    //Button btn_load_more;
    //ProgressDialog progressDialog;

    static int NUM_LOADED_PROFILE = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        progressBar = (ProgressBar) getActivity().findViewById(R.id.pb_load_more);

        dbHandler = new MyDBHandler(getActivity());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheExtraOptions(480,320,null)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();

        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();

        adapter = new ProfileGridAdapter(getActivity(), ImageLoader.getInstance());

        list = (GridView) getActivity().findViewById(R.id.listView1);
        list.setAdapter(adapter);

        //btn_load_more = (Button) getActivity().findViewById(R.id.btn_load_more);

        tw_profile_user = (TextView) getActivity().findViewById(R.id.tw_profile_user);
        tw_profile_user.setText(dbHandler.getLoginInformation().get_id() + "'s profile");

        makeRequest();

        boolean pauseOnScroll = false; // or true
        boolean pauseOnFling = false; // or false
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
        list.setOnScrollListener(listener);

        /*
        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadMoreImages();
            }
        });
        */



        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    loadMoreImages();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

        });




    }

    private ArrayList<String> imageUrls = new ArrayList<String>();
    private ArrayList<String> imageIDs = new ArrayList<String>();


    public void makeRequest(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Toast.makeText(getActivity(), "Loading images", Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(
                "http://"+MainActivity.ipAddresServer+"/getphotos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSONArray(new JSONObject(response));
                            int n = imageUrls.size();
                            if(imageUrls.size() > 15) n = 15;

                            for(int i = 0; i < n; i++){
                                adapter.addItem(imageUrls.get(i));
                            }

                            NUM_LOADED_PROFILE = n;

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
            imageIDs.add(photo_id);
            imageUrls.add("http://" + MainActivity.ipAddresServer + "/photos/" + photo_name);
        }
    }

    public void loadMoreImages(){

        progressBar.setVisibility(View.VISIBLE);

        int n = 0;
        if(imageUrls.size() >= NUM_LOADED_PROFILE) {
            if (imageUrls.size() - NUM_LOADED_PROFILE >= 10) n = 10;
            else n = imageUrls.size() - NUM_LOADED_PROFILE;
            for (int i = NUM_LOADED_PROFILE; i < NUM_LOADED_PROFILE + n; i++) {
                adapter.addItem(imageUrls.get(i));
            }
        }
        NUM_LOADED_PROFILE+= n;

        progressBar.setVisibility(View.GONE);

    }




    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
}
