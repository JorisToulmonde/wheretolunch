package com.example.wheretolunch;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListRestaurantActivity extends AppCompatActivity {

    private final String APIKEY = BuildConfig.Apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_restaurant);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        Integer rayon = intent.getIntExtra("rayon", 20);

        double longitude = intent.getDoubleExtra("longitude", 0);
        double latitude = intent.getDoubleExtra("latitude", 0);

        //Toast.makeText(this, String.valueOf(rayon)+" "+String.valueOf(longitude)+" "+String.valueOf(latitude), Toast.LENGTH_LONG).show();

        try {

            JSONObject jsonObject = getJSONObjectFromURL(latitude, longitude, rayon);
            JSONArray restaurants = jsonObject.getJSONArray("results");

            LinearLayout container = findViewById(R.id.container);

            for(int i = 0; i < restaurants.length(); i++){
                TextView tv = new TextView(this);
                tv.setText(restaurants.getJSONObject(i).getString("name"));
                container.addView(tv);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getJSONObjectFromURL(double latitude, double longitude, int rayon) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+rayon+"&type=restaurant&keyword=cruise&key="+APIKEY);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();

        return new JSONObject(jsonString);
    }
}
