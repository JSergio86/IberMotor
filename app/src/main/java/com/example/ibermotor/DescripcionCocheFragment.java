package com.example.ibermotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nikartm.button.FitButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DescripcionCocheFragment extends Fragment {

    NavController navController;
    ImageView volver, fotosCoches;
    FitButton botonChatPaco;
    AppViewModel appViewModel;
    TextView descripcion, nombreText, horasText, precioText, nombreUbicacion, ciudadText, kilometrosText, añosText, combustibleText, puertasText, cambioText, potenciaText, colorText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        volver = view.findViewById(R.id.volverAtras);
        botonChatPaco = view.findViewById(R.id.botonChatPaco);
        descripcion = view.findViewById(R.id.descripcion);
        fotosCoches = view.findViewById(R.id.fotosCoches);
        nombreText = view.findViewById(R.id.nombreText);
        horasText = view.findViewById(R.id.horasText);
        precioText = view.findViewById(R.id.precioText);
        nombreUbicacion = view.findViewById(R.id.nombreUbicacion);
        ciudadText = view.findViewById(R.id.ciudadText);
        kilometrosText = view.findViewById(R.id.kilometrosText);
        añosText = view.findViewById(R.id.añosText);
        combustibleText = view.findViewById(R.id.combustibleText);
        puertasText = view.findViewById(R.id.puertasText);
        cambioText = view.findViewById(R.id.cambioText);
        potenciaText = view.findViewById(R.id.potenciaText);
        colorText = view.findViewById(R.id.colorText);



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
                    Log.d("URL", urlPrimeraFoto);

                    // Cargar la imagen en el ImageView usando Glide
                    Glide.with(fotosCoches)
                            .load(urlPrimeraFoto)
                            .into(fotosCoches);
                }

                descripcion.setText(post.descripcion+"");
                nombreText.setText(post.marca+" "+post.modelo);
                Date date = post.date;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fechaHora = sdf.format(date);
                horasText.setText(fechaHora);
                precioText.setText(post.precio+"€");
                nombreUbicacion.setText(post.ciudad);
                ciudadText.setText(post.ciudad);
                kilometrosText.setText(post.kilometros);
                añosText.setText(post.año+"");
                combustibleText.setText(post.combustible);
                puertasText.setText(post.puertas+"");
                cambioText.setText(post.cambioMarchas+"");
                potenciaText.setText(post.potencia+"");
                colorText.setText(post.color+"");

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