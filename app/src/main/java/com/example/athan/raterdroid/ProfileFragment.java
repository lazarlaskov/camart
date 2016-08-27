package com.example.athan.raterdroid;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ProfileFragment extends Fragment {

    TextView tw_profile_user;
    MyDBHandler dbHandler;
    ListView list;
    LazyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHandler = new MyDBHandler(getActivity());
        list=(ListView) getActivity().findViewById(R.id.listView1);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);

        adapter=new LazyAdapter(getActivity(), imageUrls,ImageLoader.getInstance());
        list.setAdapter(adapter);
        //tw_profile_user = (TextView) getActivity().findViewById(R.id.tw_profile_user);
        //tw_profile_user.setText(dbHandler.getLoginInformation().get_id() + "'s profile");
    }


    private String imageUrls[] = {
            "http://192.168.0.3/ratedroid/photos/1472275694492.jpg",
            "http://192.168.0.3/ratedroid/photos/1472274537146.jpg",
            "http://192.168.0.3/ratedroid/photos/1472272716931.jpg",
            "http://192.168.0.3/ratedroid/photos/1472250358857.jpg",
            "http://192.168.0.3/ratedroid/photos/1471971207404.jpg",
            "http://192.168.0.3/ratedroid/photos/1472250358857.jpg"
    };

    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
}
