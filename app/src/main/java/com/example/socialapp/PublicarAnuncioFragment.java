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


public class PublicarAnuncioFragment extends Fragment implements MarcaSearchDialogFragment.OnMarcaSelectedListener{
    private MaterialSpinner spinnerMarca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publicar_anuncio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerMarca = view.findViewById(R.id.spinnerMarca);
        spinnerMarca.setOnItemSelectedListener((view1, position, id, item) -> {
            if (position == 0) {
                mostrarDialogoMarcas();
            }
        });
    }

    private void mostrarDialogoMarcas() {
        MarcaSearchDialogFragment dialogFragment = new MarcaSearchDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(getChildFragmentManager(), "MarcaSearchDialogFragment");
    }

    @Override
    public void onMarcaSelected(String marca) {
        spinnerMarca.setText(marca);
    }
}