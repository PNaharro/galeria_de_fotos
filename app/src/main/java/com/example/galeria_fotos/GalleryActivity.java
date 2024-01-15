package com.example.galeria_fotos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.recyclerView);
        Button openGalleryButton = findViewById(R.id.backButton);
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Obtén el conjunto de datos de imágenes
        List<File> imageFiles = getImageFiles();

        // Calcula el número de columnas en función de la cantidad de imágenes
        int spanCount = Math.max(1, Math.min(3, imageFiles.size()));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        // Inicializa el ImageAdapter después de configurar el RecyclerView
        imageAdapter = new ImageAdapter(imageFiles);
        recyclerView.setAdapter(imageAdapter);
    }

    private void openGallery() {
        Intent galleryActivityIntent = new Intent(GalleryActivity.this, MainActivity.class);
        startActivity(galleryActivityIntent);
    }

    private List<File> getImageFiles() {
        List<File> imageFiles = new ArrayList<>();

        // Obtiene el directorio de "Pictures" en la memoria externa
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.i("aaaaaaaa","aaaaaaaaaaaaaa");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Verifica si el archivo es un archivo y tiene la extensión .jpg o .png
                    if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                        imageFiles.add(file);
                    }
                }
            }
        }

        return imageFiles;
    }
}
