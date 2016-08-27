package com.example.athan.raterdroid;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by athan on 27.8.16.
 */
public class Splash extends AppCompatActivity {

    MyDBHandler dbHandler;
    String akt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashh);

        dbHandler = new MyDBHandler(getApplicationContext());

        if(dbHandler.getLoginInformation().get_skey().equals("_NEMA_")){
            akt = "Login";
        }
        else{
            akt = "Main";
        }

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(1500);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    initActivity();
                }
            }
        };
        timer.start();

    }


    private void initActivity(){
        Intent i;
        if(akt.equals("Login")){
            i = new Intent(this, LoginActivity.class);
        }else{
            i = new Intent(this, MainActivity.class);
        }
        startActivity(i);
    }


    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        finish();

    }
}
