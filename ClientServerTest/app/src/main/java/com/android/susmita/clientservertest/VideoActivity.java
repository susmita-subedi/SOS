package com.android.susmita.clientservertest;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
    private final int VIDEO_REQUEST_CODE = 100;

    Button captureVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        captureVideoButton = (Button) findViewById(R.id.button2);

        captureVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File video_file = getFilePath();
                Uri video_uri = Uri.fromFile(video_file);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
                camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == VIDEO_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(),"video successfully recorded", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"video Failed recording", Toast.LENGTH_LONG).show();
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
