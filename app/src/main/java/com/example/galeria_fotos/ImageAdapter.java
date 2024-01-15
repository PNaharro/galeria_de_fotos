package com.example.galeria_fotos;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<File> imageFiles;

    public ImageAdapter(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        holder.bind(imageFile);
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(File imageFile) {
            // Puedes usar cualquier biblioteca de carga de imágenes aquí
            // Para este ejemplo, simplemente establecemos la imagen directamente desde el archivo
            imageView.setImageURI(Uri.fromFile(imageFile));
        }
    }
}
