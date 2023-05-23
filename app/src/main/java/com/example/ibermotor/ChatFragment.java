package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.DatabaseError;

public class ChatFragment extends Fragment {

    private DatabaseReference mensajesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Obtén una referencia a la base de datos de Firebase y a la ubicación de los mensajes
        mensajesRef = FirebaseDatabase.getInstance().getReference("chats").child("chat1").child("mensajes");

        EditText barraMensaje = view.findViewById(R.id.barraMensaje);
        Button enviarButton = view.findViewById(R.id.enviarButton);

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = barraMensaje.getText().toString().trim();
                if (!mensaje.isEmpty()) {
                    String mensajeId = mensajesRef.push().getKey();
                    Mensaje nuevoMensaje = new Mensaje(mensaje);
                    mensajesRef.child(mensajeId).setValue(nuevoMensaje);
                    barraMensaje.setText("");
                }
            }
        });

        ChildEventListener mensajesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje mensaje = snapshot.getValue(Mensaje.class);
                recibirMensajeContrario(mensaje.getContenido());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Implementa los demás métodos del ChildEventListener según tus necesidades

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja la cancelación de la lectura de mensajes si es necesario
            }
        };

        mensajesRef.addChildEventListener(mensajesListener);

        return view;
    }

    private void recibirMensajeContrario(String mensaje) {
        // Implementa la lógica para recibir y mostrar los mensajes en la interfaz de usuario
    }
}
