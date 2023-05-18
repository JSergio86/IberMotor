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


public class ColorSearchDialogFragment extends DialogFragment {
        private ListView listView;
        private ArrayAdapter<String> adapter;
        private OnColorSelectedListener colorSelectedListener;
        private SearchView searchView;

        public interface OnColorSelectedListener {
            void onColorSelected(String color);
        }

        public void setColorSelectedListener(OnColorSelectedListener listener) {
            colorSelectedListener = listener;
    }

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_search_dialog, container, false);

        listView = rootView.findViewById(R.id.listViewColor);
        searchView = rootView.findViewById(R.id.searchViewColor);

        mostrarOpciones();
        configurarBusqueda();

        return rootView;
    }

    private void mostrarOpciones() {
        List<String> opcionesList = new ArrayList<>();
        opcionesList.add("Rojo");
        opcionesList.add("Azul");
        opcionesList.add("Verde");
        opcionesList.add("Amarillo");
        opcionesList.add("Blanco");
        opcionesList.add("Negro");
        opcionesList.add("Gris");
        opcionesList.add("Naranja");
        opcionesList.add("Morado");
        opcionesList.add("Rosa");
        opcionesList.add("Marrón");
        opcionesList.add("Celeste");


        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, opcionesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);
                if (colorSelectedListener != null) {
                    colorSelectedListener.onColorSelected(color);
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
                buscarColor(newText);
                return true;
            }
        });
    }

        private void buscarColor(String query) {
        adapter.getFilter().filter(query);
    }
    }