package com.example.galeria_fotos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String currentPhotoPath = "";
    static ArrayList<File> listImages = new ArrayList<File>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takeF = findViewById(R.id.foto);
        Button makef = findViewById(R.id.makef);
        Button fSize = findViewById(R.id.fullsize);
        Button recycleView = findViewById(R.id.rview);
        ImageView f = findViewById(R.id.imageView);

        if(getImages().size() > 0) {
            Bitmap firstImage = BitmapFactory.decodeFile(getImages().get(getImages().size() - 1).getAbsolutePath());
            f.setImageBitmap(firstImage);
        }

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            f.setImageURI(uri);
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> someActivityResultLauncherV2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            f.setImageBitmap(imageBitmap);
                        }

                    }
                }
        );

        takeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //Launch activity to get result
                someActivityResultLauncher.launch(intent);
            }
        });

        makef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Launch activity to get result
                someActivityResultLauncherV2.launch(intent);
            }
        });

        fSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        recycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listImages = getImages();
                Intent mIntent = new Intent(MainActivity.this, RecicleViewActivity.class);
                MainActivity.this.startActivity(mIntent);
            }
        });
    }

    private ArrayList<File> getImages(){
        ArrayList<File> listafinal = new ArrayList<File>();
        try{
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            if(storageDir != null && storageDir.isDirectory()) {
                File[] archivos = storageDir.listFiles();

                if (archivos != null) {
                    for (File f : archivos) {
                        listafinal.add(f);
                    }
                }
            }

        } catch (Exception e){}
        return listafinal;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.dani.pr22_captura_app_externa.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            someActivityResultLauncherV3.launch(takePictureIntent);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncherV3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageURI(Uri.parse(currentPhotoPath));
                    }
                }
            }
    );
}