package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ListView listView;
    private LocationManager lm;
    private LocationListener listener;

    ArrayAdapter<String> adapter;
    List<String> listItems = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        tv = (TextView) findViewById(R.id.text_view);
        listView = (ListView) findViewById(R.id.list_view);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        registerListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        registerListener();
    }

    void registerListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    public void addItem(String msg) {
        Collections.reverse(listItems);
        if (listItems.size() == 10) {
            listItems.remove(0);
        }
        listItems.add(msg);
        Collections.reverse(listItems);
        adapter.notifyDataSetChanged();
    }


    private class MyListener implements LocationListener {
        Location prevLocation = null;
        Instant start = Instant.now();
        Instant end;

        @Override
        public void onLocationChanged(Location location) {
            end = Instant.now();
            przetwarzajLokalizacje(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        private void przetwarzajLokalizacje(Location location) {
            String info = String.format("Lat: %.4f, Lon: %.4f", location.getLatitude(), location.getLongitude());
            if (prevLocation != null) {
                float bearing = prevLocation.bearingTo(location);
                float distance = prevLocation.distanceTo(location);
                float speed = location.getSpeed();
                info += String.format(", bearing: %.2f, distance: %.2f, speed: %.2f", bearing, distance, speed);
            }
            tv.setText(info);
            addItem(info);
            prevLocation = location;
            start = Instant.now();

        }
    }

}