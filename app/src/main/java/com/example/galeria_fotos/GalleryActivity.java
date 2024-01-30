package com.example.galeria_fotos;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> imagePaths = getAssetImagePaths();
        List<String> imageUris = convertPathsToUris(imagePaths);

        galleryAdapter = new GalleryAdapter(imageUris);
        recyclerView.setAdapter(galleryAdapter);

        Button backToMainButton = findViewById(R.id.backToMainButton);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Método para obtener la lista de nombres de archivos en la carpeta "assets/images"
    private List<String> getAssetImagePaths() {
        List<String> imagePaths = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try {
            // Cambia "images" por el nombre de la carpeta dentro de "assets" donde se encuentran tus imágenes
            String[] files = assetManager.list("images");

            if (files != null) {
                imagePaths = Arrays.asList(files);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagePaths;
    }

    // Método para convertir los nombres de archivo en URIs
    private List<String> convertPathsToUris(List<String> imagePaths) {
        List<String> imageUris = new ArrayList<>();
        for (String imagePath : imagePaths) {
            // Construye la URI para cada imagen en la carpeta "assets/images"
            String imageUri = "file:///android_asset/images/" + imagePath;
            imageUris.add(imageUri);
        }
        return imageUris;
    }

    // Adaptador para el RecyclerView
    private static class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private List<String> imageUris;

        public GalleryAdapter(List<String> imageUris) {
            this.imageUris = imageUris;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(imageUris.get(position));
        }

        @Override
        public int getItemCount() {
            return imageUris.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.galleryImageView);
            }

            public void bind(String imageUri) {
                // Cargar la imagen utilizando Glide
                Glide.with(itemView.getContext())
                        .load(imageUri)
                        .into(imageView);
            }
        }
    }
}
