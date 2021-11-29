package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.jmt.fsapp.POJO.ReciboDeSueldo;
import com.jmt.fsapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.itextpdf.text.html.HtmlTags.IMG;

public class ReciboDeSueldoViewer extends AppCompatActivity {
    private Activity activity = this;
    private static final int ASK_QUESTION_PROFILE = 1;
    private FirebaseStorage storage;
    private ReciboDeSueldo recibo;
    StorageReference httpsReference;
    static public StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_de_sueldo_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        recibo = (ReciboDeSueldo) i.getSerializableExtra("recibo");
        loadPDF(new FirebaseCallbackPDF() {
            @Override
            public void onCallback(byte[] bytes) {
                showPDF(bytes);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,MisRecibosDeSueldo.class);
        startActivity(intent);
        finish();
    }

    public boolean onPrepareOptionsMenu (Menu menu){

        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow_recibos_de_sueldo,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.itemfirmarrecibo){
            Intent intent = new Intent(activity,SignActivity.class);
            intent.putExtra("return-data", true);
            startActivityForResult(intent,ASK_QUESTION_PROFILE);
        }
        if(id == R.id.itemdescargarrecibo){

        }
        return super.onOptionsItemSelected(item);
    }
    private interface FirebaseCallbackPDF{
        void onCallback(byte[] bytes) throws IOException, DocumentException;
    }
    public void loadPDF(final FirebaseCallbackPDF firebaseCallbackPDF){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        httpsReference = storage.getReferenceFromUrl(recibo.getUrl());
        final long FOUR_MEGABYTE = 2048 * 2048;
        httpsReference.getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    firebaseCallbackPDF.onCallback(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void showPDF(byte[] bytes){
        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromBytes(bytes)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
            loadPDF(new FirebaseCallbackPDF() {
                @Override
                public void onCallback(byte[] bytes) throws IOException, DocumentException {
                    Bitmap signature = BitmapFactory.decodeStream(activity.openFileInput("signature"));
                    byte[] signedRecibo = manipulatePdf(bytes,signature);
                    showPDF(signedRecibo);
                    UploadTask uploadTask = httpsReference.putBytes(signedRecibo);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("RecibosDeSueldo").child(recibo.getCid()).child(recibo.getTipo()).child(recibo.getMes());
                                    reference.child("firma").setValue("true");
                                    reference.child("url").setValue(downloadUrl.toString());
                                }
                            });
                        }
                    });

                }
            });
        super.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] manipulatePdf(byte[] src, Bitmap signature) throws IOException, DocumentException {
        ByteArrayOutputStream signedRecibo = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, signedRecibo);
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 100, stream3);
        Image image = Image.getInstance(stream3.toByteArray());
        PdfImage stream = new PdfImage(image, "", null);
        stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
        PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
        image.setDirectReference(ref.getIndirectReference());
        image.scaleToFit(150,150);
        image.setAbsolutePosition(210, 265);
        PdfContentByte over = stamper.getOverContent(1);
        over.addImage(image);
        stamper.close();
        reader.close();
        byte[] recibofirmado = signedRecibo.toByteArray();
        return recibofirmado;
    }


}
