package com.unam.mobile.misavisos.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.unam.mobile.misavisos.R;
import com.unam.mobile.misavisos.UtilsClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends EasyLocationAppCompatActivity {

    @BindView(R.id.btn_sendLocation)
    ImageButton sendLocation;
    @BindView(R.id.btn_911)
    ImageButton btn_911;

    @BindView(android.R.id.content)
    View parentLayout;


    private String lat;
    private String lon;

    List<String> telefonosContactos = new ArrayList<>();
    private String textoFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);


        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocation();
            }
        });
        btn_911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsClass.makePhoneCall(getApplicationContext());
            }
        });
    }

    private void sendLocation() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);

        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);
    }

    @Override
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {
        Snackbar.make(parentLayout, getString(R.string.permLocation), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationReceived(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        telefonosContactos = UtilsClass.leeJSON(this);

        //Enviar SMS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(parentLayout, getString(R.string.permSms), Snackbar.LENGTH_SHORT).show();
        } else {
            textoFinal = getString(R.string.mapsURL) + lat + "," + lon;
            for (int i = 0; i < telefonosContactos.size(); i++) {
                UtilsClass.sendSms(telefonosContactos.get(i), textoFinal, this);
            }
            Toast.makeText(this, getString(R.string.locaizacionEnviada), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationProviderEnabled() {
    }

    @Override
    public void onLocationProviderDisabled() {
        Snackbar.make(parentLayout, getString(R.string.habilitLocation), Snackbar.LENGTH_SHORT).show();
    }

    /*@Override
    public void onBackPressed() {
        finish();
    }*/
}
