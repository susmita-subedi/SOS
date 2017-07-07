package com.android.susmita.ssgaud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class StressActivity extends AppCompatActivity {

    Button stressButton;
    TextView stressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress);

        stressText = (TextView) findViewById(R.id.tvStress);
        stressText.setText("Hello! Please press below button in case of emergency.");

        stressButton = (Button) findViewById(R.id.btStress);
        /*stressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer mediaPlayer = MediaPlayer.create(StressActivity.this, R.raw.siren);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6504229567"));
                if (ActivityCompat.checkSelfPermission(StressActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });*/

    }
}
