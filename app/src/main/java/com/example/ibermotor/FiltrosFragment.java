package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nikartm.button.FitButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;


public class FiltrosFragment extends Fragment {

    NavController navController;
    TextView desdeAñoTextView, hastaAñoTextView, desdePuertasTextView, hastaPuertasTextView;
    ImageView volver;
    TextInputEditText modeloEditText, potenciaDesdeEditText, potenciaHastaEditText, kmDesdeEditText, kmHastaEditText, precioDesdeEditText, precioHastaEditText, ciudadEditText;
    AutoCompleteTextView marcaAutoCompleteTextView, combustibleAutoCompleteTextView, colorAutoCompleteTextView, cambioAutoCompleteTextView;
    FitButton botonFiltrar;
    RangeSlider lineaAñoSlider, lineapuertasSlider;
    int minIntValueAño = 1970,maxIntValueAño = 2023,minIntValuePuerta = 1,maxIntValuePuerta = 5;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volverAtras);
        lineaAñoSlider = view.findViewById(R.id.lineaAñoSlider);
        lineapuertasSlider = view.findViewById(R.id.lineaPuertasSlider);
        desdeAñoTextView = view.findViewById(R.id.desdeAñoNumeroTextView);
        hastaAñoTextView = view.findViewById(R.id.hastaAñoTextView);
        desdePuertasTextView = view.findViewById(R.id.desdepuertasTextView);
        hastaPuertasTextView = view.findViewById(R.id.hastaPuertaNumeroTextView);
        modeloEditText = view.findViewById(R.id.modeloEditText);
        potenciaDesdeEditText = view.findViewById(R.id.potenciaDesdeEditText);
        potenciaHastaEditText = view.findViewById(R.id.potenciaHastaEditText);
        kmDesdeEditText = view.findViewById(R.id.kmDesdeEditText);
        kmHastaEditText = view.findViewById(R.id.kmHastaEditText);
        precioDesdeEditText = view.findViewById(R.id.precioDesdeEditText);
        precioHastaEditText = view.findViewById(R.id.precioHastaEditText);
        ciudadEditText = view.findViewById(R.id.ciudadEditText);

        botonFiltrar = view.findViewById(R.id.botonFiltrar);

        marcaAutoCompleteTextView = view.findViewById(R.id.marcaAutoCompleteTextView);
        combustibleAutoCompleteTextView = view.findViewById(R.id.combustibleAutoCompleteTextView);
        colorAutoCompleteTextView = view.findViewById(R.id.colorAutoCompleteTextView);
        cambioAutoCompleteTextView = view.findViewById(R.id.cambioAutoCompleteTextView);

        modeloEditText.setText("Todos");
        potenciaDesdeEditText.setText("Todos");
        potenciaHastaEditText.setText("Todos");
        kmDesdeEditText.setText("Todos");
        kmHastaEditText.setText("Todos");
        precioDesdeEditText.setText("Todos");
        precioHastaEditText.setText("Todos");
        marcaAutoCompleteTextView.setText("Todas");
        combustibleAutoCompleteTextView.setText("Todos");
        colorAutoCompleteTextView.setText("Todos");
        cambioAutoCompleteTextView.setText("Todos");
        ciudadEditText.setText("Todas");

        marcaAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarcaSearchDialog(marcaAutoCompleteTextView);
            }
        });

        combustibleAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCombustibleSearchDialog(combustibleAutoCompleteTextView);
            }
        });

        colorAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSearchDialog(colorAutoCompleteTextView);
            }
        });

        cambioAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCambioSearchDialog(cambioAutoCompleteTextView);
            }
        });



        lineaAñoSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> selectedValues = lineaAñoSlider.getValues();
                lineaAñoSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);

                float minValue = selectedValues.get(0);
                float maxValue = selectedValues.get(1);

                minIntValueAño = (int) minValue;
                maxIntValueAño = (int) maxValue;
                desdeAñoTextView.setText(minIntValueAño+"");
                hastaAñoTextView.setText(maxIntValueAño+"");
            }
        });
        lineapuertasSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> selectedValues = lineapuertasSlider.getValues();
                lineapuertasSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);

                float minValue = selectedValues.get(0);
                float maxValue = selectedValues.get(1);

                minIntValuePuerta = (int) minValue;
                maxIntValuePuerta = (int) maxValue;
                desdePuertasTextView.setText(minIntValuePuerta+"");
                hastaPuertasTextView.setText(maxIntValuePuerta+"");
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);

            }
        });

        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un objeto Bundle y agregar los valores
                Bundle bundle = new Bundle();
                bundle.putString("modelo", modeloEditText.getText().toString());
                bundle.putString("potenciaDesde", potenciaDesdeEditText.getText().toString());
                bundle.putString("potenciaHasta", potenciaHastaEditText.getText().toString());
                bundle.putString("kmDesde", kmDesdeEditText.getText().toString());
                bundle.putString("kmHasta", kmHastaEditText.getText().toString());
                bundle.putString("precioDesde", precioDesdeEditText.getText().toString());
                bundle.putString("precioHasta", precioHastaEditText.getText().toString());
                bundle.putString("marca",  marcaAutoCompleteTextView.getText().toString());
                bundle.putString("combustible", combustibleAutoCompleteTextView.getText().toString());
                bundle.putString("color", colorAutoCompleteTextView.getText().toString());
                bundle.putString("cambio", cambioAutoCompleteTextView.getText().toString());
                bundle.putString("ciudad", ciudadEditText.getText().toString());
                bundle.putString("añoDesde", String.valueOf(minIntValueAño));
                bundle.putString("añoHasta", String.valueOf(maxIntValueAño));
                bundle.putString("puertaDesde", String.valueOf(minIntValuePuerta));
                bundle.putString("puertaHasta", String.valueOf(maxIntValuePuerta));


                // Navegar al fragmento de destino y pasar los argumentos
                navController.navigate(R.id.queryFiltros, bundle);

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

    private void showCambioSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        CambioSearchDialogFragment dialogFragment = new CambioSearchDialogFragment();
        dialogFragment.setCambioSelectedListener(new CambioSearchDialogFragment.OnCambioSelectedListener() {
            @Override
            public void onCambioSelected(String cambio) {
                autoCompleteTextView.setText(cambio);
            }
        });

        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }
}