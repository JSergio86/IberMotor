package com.example.ibermotor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.nikartm.button.FitButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;


public class FiltrosFragment extends Fragment {

    NavController navController;
    TextView desdeAñoTextView, desdePuertasTextView;
    ImageView volver;
    TextInputEditText modeloEditText, potenciaEditText, kmEditText, precioDesdeEditText, precioHastaEditText, ciudadEditText;
    AutoCompleteTextView marcaAutoCompleteTextView, combustibleAutoCompleteTextView, colorAutoCompleteTextView, cambioAutoCompleteTextView;
    FitButton botonFiltrar;
    Slider lineaAñoSlider, lineapuertasSlider;
    int intValueAño,intValuePuerta;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volverAtras);
        lineaAñoSlider = view.findViewById(R.id.lineaAñoSlider);
        lineapuertasSlider = view.findViewById(R.id.lineaPuertasSlider);
        desdeAñoTextView = view.findViewById(R.id.desdeAñoNumeroTextView);
        desdePuertasTextView = view.findViewById(R.id.desdepuertasTextView);
        modeloEditText = view.findViewById(R.id.modeloEditText);
        potenciaEditText = view.findViewById(R.id.potenciaEditText);
        kmEditText = view.findViewById(R.id.kmEditText);
        precioDesdeEditText = view.findViewById(R.id.precioDesdeEditText);
        precioHastaEditText = view.findViewById(R.id.precioHastaEditText);
        ciudadEditText = view.findViewById(R.id.ciudadEditText);

        botonFiltrar = view.findViewById(R.id.botonFiltrar);

        marcaAutoCompleteTextView = view.findViewById(R.id.marcaAutoCompleteTextView);
        combustibleAutoCompleteTextView = view.findViewById(R.id.combustibleAutoCompleteTextView);
        colorAutoCompleteTextView = view.findViewById(R.id.colorAutoCompleteTextView);
        cambioAutoCompleteTextView = view.findViewById(R.id.cambioAutoCompleteTextView);

        modeloEditText.setText("Todos");
        potenciaEditText.setText("Todos");
        kmEditText.setText("Todos");
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



        lineaAñoSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                float selectedValues = lineaAñoSlider.getValue();
                lineaAñoSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);

                intValueAño = (int) selectedValues;
                desdeAñoTextView.setText(intValueAño+"");
            }
        });

        lineapuertasSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                float selectedValues = lineapuertasSlider.getValue();
                lineapuertasSlider.setLabelBehavior(LabelFormatter.LABEL_GONE);

                intValuePuerta = (int) selectedValues;

                desdePuertasTextView.setText(intValuePuerta+"");
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
                bundle.putString("potencia", potenciaEditText.getText().toString());
                bundle.putString("km", kmEditText.getText().toString());
                bundle.putString("precioDesde", precioDesdeEditText.getText().toString());
                bundle.putString("precioHasta", precioHastaEditText.getText().toString());
                bundle.putString("marca",  marcaAutoCompleteTextView.getText().toString());
                bundle.putString("combustible", combustibleAutoCompleteTextView.getText().toString());
                bundle.putString("color", colorAutoCompleteTextView.getText().toString());
                bundle.putString("cambio", cambioAutoCompleteTextView.getText().toString());
                bundle.putString("ciudad", ciudadEditText.getText().toString());
                bundle.putString("año", String.valueOf(intValueAño));
                bundle.putString("puertas", String.valueOf(intValuePuerta));


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