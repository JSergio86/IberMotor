package com.example.ibermotor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class PrivacidadFragment extends Fragment {

    NavController navController;
    ImageView volver;
    ImageView switchImage;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volverAtras);
        switchImage = view.findViewById(R.id.switchImage);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);
            }
        });

        switchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPDF();
            }
            });

    }
    private void abrirPDF() {
        File pdfFile = new File(context.getFilesDir(), "DAM_M13_-_Memoria_del_proyecto_-_Victor_Tobaruela_Sergio_Jimenez.pdf");

        Uri pdfUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Maneja la excepción si no hay una aplicación para abrir archivos PDF instalada en el dispositivo
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacidad, container, false);
    }
}

