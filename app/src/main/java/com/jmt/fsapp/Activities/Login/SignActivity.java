package com.jmt.fsapp.Activities.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
        mSignaturePad.setPenColor(Color.rgb(38,97,180));
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
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

}
