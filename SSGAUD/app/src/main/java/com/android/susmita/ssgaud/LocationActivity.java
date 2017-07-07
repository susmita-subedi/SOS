package com.android.susmita.ssgaud;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    private Button b_get,b_request, b_trackLocation;
    private TextView tvLocation;
    private TrackGPS gps;
    double longitude;
    double latitude;
    Geocoder geocoder;
    List<Address> adresses;

    //server variables
    Socket client;
    PrintWriter writer;
    int status = 0;

    //time
    Calendar calendar = Calendar.getInstance();
    //current time
    Date d=new Date();
    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
    String currentDateTimeString = sdf.format(d);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        b_get = (Button) findViewById(R.id.btGetLocation);
        b_request = (Button) findViewById(R.id.btRequest);
        b_trackLocation = (Button) findViewById(R.id.btTrackLocation);

        b_trackLocation.setVisibility(View.INVISIBLE);

        tvLocation = (TextView) findViewById(R.id.tvLocation);

        //gps
        geocoder = new Geocoder(this, Locale.getDefault());
        gps = new TrackGPS(LocationActivity.this);
        longitude = gps.getLongitude();
        latitude = gps.getLatitude();

        final int lng = (int) Math.round(longitude);
        final int lat = (int) Math.round(latitude);


        //b_get function
        b_get.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String address,city,state,country,postalCode,knownName;
                        if(gps.canGetLocation()){
                            try {
                                adresses = geocoder.getFromLocation(latitude,longitude,1);
                               address = adresses.get(0).getAddressLine(0);
                                city = adresses.get(0).getLocality();
                                state = adresses.get(0).getAdminArea();
                                country = adresses.get(0).getCountryName();
                                postalCode = adresses.get(0).getPostalCode();
                                knownName = adresses.get(0).getFeatureName();

                                /*tvLocation.setText("Longitude:" +Double.toString(longitude)+
                                        "\nLatitude:" +Double.toString(latitude));*/

                                tvLocation.setText("Your Location:\n" +address+"\n"+city+"\n"+state+
                                "\n"+country+"\n"+postalCode+"\n"+knownName);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Toast.makeText(getApplicationContext(),"Longitude:" +lng+"\nLatitude:" +Double.toString(latitude), Toast.LENGTH_LONG).show();

                        }else{
                            gps.showSettingsAlert();
                        }
                    }
                }
        );//end of b_get


        //b_reqest function
        b_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Socket socket = null;
                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;

                try{
                    socket = new Socket("192.168.1.93", 5554);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF(String.valueOf(lng));
                    dataOutputStream.writeUTF(String.valueOf(lat));

                    tvLocation.setText("Droid "+dataInputStream.readUTF()+" has been assited to you.\n Thanks.");
                    b_trackLocation.setVisibility(View.VISIBLE);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });//end of b_request function



        //start of b_tracklocation
        b_trackLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String address,city,state,country,postalCode,knownName;
                if(gps.canGetLocation()) {
                    try {
                        adresses = geocoder.getFromLocation(latitude, longitude, 1);
                        address = adresses.get(0).getAddressLine(0);
                        city = adresses.get(0).getLocality();
                        state = adresses.get(0).getAdminArea();
                        country = adresses.get(0).getCountryName();

                        Intent adressIntent = new Intent(LocationActivity.this,MapsActivity.class);
                        adressIntent.putExtra("Address", address);
                        adressIntent.putExtra("City", city);
                        adressIntent.putExtra("State", state);
                        adressIntent.putExtra("Country", country);
                        startActivity(adressIntent);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });



    }//end of onCreate




}//edn of activity
