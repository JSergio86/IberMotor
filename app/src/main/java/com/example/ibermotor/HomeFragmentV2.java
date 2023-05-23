package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import io.github.muddz.styleabletoast.StyleableToast;

public class HomeFragmentV2 extends Fragment {

    NavController navController;   // <-----------------
    ImageView perfil, menuDrawer, iconoVista, iconoFiltro;
    View cuadroInfoCoche , cuadroInfoCocheDodge;
    PopupMenu popupMenu;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        perfil = view.findViewById(R.id.perfil);
        menuDrawer = view.findViewById(R.id.menuDrawer);
        cuadroInfoCoche = view.findViewById(R.id.cuadroInfoCoche);
        cuadroInfoCocheDodge = view.findViewById(R.id.cuadroInfoCoche_dodge);
        iconoVista = view.findViewById(R.id.iconoVista);
        iconoFiltro = view.findViewById(R.id.iconoFiltro);



        popupMenu = new PopupMenu(getContext(), menuDrawer);
        popupMenu.getMenuInflater().inflate(R.menu.menudrawer, popupMenu.getMenu());

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.perfilFragment);
                StyleableToast.makeText(getContext(), "Hello World!", Toast.LENGTH_LONG, R.style.mytoast).show();

            }
        });

        iconoFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.filtrosFragment);
            }
        });
/*
        cuadroInfoCocheDodge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.dodgeDescripcionFragment);

            }
        });

 */

        iconoVista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.homeFragment);

            }
        });

        menuDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.menudrawer);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.notificaciones:
                                navController.navigate(R.id.notificacionesFragment);
                                return true;
                            case R.id.privacidad:
                                navController.navigate(R.id.privacidadFragment);
                                return true;
                            case R.id.ayuda:
                                navController.navigate(R.id.ayudaFragment);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        cuadroInfoCoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.descripcionCocheFragment);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_v2, container, false);
    }
}