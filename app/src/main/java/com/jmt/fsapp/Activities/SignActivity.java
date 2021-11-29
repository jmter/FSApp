package com.jmt.fsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jmt.fsapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class SignActivity extends AppCompatActivity {
    private SignaturePad mSignaturePad;
    private Activity activity = this;
    private Button clearPad;
    private Button saveSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        iniciarGraficos();
    }
    private void iniciarGraficos(){
        mSignaturePad = findViewById(R.id.signature_pad);
        clearPad = findViewById(R.id.buttonclearfirma);
        saveSignature = findViewById(R.id.buttonsavefirma);

        clearPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });
        saveSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Firma","ReciboFirmado");
                Intent intent = new Intent();
                intent.putExtra("signature",createImageFromBitmap(mSignaturePad.getSignatureBitmap()));
                activity.setResult(RESULT_OK,intent);
                activity.finish();
            }
        });
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "signature";//no .png or .jpg needed
        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
