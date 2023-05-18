package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class PublicarAnuncioFragment extends Fragment{
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

        AutoCompleteTextView marcaAutoCompleteTextView = view.findViewById(R.id.marcaAutoCompleteTextView);
        marcaAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarcaSearchDialog(marcaAutoCompleteTextView);
            }
        });


        TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarcaSearchDialog(autoCompleteTextView);
            }
        });

        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarcaSearchDialog(autoCompleteTextView);
            }
        });

    }
    private void showMarcaSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        MarcaSearchDialogFragment dialogFragment = new MarcaSearchDialogFragment();
        dialogFragment.setMarcaSelectedListener(new MarcaSearchDialogFragment.OnMarcaSelectedListener() {
            @Override
            public void onMarcaSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }
}

/*
        Spinner spinnerModelo = view.findViewById(R.id.spinnerModelo);
        EditText editTextModelo = view.findViewById(R.id.editTextModelo);

        spinnerModelo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MarcaSearchDialogFragment dialogFragment = new MarcaSearchDialogFragment();
                    dialogFragment.setMarcaSelectedListener(new MarcaSearchDialogFragment.OnMarcaSelectedListener() {
                        @Override
                        public void onMarcaSelected(String modelo) {
                            editTextModelo.setText(modelo);
                        }
                    });
                    dialogFragment.show(getChildFragmentManager(), "modelo_search_dialog");
                    return true;
                }
                return false;
            }
        });

         @Override
    public void onMarcaSelected(String marca) {
        spinnerMarca.setText(marca);
    }

         */