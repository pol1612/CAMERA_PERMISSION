package cat.uvic.teknos.m08.pol.sanejove.camera_permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_CAPTURE_IMAGE = 100;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
    }
    public void onCapture(View view){
        onCaptureCamara();

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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Usuari ok
                onCaptureCamara();
            }else{
                // Usuari ko
                // No se’ns ha otorgat permís, aquesta funcionalitat no està habilitada
            }

    }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}























