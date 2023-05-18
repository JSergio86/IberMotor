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


public class CombustibleSearchDialogFragment extends DialogFragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private OnCombustibleSelectedListener combustibleSelectedListener;
    private SearchView searchView;

    public interface OnCombustibleSelectedListener {
        void onCombustibleSelected(String combustible);
    }

    public void setCombustibleSelectedListener(OnCombustibleSelectedListener listener) {
        combustibleSelectedListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_combustible_search_dialog, container, false);

        listView = rootView.findViewById(R.id.listViewCombustible);
        searchView = rootView.findViewById(R.id.searchViewCombustible);

        mostrarOpciones();
        configurarBusqueda();

        return rootView;
    }

    private void mostrarOpciones() {
        List<String> opcionesList = new ArrayList<>();
        opcionesList.add("Gasolina");
        opcionesList.add("Diesel");
        opcionesList.add("Hibrido");
        opcionesList.add("Electrico");
        opcionesList.add("GLP");
        opcionesList.add("Otro");

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, opcionesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String combusitble = (String) parent.getItemAtPosition(position);
                if (combustibleSelectedListener != null) {
                    combustibleSelectedListener.onCombustibleSelected(combusitble);
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
                buscarCombustible(newText);
                return true;
            }
        });
    }

    private void buscarCombustible(String query) {
        adapter.getFilter().filter(query);
    }
}