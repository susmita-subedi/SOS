package com.example.snehauike.sos;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WhereAmIActivity extends AppCompatActivity {
    private Button b_get;
    private AutoCompleteTextView tvLocation;
    private TrackGPS gps;
    double longitude;
    double latitude;
    Geocoder geocoder;
    List<Address> adresses;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wher_am_i);

        b_get = (Button) findViewById(R.id.btLocation);
        tvLocation = (AutoCompleteTextView) findViewById(R.id.tvLocation);
        geocoder = new Geocoder(this, Locale.getDefault());

        b_get.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps = new TrackGPS(WhereAmIActivity.this);
                        final String address, city, state, country, postalCode, knownName;

                        if (gps.canGetLocation()) {
                            longitude = gps.getLongitude();
                            latitude = gps.getLatitude();
                            try {
                                adresses = geocoder.getFromLocation(latitude, longitude, 1);
                                address = adresses.get(0).getAddressLine(0);
                                city = adresses.get(0).getLocality();
                                state = adresses.get(0).getAdminArea();
                                country = adresses.get(0).getCountryName();
                                postalCode = adresses.get(0).getPostalCode();
                                knownName = adresses.get(0).getFeatureName();

                                tvLocation.setText("Address:" + address + "\nCity:" + city + "\nState:" + state + "\nCountry:" + country + "\nCode:" + postalCode);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_LONG).show();

                        } else {
                            gps.showSettingsAlert();
                        }
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }
}
