package com.example.wheretolunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity{

    private FusedLocationProviderClient client;
    private static boolean isLocationOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView rayonLabel = findViewById(R.id.rayonlabel);
        final SeekBar rayon = findViewById(R.id.rayon);

        rayonLabel.setText(String.valueOf(rayon.getProgress()*50)+"m");

        requestPermission();

        final Intent intent = new Intent(this, ListRestaurantActivity.class);


        client = LocationServices.getFusedLocationProviderClient(this);

        Button button = findViewById(R.id.getlocation);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                boolean isLocationOK = false;
                if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    return;
                }

                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {
                            intent.putExtra("rayon", rayon.getProgress()*50);
                            intent.putExtra("longitude", location.getLongitude());
                            intent.putExtra("latitude", location.getLatitude());
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        rayon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int actualProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                actualProgress = seekBar.getProgress();
                rayonLabel.setText(String.valueOf(actualProgress*50)+"m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, INTERNET, ACCESS_NETWORK_STATE}, 1);
    }

}
