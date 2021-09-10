package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jmt.fsapp.Adapter.CameraPreview;
import com.jmt.fsapp.R;

import android.widget.ImageView;

import java.io.ByteArrayOutputStream;


public class CameraScanDoc extends AppCompatActivity {
        private Camera camera;
        private ConstraintLayout constraintLayout;
        private Button capture, tomarotra, salir, guardar;
        private ImageView cuadro;
        private FrameLayout frameLayout;
        private CameraPreview cameraPreview;
        private int viewHeight;
        private int viewWidth;
        private int iviewHeight;
        private int iviewWidth;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        static public StorageReference storageRef;
        Activity activity = this;

    @Override
    protected void onDestroy() {
        camera.release();
        super.onDestroy();
    }

    @Override
        public void onCreate(Bundle savedInstanceState) {

            checkCameraPermission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera_scan_doc);
            IniciarGrafico();
            storageRef = storage.getReference().child("Personal").child(getIntent().getExtras().getString("user")).child(getIntent().getExtras().getString("name")+".jpeg");
            //Abrir camara
            camera = Camera.open();
            cameraPreview = new CameraPreview(this, camera);
            frameLayout.addView(cameraPreview);

        }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, final Camera camera) {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bmp != null) {

                bmp = agjustarImagen(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] byteArray = stream.toByteArray();
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardarFoto(byteArray);
                    }
                });
                frameLayout.setVisibility(View.INVISIBLE);
                guardar.setVisibility(View.VISIBLE);
                tomarotra.setVisibility(View.VISIBLE);
                capture.setVisibility(View.GONE);
                cuadro.setImageBitmap(bmp);
                tomarotra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cuadro.setImageResource(R.mipmap.objetivo);
                        frameLayout.setVisibility(View.VISIBLE);
                        camera.startPreview();
                        guardar.setVisibility(View.GONE);
                        tomarotra.setVisibility(View.GONE);
                        capture.setVisibility(View.VISIBLE);
                    }
                });


            } else {
                System.out.println("bitmap is null");
            }

        }
    };
    private void IniciarGrafico(){

        cuadro =  findViewById(R.id.cuadro);
        capture = findViewById(R.id.captureBut);
        tomarotra = findViewById(R.id.tomarotraButon);
        salir = findViewById(R.id.salircamaraButton);
        frameLayout = findViewById(R.id.frameLayout);
        guardar = findViewById(R.id.guardarfoto);
        constraintLayout = findViewById(R.id.constrainLayout);
        tomarotra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,mPicture);

            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
        ajusteDeCamara();
    }
    private void ajusteDeCamara(){

        ViewTreeObserver viewTreeObserver = frameLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewHeight = frameLayout.getWidth();
                    viewWidth = frameLayout.getWidth();

                    Log.i("Graficos","flH:"+viewHeight+"flW"+viewWidth+"cH:"+iviewHeight+"cW:"+iviewWidth);
                    frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth,viewHeight));
                    ViewGroup.LayoutParams params = cuadro.getLayoutParams();
                    switch (getIntent().getStringExtra("name")){
                        case "PIC":
                            iviewHeight = viewHeight;
                            iviewWidth = viewHeight;
                            cuadro.setScaleType(ImageView.ScaleType.FIT_XY);
                            cuadro.requestLayout();
                            break;
                        default:
                            iviewHeight = viewHeight*2/3;
                            iviewWidth = viewHeight;
                            cuadro.requestLayout();
                            break;
                    }
                    cuadro.setLayoutParams(new ConstraintLayout.LayoutParams(iviewWidth,iviewHeight));
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.frameLayout,ConstraintSet.END,R.id.constrainLayout,ConstraintSet.END);
                    constraintSet.connect(R.id.frameLayout,ConstraintSet.TOP,R.id.constrainLayout,ConstraintSet.TOP);
                    constraintSet.connect(R.id.frameLayout,ConstraintSet.BOTTOM,R.id.constrainLayout,ConstraintSet.BOTTOM);
                    constraintSet.connect(R.id.frameLayout,ConstraintSet.START,R.id.constrainLayout,ConstraintSet.START);
                    constraintSet.connect(R.id.cuadro,ConstraintSet.END,R.id.constrainLayout,ConstraintSet.END);
                    constraintSet.connect(R.id.cuadro,ConstraintSet.TOP,R.id.constrainLayout,ConstraintSet.TOP);
                    constraintSet.connect(R.id.cuadro,ConstraintSet.BOTTOM,R.id.constrainLayout,ConstraintSet.BOTTOM);
                    constraintSet.connect(R.id.cuadro,ConstraintSet.START,R.id.constrainLayout,ConstraintSet.START);
                    constraintSet.applyTo(constraintLayout);
                }
            });
        }


    }
    private void guardarFoto(byte[] data){
        final UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


            }
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Intent intent = new Intent();
                    intent.putExtra("name",getIntent().getExtras().getString("name"));
                    intent.putExtra("URL",downloadUri.toString());
                    intent.putExtra("user",getIntent().getExtras().getString("user"));
                    setResult(RESULT_OK,intent);
                    camera.release();
                    finish();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    private Bitmap agjustarImagen(Bitmap bmp){
        Matrix matrix = new Matrix();
        int bmpHeight;
        int bmpWidth;
        int offsetX = 0;
        int offsetY = 0;
        float sy = (float) frameLayout.getWidth()/bmp.getWidth();
        float sx = (float) frameLayout.getHeight()/bmp.getHeight();
        matrix.postRotate(90);
        matrix.postScale(sx,sy);
        bmp = Bitmap.createBitmap(bmp,offsetX, offsetY, bmp.getWidth(),bmp.getHeight(), matrix, true);
        bmpHeight = cuadro.getHeight();
        bmpWidth = cuadro.getWidth();
        offsetY = (int) ((viewHeight - iviewHeight)/2);
        bmp = Bitmap.createBitmap(bmp,offsetX, offsetY,bmpWidth,bmpHeight);

       return bmp;
    }
    private boolean checkCameraPermission() {
        boolean resp = false;
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 225);
        } else {
            resp = true;
        }
        return resp;
    }

    }