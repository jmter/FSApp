package com.jmt.fsapp.Adapter;

import android.content.Context;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback  {

    Camera camera;
    SurfaceHolder holder;
    public Camera.Size size;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Parameters parameters = camera.getParameters();
        // Ajustar parametros de la camara
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            parameters.set("orientation", "portrait");
            parameters.set("rotation",90);
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }else{
            parameters.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
        camera.setParameters(parameters);
        Log.i("Config",String.valueOf(this.getResources().getConfiguration().orientation));
        try {
            // set preview size and make any resize, rotate or
            // reformatting changes here

            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                float r = (float) size.width/size.height;
                if (1920 >= size.width & (r >= 0.98) & (r <= (1.02))) {
                    Log.i("Dim","A"+size.width+"L"+size.height);
                    parameters.setPreviewSize(size.width,size.height);
                    parameters.setPictureSize(size.width,size.height);
                    break;
                }
            }
            // Set parameters for camera
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            Camera.Size size1 = camera.getParameters().getPictureSize();
            Log.i("PIC","A"+size1.width+"L"+size1.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    camera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                }
                else {
                    // This is an undocumented although widely known feature
                    parameters.set("orientation", "landscape");
                    // For Android 2.2 and above
                    camera.setDisplayOrientation(0);
                    // Uncomment for Android 2.0 and above
                    parameters.setRotation(0);
                }
                camera.setPreviewDisplay(holder);
                camera.startPreview();

            } catch (IOException e) {
                // left blank for now
            }
        }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}