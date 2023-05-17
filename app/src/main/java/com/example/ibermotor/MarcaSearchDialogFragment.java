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
        List<String> opcionesList = new ArrayList<>();
        opcionesList.add("Audi");
        opcionesList.add("BMW");
        opcionesList.add("Mercedes-Benz");
        opcionesList.add("Toyota");
        opcionesList.add("Honda");
        opcionesList.add("Ford");
        opcionesList.add("Chevrolet");
        opcionesList.add("Volkswagen");
        opcionesList.add("Nissan");
        opcionesList.add("Hyundai");
        opcionesList.add("Kia");
        opcionesList.add("Volvo");
        opcionesList.add("Mazda");
        opcionesList.add("Subaru");
        opcionesList.add("Ferrari");

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, opcionesList);
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
