package com.example.socialapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class FiltrosFragment extends Fragment {

    NavController navController;   // <-----------------
    ImageView volver;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        volver = view.findViewById(R.id.volverAtras);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);

            }
        });

        MaterialSpinner spinner = view.findViewById(R.id.spinner);
        spinner.setItems("Todas","BMW", "Dodge", "Renault", "Nissan", "Ford");

        MaterialSpinner spinnerModelo = view.findViewById(R.id.spinnerModelo);
        spinnerModelo.setItems("Todos","E36", "Challenger", "370z", "Mustang");

        MaterialSpinner spinnerVersion = view.findViewById(R.id.spinnerVersion);
        spinnerVersion.setItems("Todas","2 puertas", "3 puertas", "4 puertas", "5 puertas");

        MaterialSpinner spinnerCiudad = view.findViewById(R.id.spinnerCiudad);
        spinnerCiudad.setItems("Todas","Barcelona", "Badalona", "Madrid", "Sevilla");

        MaterialSpinner spinnerPrecioDesde = view.findViewById(R.id.spinnerPrecioDesde);
        spinnerPrecioDesde.setItems("Desde","1000€", "10000€", "50000€", "100000€");

        MaterialSpinner spinnerPrecioHasta = view.findViewById(R.id.spinnerPrecioHasta);
        spinnerPrecioHasta.setItems("Hasta","1000€", "10000€", "50000€", "100000€");

        MaterialSpinner spinnerKilometraje = view.findViewById(R.id.spinnerKilometraje);
        spinnerKilometraje.setItems("Todos","0km", "10000km", "50000km", "100000km");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }
}