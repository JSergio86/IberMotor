package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class PublicarAnuncioFragment extends Fragment implements MarcaSearchDialogFragment.OnMarcaSelectedListener{
    private MaterialSpinner spinnerMarca;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publicar_anuncio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        spinnerMarca = view.findViewById(R.id.spinnerMarca);
        spinnerMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarcaSearchDialogFragment dialogFragment = new MarcaSearchDialogFragment();
                dialogFragment.setMarcaSelectedListener(PublicarAnuncioFragment.this);
                dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
            }
        });
    }

    @Override
    public void onMarcaSelected(String marca) {
        spinnerMarca.setText(marca);
    }
}