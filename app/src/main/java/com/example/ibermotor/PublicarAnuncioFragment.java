package com.example.ibermotor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

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


        AutoCompleteTextView combustibleAutoCompleteTextView = view.findViewById(R.id.combustibleAutoCompleteTextView);
        combustibleAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCombustibleSearchDialog(combustibleAutoCompleteTextView);
            }
        });

        AutoCompleteTextView colorAutoCompleteTextView = view.findViewById(R.id.colorAutoCompleteTextView);
        colorAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSearchDialog(colorAutoCompleteTextView);
            }
        });

        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        int childCount = gridLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = gridLayout.getChildAt(i);

            if (childView instanceof Button) {
                Button button = (Button) childView;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Deseleccionar todos los botones
                        for (int j = 0; j < childCount; j++) {
                            View siblingView = gridLayout.getChildAt(j);
                            siblingView.setSelected(false);
                            if (siblingView instanceof Button) {
                                Button siblingButton = (Button) siblingView;
                                siblingButton.setTextColor(getResources().getColor(R.color.black));
                                siblingButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorButtonUnselected));
                            }
                        }

                        // Seleccionar el botón clicado
                        button.setSelected(true);
                        button.setTextColor(getResources().getColor(R.color.lila));
                        button.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));

                        String option = button.getText().toString();


                    }
                });
            }
        }

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

    private void showCombustibleSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        CombustibleSearchDialogFragment dialogFragment = new CombustibleSearchDialogFragment();
        dialogFragment.setCombustibleSelectedListener(new CombustibleSearchDialogFragment.OnCombustibleSelectedListener() {
            @Override
            public void onCombustibleSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "combustible_search_dialog");
    }

    private void showColorSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        ColorSearchDialogFragment dialogFragment = new ColorSearchDialogFragment();
        dialogFragment.setColorSelectedListener(new ColorSearchDialogFragment.OnColorSelectedListener() {
            @Override
            public void onColorSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }
}