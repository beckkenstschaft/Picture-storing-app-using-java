package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button cameraBtn, galleryBtn;
    public Intent cameraIntent;
    int CAMERA_REQUEST = 1;
    String IMAGES_FOLDER_NAME = "images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = findViewById(R.id.camera_btn);
        galleryBtn = findViewById(R.id.gallery_btn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN CAMERA

                cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");  // OPEN THE CAMERA
                startActivityForResult(cameraIntent, CAMERA_REQUEST);  //TAKE THE PICTURE

            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN GALLERY
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST){
            if(resultCode == Activity.RESULT_OK){    // MAKE A FUNCTION THAT YOU LIKED THE PICTURE AND SAVE IT
                Bitmap image = (Bitmap) data.getExtras().get("data");
//                Toast.makeText(this, "Image is recieved", Toast.LENGTH_SHORT).show();
                try {
                    if(saveImage(image)){
                        Toast.makeText(this, "Image is saved", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean saveImage(Bitmap bitmap){
        boolean saved = false;

        OutputStream fos;
        String timestamp = new SimpleDateFormat("yyyymmdd_hhmmss").format(new Date());
        String name = "Image_" + timestamp;

        ContentResolver resolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,"DCIM/"+IMAGES_FOLDER_NAME);

        try {
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            fos.flush();
            fos.close();
        }

        catch (IOException e){
            e.printStackTrace();
        }

        return saved;
    }
}