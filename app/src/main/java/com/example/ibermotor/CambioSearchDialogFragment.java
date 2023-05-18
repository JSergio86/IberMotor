package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


public class CambioSearchDialogFragment extends DialogFragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private OnCambioSelectedListener cambioSelectedListener;
    private SearchView searchView;

    public interface OnCambioSelectedListener {
        void onCambioSelected(String cambio);
    }

    public void setCambioSelectedListener(OnCambioSelectedListener listener) {
        cambioSelectedListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cambio_search_dialog, container, false);

        listView = rootView.findViewById(R.id.listViewCambio);
        searchView = rootView.findViewById(R.id.searchViewCambio);

        mostrarOpciones();
        configurarBusqueda();

        return rootView;
    }

    private void mostrarOpciones() {
        List<String> opcionesList = new ArrayList<>();
        opcionesList.add("Automatico");
        opcionesList.add("Manual");


        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, opcionesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cambio = (String) parent.getItemAtPosition(position);
                if (cambioSelectedListener != null) {
                    cambioSelectedListener.onCambioSelected(cambio);
                }
                dismiss();
            }
        });
    }

    private void configurarBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarCambio(newText);
                return true;
            }
        });
    }

    private void buscarCambio(String query) {
        adapter.getFilter().filter(query);
    }
}