package com.example.athan.raterdroid;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import org.w3c.dom.Text;


public class RaterFragment extends Fragment {

    TextView tw;
    MyDBHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_rater, container, false);

        dbHandler = new MyDBHandler(getActivity().getApplicationContext());
        String username = "NEMA NISTO";
        username = dbHandler.getLoginInformation().get_id();
        //tw = (TextView) getActivity().findViewById(R.id.tw_user1);
        //tw.setText(username);

        Toast.makeText(getActivity().getApplicationContext(), username, Toast.LENGTH_LONG).show();

        return rootView;
    }

}
