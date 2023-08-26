package com.example.memeshare;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private final String url = "https://meme-api.com/gimme";
//private final String url = "https://ronreiter-meme-generator.p.rapidapi.com/meme";
ImageView imageView;
ProgressBar progressBar;
    String URL= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
//        Glide.with(this).load(url).into(imageView);
    }

    private void getData() {
        // RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, "Running", Toast.LENGTH_SHORT).show();

                try {
                    URL = response.getString("url");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                imageView = findViewById(R.id.imageView);
                progressBar=findViewById(R.id.progressBar);
                Glide.with(MainActivity.this)
                        .load(URL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);
                //Glide.with(MainActivity.this).load(URL).into(imageView);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Some error occurred."+error, Toast.LENGTH_SHORT).show();
                    }
                });

       mRequestQueue.add(jsonObjectRequest);


    }

    @SuppressLint("QueryPermissionsNeeded")
    public void shareMeme(View view) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,URL);
        Intent chooser= Intent.createChooser(intent,"Share using...");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void nextMeme(View view) {
        getData();
    }
}