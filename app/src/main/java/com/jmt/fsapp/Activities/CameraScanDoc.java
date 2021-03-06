package com.jmt.fsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jmt.fsapp.Adapter.CameraPreview;
import com.jmt.fsapp.R;

public class CameraScanDoc extends AppCompatActivity {
        private Camera camera;
        private Button capture, tomarotra, salir;
        private FrameLayout frameLayout;
        private CameraPreview cameraPreview;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera_scan_doc);
            capture = findViewById(R.id.captureBut);
            tomarotra = findViewById(R.id.tomarotraButon);
            salir = findViewById(R.id.salircamaraButton);
            frameLayout = findViewById(R.id.frameLayout);
            //Abrir camara
            camera = Camera.open();
            cameraPreview = new CameraPreview(this, camera);
            frameLayout.addView(cameraPreview);

        }


    }