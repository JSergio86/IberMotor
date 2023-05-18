package com.example.ibermotor;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.List;

public class MarcaSearchDialogFragment extends DialogFragment {
    private ListView listViewMarcas;
    private ArrayAdapter<String> adapter;
    private OnMarcaSelectedListener marcaSelectedListener;
    private SearchView searchViewMarcas;

    public interface OnMarcaSelectedListener {
        void onMarcaSelected(String marca);
    }

    public void setMarcaSelectedListener(OnMarcaSelectedListener listener) {
        marcaSelectedListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_marca_search_dialog, container, false);

        listViewMarcas = rootView.findViewById(R.id.listViewMarcas);
        searchViewMarcas = rootView.findViewById(R.id.searchViewMarcas);

        mostrarOpciones();
        configurarBusqueda();

        return rootView;
    }

    private void mostrarOpciones() {
        List<String> marcasList = new ArrayList<>();
        marcasList.add("Abarth");
        marcasList.add("Alfa Romeo");
        marcasList.add("Aston Martin");
        marcasList.add("Audi");
        marcasList.add("Bentley");
        marcasList.add("BMW");
        marcasList.add("Cadillac");
        marcasList.add("Caterham");
        marcasList.add("Chevrolet");
        marcasList.add("Citroen");
        marcasList.add("Dacia");
        marcasList.add("Ferrari");
        marcasList.add("Fiat");
        marcasList.add("Ford");
        marcasList.add("Honda");
        marcasList.add("Infiniti");
        marcasList.add("Isuzu");
        marcasList.add("Iveco");
        marcasList.add("Jaguar");
        marcasList.add("Jeep");
        marcasList.add("Kia");
        marcasList.add("KTM");
        marcasList.add("Lada");
        marcasList.add("Lamborghini");
        marcasList.add("Lancia");
        marcasList.add("Land Rover");
        marcasList.add("Lexus");
        marcasList.add("Lotus");
        marcasList.add("Maserati");
        marcasList.add("Mazda");
        marcasList.add("Mercedes-Benz");
        marcasList.add("Mini");
        marcasList.add("Mitsubishi");
        marcasList.add("Morgan");
        marcasList.add("Nissan");
        marcasList.add("Opel");
        marcasList.add("Peugeot");
        marcasList.add("Piaggio");
        marcasList.add("Porsche");
        marcasList.add("Renault");
        marcasList.add("Rolls-Royce");
        marcasList.add("Seat");
        marcasList.add("Skoda");
        marcasList.add("Smart");
        marcasList.add("SsangYong");
        marcasList.add("Subaru");
        marcasList.add("Suzuki");
        marcasList.add("Tata");
        marcasList.add("Tesla");
        marcasList.add("Toyota");
        marcasList.add("Volkswagen");
        marcasList.add("Volvo");

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, marcasList);
        listViewMarcas.setAdapter(adapter);

        listViewMarcas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String marca = (String) parent.getItemAtPosition(position);
                if (marcaSelectedListener != null) {
                    marcaSelectedListener.onMarcaSelected(marca);
                }
                dismiss();
            }
        });
    }

    private void configurarBusqueda() {
        searchViewMarcas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarMarcas(newText);
                return true;
            }
        });
    }

    private void buscarMarcas(String query) {
        adapter.getFilter().filter(query);
    }
}
