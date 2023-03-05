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

import com.github.nikartm.button.FitButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


public class PublicarAnuncioFragment extends Fragment {

    NavController navController;   // <-----------------
    ImageView volver;
    FitButton subiranuncio;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        volver = view.findViewById(R.id.volverAtras);
        subiranuncio = view.findViewById(R.id.subiranuncio);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.bottomNavigation).findViewById(R.id.buscar).performClick();
                navController.navigate(R.id.homeFragment);

            }
        });

        subiranuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicToast.make(getContext(), "Subido correctamente", R.drawable.iconocheckchat).show();
                getActivity().findViewById(R.id.bottomNavigation).findViewById(R.id.buscar).performClick();
                navController.navigate(R.id.homeFragment);

            }
        });

        MaterialSpinner spinner = view.findViewById(R.id.spinner);
        spinner.setItems("Todas","BMW", "Dodge", "Renault", "Nissan", "Ford");

        MaterialSpinner spinnerModelo = view.findViewById(R.id.spinnerModelo);
        spinnerModelo.setItems("Todos","E36", "Challenger", "370z", "Mustang");

        MaterialSpinner spinnerVersion = view.findViewById(R.id.spinnerCarroceria);
        spinnerVersion.setItems("Todas","2 puertas", "3 puertas", "4 puertas", "5 puertas");

        MaterialSpinner spinnerCiudad = view.findViewById(R.id.spinnerCombustible);
        spinnerCiudad.setItems("Todos","Gasolina", "Diesel");

        MaterialSpinner spinnerPrecioDesde = view.findViewById(R.id.spinnerAño);
        spinnerPrecioDesde.setItems("Todos","1980-1990", "1990-2000", "2000-2010", "2010-2020");

        MaterialSpinner spinnerPrecioHasta = view.findViewById(R.id.spinnerColor);
        spinnerPrecioHasta.setItems("Todos","Rojo", "Negro", "Blanco", "Azul");

        MaterialSpinner spinnerKilometraje = view.findViewById(R.id.spinnerKilometraje);
        spinnerKilometraje.setItems("Todos","0km", "10000km", "50000km", "100000km");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publicar_anuncio, container, false);
    }
}