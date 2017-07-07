package com.android.susmita.clientservertest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class DroidBluetooth extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private final int VIDEO_REQUEST_CODE = 100;

    BluetoothAdapter bluetoothAdapter;

    private UUID myUUID;
    private String myName;

    LinearLayout inputPane;
    EditText inputField;
    Button btnSend;

    TextView textInfo, textStatus;

    ThreadBeConnected myThreadBeConnected;
    ThreadConnected myThreadConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_bluetooth);

        textInfo = (TextView)findViewById(R.id.info);
        textStatus = (TextView)findViewById(R.id.status);

        inputPane = (LinearLayout)findViewById(R.id.inputpane);
        inputField = (EditText)findViewById(R.id.input);
        btnSend = (Button)findViewById(R.id.send);

        btnSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(myThreadConnected!=null){
                    byte[] bytesToSend = inputField.getText().toString().getBytes();
                    myThreadConnected.write(bytesToSend);
                }
            }});

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //generate UUID on web: http://www.famkruithof.net/uuid/uuidgen
        //have to match the UUID on the another device of the BT connection
        myUUID = UUID.fromString("ec79da00-853f-11e4-b4a9-0800200c9a66");
        myName = myUUID.toString();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not supported on this hardware platform",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = "\n"+bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        //textInfo.setText(stInfo);


    }


    @Override
    protected void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {
        //textStatus.setText("\nsetup()");
        myThreadBeConnected = new ThreadBeConnected();
        myThreadBeConnected.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myThreadBeConnected!=null){
            myThreadBeConnected.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                setup();
            }else{
                Toast.makeText(this,
                        "BlueTooth NOT enabled",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(requestCode == VIDEO_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(),"video successfully recorded", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"video Failed recording", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class ThreadBeConnected extends Thread {

        private BluetoothServerSocket bluetoothServerSocket = null;

        public ThreadBeConnected() {
            try {
                bluetoothServerSocket =
                        bluetoothAdapter.listenUsingRfcommWithServiceRecord(myName, myUUID);

                //textStatus.setText("\nWaiting\n" + "bluetoothServerSocket :\n" + bluetoothServerSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket bluetoothSocket = null;

            if(bluetoothServerSocket!=null){
                try {
                    bluetoothSocket = bluetoothServerSocket.accept();

                    BluetoothDevice remoteDevice = bluetoothSocket.getRemoteDevice();

                    final String strConnected = "\nConnected:\n" +
                            remoteDevice.getName() + "\n" +
                            remoteDevice.getAddress();

                    //connected
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            //textStatus.setText(strConnected);
                            inputPane.setVisibility(View.VISIBLE);
                            //video
                            Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            File video_file = getFilePath();
                            Uri video_uri = Uri.fromFile(video_file);
                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
                            camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                            startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);

                        }});

                    startThreadConnected(bluetoothSocket);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String eMessage = e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            textStatus.setText("something wrong: \n" + eMessage);
                        }});
                }
            }else{
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        textStatus.setText("bluetoothServerSocket == null");
                    }});
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(),
                    "\nclose bluetoothServerSocket\n",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);

                    final String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = strReceived;

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            textStatus.setText(msgReceived);
                            if(strReceived.equals("help")){

                                Intent videoIntent = new Intent(DroidBluetooth.this, StressActivity.class);
                                startActivity(videoIntent);


                                Toast.makeText(getApplicationContext(),"help",Toast.LENGTH_LONG).show();
                            }

                        }});

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            textStatus.setText(msgConnectionLost);
                        }});
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
                connectedOutputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }



    public File getFilePath(){

        File folder = new File("sdcard/video_app");
        if(folder.exists()){
            folder.mkdir();

        }

        File video_file = new File(folder,"sample_video.mp4");

        return video_file;
    }





}
