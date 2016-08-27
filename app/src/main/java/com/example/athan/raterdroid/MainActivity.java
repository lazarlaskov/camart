package com.example.athan.raterdroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    public static AHBottomNavigation bottomNavigation;
    public static String ipAddresServer = "192.168.0.3";
    public static MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setDisplayUseLogoEnabled(true);
        menu.setDisplayShowTitleEnabled(false);

        dbHandler = new MyDBHandler(getApplicationContext());

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomnavig_ID);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {

                    RaterFragment raterFragment = new RaterFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.maincontent, raterFragment)
                            .addToBackStack("raterfrag")
                            .commit();

                } else if (position == 1) {


                    UploadFragment uploadFragment = new UploadFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.maincontent,uploadFragment)
                            .addToBackStack("uploadfrag")
                            .commit();

                } else if(position == 2){

                    ProfileFragment profileFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.maincontent,profileFragment)
                            .addToBackStack("profilefrag")
                            .commit();

                }
            }
        });

        this.createNavItems();
    }


    private void createNavItems() {

        AHBottomNavigationItem likes_item = new AHBottomNavigationItem("Rate photos", R.drawable.star);
        AHBottomNavigationItem upload_item = new AHBottomNavigationItem("Take photo", R.drawable.upload);
        AHBottomNavigationItem profile_item = new AHBottomNavigationItem("My Profile", R.drawable.user);

        bottomNavigation.setAccentColor(Color.parseColor("#ecf0f1"));
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#34495e"));
        bottomNavigation.setInactiveColor(Color.parseColor("#16a085"));

        bottomNavigation.addItem(likes_item);
        bottomNavigation.addItem(upload_item);
        bottomNavigation.addItem(profile_item);
        //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("white"));
        bottomNavigation.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_about){
            Toast.makeText(getApplicationContext(), "Version 1.0 | Developed by AJ and LL", Toast.LENGTH_LONG).show();
            return true;
        }else if(item.getItemId() == R.id.menu_item_logout){
            dbHandler.deleteLogin();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
