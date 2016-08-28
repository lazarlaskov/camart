package com.example.athan.raterdroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class UploadFragment extends Fragment {
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    private ProgressBar progressBar;
    private TextView tw_response;
    private ImageView iw_uploadphoto;
    private String poslednaslikaime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getFileUri();
        i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        startActivityForResult(i, 10);
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getFileUri() {
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        poslednaslikaime = ts + ".jpg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + poslednaslikaime
        );

        file_uri = Uri.fromFile(file);
    }

    public Bitmap decodeFile(File f){

        Display display = this.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int size_x = size.x;

        try {


            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=size_x;
            final int REQUIRED_HIGHT=size_x;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = null;
           // bitmap = BitmapFactory.decodeFile(file_uri.getPath());
          //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
         //   bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
          //  bitmap.recycle();

            bitmap = decodeFile(new File(file_uri.getPath()));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            bitmap.recycle();

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, Base64.DEFAULT);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }


    private void makeRequest() {
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressupload);
        tw_response = (TextView) getActivity().findViewById(R.id.uploadresponse);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#1abc9c"), PorterDuff.Mode.SRC_ATOP);
      //  tw_response.setBackgroundColor(Color.parseColor("#1abc9c"));
        tw_response.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://" + MainActivity.ipAddresServer +"/ratedroid/uploadp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resp = "";
                        if (response.equalsIgnoreCase("success")) {
                            resp = "Photo uploaded";
                            tw_response.setBackgroundColor(Color.parseColor("#1abc9c"));
                        } else {
                            System.out.println("RESPONSE BRATE: " + response);
                            Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                            resp = response;
                        }

                        progressBar.setVisibility(View.GONE);
                        tw_response.setText(resp);
                        //tw_response.setBackgroundColor(Color.parseColor("#1abc9c"));
                        tw_response.setVisibility(View.VISIBLE);

                        new DownloadImageTask((ImageView) getActivity().
                                findViewById(R.id.iw_upReview))
                                .execute("http://" + MainActivity.ipAddresServer + "/ratedroid/photos/" + poslednaslikaime);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tw_response.setText(error.toString());
                tw_response.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("encoded_string", encoded_string);
                map.put("image_name", poslednaslikaime);

                return map;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == getActivity().RESULT_OK) {
            new Encode_image().execute();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Toast.makeText(getActivity(),"bitmap OnPostExecute()",Toast.LENGTH_LONG);
            bmImage.setImageBitmap(result);
        }
    }

}
