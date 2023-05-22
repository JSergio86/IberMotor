package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nikartm.button.FitButton;

import java.util.List;

public class DescripcionCocheFragment extends Fragment {

    NavController navController;
    ImageView volver, fotoCoche;
    FitButton botonChatPaco;
    AppViewModel appViewModel;
    TextView descripcion, nombreText, horasText, precioText, nombreUbicacion, ciudadText, kilometrosText, añosText, combustibleText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        volver = view.findViewById(R.id.volverAtras);
        botonChatPaco = view.findViewById(R.id.botonChatPaco);
        descripcion = view.findViewById(R.id.descripcion);
        fotoCoche = view.findViewById(R.id.fotoCoche);
        nombreText = view.findViewById(R.id.nombreText);
        horasText = view.findViewById(R.id.horasText);
        precioText = view.findViewById(R.id.precioText);
        nombreUbicacion = view.findViewById(R.id.nombreUbicacion);
        ciudadText = view.findViewById(R.id.ciudadText);
        kilometrosText = view.findViewById(R.id.kilometrosText);
        añosText = view.findViewById(R.id.añosText);
        combustibleText = view.findViewById(R.id.combustibleText);


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);

            }
        });

        botonChatPaco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.chatPacoFragment);

            }
        });

        appViewModel.postSeleccionado.observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                List<String> fotos = post.fotoCoche;

                // Verificar si hay fotos disponibles
                if (!fotos.isEmpty()) {
                    // Obtener la URL de la primera foto
                    String urlPrimeraFoto = fotos.get(0);

                    // Cargar la imagen en el ImageView usando Glide
                    Glide.with(requireView())
                            .load(urlPrimeraFoto)
                            .into(fotoCoche);
                }

                descripcion.setText(post.descripcion+"");
                nombreText.setText(post.marca+" "+post.modelo);
                horasText.setText(post.date+"");
                precioText.setText(post.precio+"€");
                nombreUbicacion.setText(post.ciudad);
                ciudadText.setText(post.ciudad);
                kilometrosText.setText(post.kilometros);
                añosText.setText(post.año+"");
                combustibleText.setText(combustibleText+"");
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_descripcion_coche, container, false);
    }
}