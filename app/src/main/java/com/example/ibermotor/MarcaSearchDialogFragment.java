package com.example.ibermotor;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.ArrayList;
import java.util.List;

public class MarcaSearchDialogFragment extends DialogFragment {
    private List<String> marcasList;
    private ArrayAdapter<String> adapter;
    private OnMarcaSelectedListener listener;

    public interface OnMarcaSelectedListener {
        void onMarcaSelected(String marca);
    }

    public void setListener(OnMarcaSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marcasList = new ArrayList<>();
        marcasList.add("BMW");
        marcasList.add("Dodge");
        marcasList.add("Renault");
        marcasList.add("Nissan");
        marcasList.add("Ford");
        // Agrega todas las marcas que desees a la lista
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Seleccionar Marca");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_marca_search_dialog, null);
        dialog.setContentView(view);

        ListView listView = view.findViewById(R.id.listViewMarcas);
        SearchView searchView = view.findViewById(R.id.searchViewMarcas);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, marcasList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String marca = adapter.getItem(position);
            if (listener != null) {
                listener.onMarcaSelected(marca);
            }
            dismiss();
        });

        return dialog;
    }
}
