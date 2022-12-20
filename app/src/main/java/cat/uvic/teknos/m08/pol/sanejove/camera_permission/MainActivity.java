package cat.uvic.teknos.m08.pol.sanejove.camera_permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int RESULT_CAPTURE_IMAGE = 100;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int RESULT_LOAD_GALERY = 200;
    private static final int PERMISSION_REQUEST_GALERY = 2;
    private ImageView imageView;
    private TextView tv_text;
    private ConstraintLayout contentFrame;
    private ZXingScannerView mScannerView;
    private boolean cameraOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        contentFrame =findViewById(R.id.contentFrame);
        this.tv_text = findViewById(R.id.textView);
        this.mScannerView = new ZXingScannerView(this);
        this.cameraOn = false;
        Log.i("AD_C11", "onCreate");
    }
    public void onCapture(View view){
        //TODO ALERT DIALOG
        //onCaptureCamara();
        //onCaptureGalery();
        onCaptureQR();
    }
    public void startCamera() {
        Log.i("AD_C11", "startCamera");
        contentFrame.addView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        cameraOn = true;
    }
    public void stopCamera() {
        Log.i("AD_C11", "stopCamera");
        mScannerView.stopCamera();
        contentFrame.removeView(mScannerView);
        cameraOn = false;
    }
    private void onCaptureQR() {
        Log.i("AD_C11", "captureQR");
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.startCamera();
            } else {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    private void onCaptureGalery() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
            Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i,RESULT_LOAD_GALERY);
        }else {
            /*if (Build.VERSION.SDK_INT >= 23)*/
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_GALERY);

        }
    }
    public void onCaptureCamara(){
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ){
            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,RESULT_CAPTURE_IMAGE);
        }else {
            /*if (Build.VERSION.SDK_INT >= 23)*/
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RESULT_CAPTURE_IMAGE){
            if (resultCode==RESULT_OK){
                Bitmap photo=(Bitmap) data.getExtras().get("data");
                System.out.println(photo.toString());
                imageView.setImageBitmap(photo);
            }if(resultCode==RESULT_CANCELED) {

            }
        }
        else if (requestCode == RESULT_LOAD_GALERY) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap photo = (Bitmap)MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    this.imageView.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Usuari ha cancelat la captura d'imatge
            } else {
                // La captura d'imatge a fallat, advertir a l'usuari
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Usuari ok
                //onCaptureCamara();
                onCaptureQR();
            }else{
                // Usuari ko
                // No se’ns ha otorgat permís, aquesta funcionalitat no està habilitada
            }

        }else if(requestCode==PERMISSION_REQUEST_GALERY){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Usuari ok
                onCaptureGalery();
            }else{
                // Usuari ko
                // No se’ns ha otorgat permís, aquesta funcionalitat no està habilitada
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void handleResult(Result rawResult) {
        Log.i("AD_C11", "handleResult");
        if (rawResult!=null) {
            rawResult.getRawBytes();
            this.tv_text.setText(rawResult.getText());
        }
        stopCamera();
    }

    @Override
    public void onBackPressed() {
        Log.i("AD_C11", "onBackPressed");
        if (cameraOn) {
            stopCamera();
        } else {
            // Estat per defecte
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause() {
        Log.i("AD_C11", "onPause");
        if (cameraOn)
            stopCamera();
        super.onPause();
    }
}























