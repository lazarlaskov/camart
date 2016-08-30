package com.example.athan.raterdroid;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_reg;
    EditText et_user, et_pass;
    TextView tw_login;
    LinearLayout loginlayout, registerlayout;
    EditText et_user_reg, et_pass_reg;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new MyDBHandler(getApplicationContext());

        if(!dbHandler.getLoginInformation().get_id().equals("_NEMA_")){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setDisplayUseLogoEnabled(true);

        btn_login = (Button) findViewById(R.id.btn_login);
        et_pass = (EditText) findViewById(R.id.et_password);
        et_user = (EditText) findViewById(R.id.et_username);
        tw_login = (TextView) findViewById(R.id.tw_login);
        loginlayout = (LinearLayout) findViewById(R.id.layoutlogin);
        registerlayout = (LinearLayout) findViewById(R.id.layoutregister);
        btn_reg = (Button) findViewById(R.id.btn_register);
        et_pass_reg = (EditText) findViewById(R.id.et_password_register);
        et_user_reg = (EditText) findViewById(R.id.et_username_register);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void attemptLogin(){
        final String user = et_user.getText().toString();
        final String pass = et_pass.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://" + MainActivity.ipAddresServer +"/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("1")) {
                            String usernameLI = user;
                            LoginInformation loginInformation = new LoginInformation(usernameLI,"132065");
                            dbHandler.addLogin(loginInformation);
                            Toast.makeText(getApplicationContext(),"LOGIN SUCCESSFUL!" +
                                    " SESKEY: " + dbHandler.getLoginInformation().get_skey() +
                                    " USERNAME: " + dbHandler.getLoginInformation().get_id(),Toast.LENGTH_LONG).show();
                            Intent redirect = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(redirect);
                        } else {
                            Toast.makeText(getApplicationContext(),"LOGIN FAILED",Toast.LENGTH_LONG).show();
                            tw_login.setText("Incorrect username or password. Please try again.");
                            tw_login.setBackgroundColor(Color.parseColor("#e74c3c"));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", user);
                map.put("password", pass);

                return map;
            }
        };
        requestQueue.add(request);
    }


    private void attemptRegister(){
        final String username_reg = et_user_reg.getText().toString();
        final String password_reg = et_pass_reg.getText().toString();

        if(username_reg.equals("") || password_reg.equals("")) {

            if(username_reg.equals("")){
                et_user_reg.setError("Please enter username");
            }

            if (password_reg.equals("")) {
                et_pass_reg.setError("Please enter password");

            }

            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://" + MainActivity.ipAddresServer +"/register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.equals("1")){
                           Toast.makeText(getApplicationContext(),"Registration successful. Please log in.",Toast.LENGTH_LONG).show();
                           fnk_backreg_swap();
                       }else {
                           Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username_reg);
                map.put("password", password_reg);

                return map;
            }
        };
        requestQueue.add(request);




    }

    public void putLoginInDB(){

    }

    public void btn_register_click(View v){
        fnk_reg_swap();
    }

    public void btn_backregister_click(View v){
        fnk_backreg_swap();
    }

    public void fnk_backreg_swap(){
        registerlayout.setVisibility(View.GONE);
        tw_login.setText("Enter login information");
        loginlayout.setVisibility(View.VISIBLE);
    }

    public void fnk_reg_swap(){
        loginlayout.setVisibility(View.GONE);
        tw_login.setText("Enter register information");
        registerlayout.setVisibility(View.VISIBLE);
    }

}
