package com.example.ibermotor;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AyudaFragment extends Fragment {
    NavController navController;
    ImageView volver;
    View fondoGmail, fondoTelefono;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volverAtras);
        fondoGmail = view.findViewById(R.id.fondoGmail);
        fondoTelefono = view.findViewById(R.id.fondoTelefono);



        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);

            }
        });

        fondoGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dirección de correo electrónico
                String emailAddress = "ibermotor@gmail.com";

                // Crear el intent para abrir la aplicación de Gmail y redactar un correo
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + emailAddress));

                try {
                    // Comprobar si hay alguna aplicación en el dispositivo que pueda manejar el intent
                    PackageManager packageManager = requireActivity().getPackageManager();
                    if (intent.resolveActivity(packageManager) != null) {
                        // Abrir la aplicación de Gmail con el correo electrónico predefinido
                        startActivity(intent);
                    } else {
                        // No se encontró una aplicación para manejar el intent
                        Toast.makeText(requireContext(), "No se encontró una aplicación de correo electrónico", Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    // No se encontró ninguna aplicación compatible para manejar el intent
                    Toast.makeText(requireContext(), "No se encontró ninguna aplicación compatible de correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fondoTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Número de teléfono
                String phoneNumber = "622383826";

                // Crear el intent para abrir la aplicación de teléfono y realizar la llamada
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                try {
                    // Comprobar si hay alguna aplicación en el dispositivo que pueda manejar el intent
                    PackageManager packageManager = requireActivity().getPackageManager();
                    if (intent.resolveActivity(packageManager) != null) {
                        // Abrir la aplicación de teléfono y mostrar el número de teléfono
                        startActivity(intent);
                    } else {
                        // No se encontró una aplicación para manejar el intent
                        Toast.makeText(requireContext(), "No se encontró una aplicación de teléfono", Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    // No se encontró ninguna aplicación compatible para manejar el intent
                    Toast.makeText(requireContext(), "No se encontró ninguna aplicación compatible de teléfono", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ayuda, container, false);
    }
}