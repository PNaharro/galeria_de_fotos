package com.example.galeria_fotos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LAST_CAPTURED_IMAGE_URI_KEY = "last_captured_image_uri";

    private ImageView imageView;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button openGalleryButton = findViewById(R.id.openGalleryButton);
        Button captureThumbnailButton = findViewById(R.id.captureThumbnailButton);
        Button openGalleryActivityButton = findViewById(R.id.openGalleryActivityButton);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                if (imageUri != null) {
                                    saveLastCapturedImageUri(imageUri);
                                    imageView.setImageURI(imageUri);
                                }
                            }
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                if (imageUri != null) {
                                    saveLastCapturedImageUri(imageUri);
                                    imageView.setImageURI(imageUri);
                                }
                            }
                        }
                    }
                });

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        captureThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureThumbnail();
            }
        });
        openGalleryActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryActivity();
            }
        });

        // Mostrar la última foto capturada al abrir la aplicación
        Uri lastCapturedImageUri = getLastCapturedImageUri();
        if (lastCapturedImageUri != null) {
            imageView.setImageURI(lastCapturedImageUri);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }
    private void openGalleryActivity() {
        Intent galleryActivityIntent = new Intent(MainActivity.this, GalleryActivity.class);
        startActivity(galleryActivityIntent);
    }

    private void captureThumbnail() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(captureIntent);
    }

    // Métodos para almacenar y recuperar la última URI de la imagen capturada en SharedPreferences
    private void saveLastCapturedImageUri(Uri imageUri) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_CAPTURED_IMAGE_URI_KEY, imageUri.toString());
        editor.apply();
    }

    private Uri getLastCapturedImageUri() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String uriString = preferences.getString(LAST_CAPTURED_IMAGE_URI_KEY, null);
        return (uriString != null) ? Uri.parse(uriString) : null;
    }
}
