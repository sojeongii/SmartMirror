package com.example.smartmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ManualActivity extends AppCompatActivity {

    Button buttonCamera;
    String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        buttonCamera = (Button)findViewById(R.id.button_picture);

        buttonCamera.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //virtualfittingThread cameraThread = ThreadHandler.thread;
                //ameraThread.execute("cam");
                //virtualfittingThread camera = ThreadHandler.getThread();
                virtualfittingThread camera = new virtualfittingThread();
                camera.execute("cam");

            }
        });
    }


}
