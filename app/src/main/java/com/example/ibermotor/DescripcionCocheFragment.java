package com.example.ibermotor;

import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DescripcionCocheFragment extends Fragment {

    NavController navController;
    ImageView volver;
    FitButton botonChatPaco;
    AppViewModel appViewModel;
    TextView descripcion, nombreText, horasText, precioText, nombreUbicacion, ciudadText, kilometrosText, añosText, combustibleText, puertasText, cambioText, potenciaText, colorText, userName;
    CircleImageView fotoPerfil;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        volver = view.findViewById(R.id.volverAtras);
        descripcion = view.findViewById(R.id.descripcion);
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
        fotoPerfil = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);




        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.homeFragment);

            }
        });


        appViewModel.postSeleccionado.observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                descripcion.setText(post.descripcion + "");
                nombreText.setText(post.marca + " " + post.modelo);
                Date date = post.date;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fechaHora = sdf.format(date);
                horasText.setText(fechaHora);
                precioText.setText(post.precio + "€");
                nombreUbicacion.setText(post.ciudad);
                ciudadText.setText(post.ciudad);
                kilometrosText.setText(post.kilometros + "km");
                añosText.setText(post.año + "");
                combustibleText.setText(post.combustible);
                puertasText.setText(post.puertas + " puertas");
                cambioText.setText(post.cambioMarchas + "");
                potenciaText.setText(post.potencia + "CV");
                colorText.setText(post.color + "");

                List<String> fotos = post.fotoCoche;
                List<CarouselItem> list = new ArrayList<>();

                ImageCarousel carousel = view.findViewById(R.id.fotosCoches);

                carousel.registerLifecycle(getLifecycle());
                if (!fotos.isEmpty()) {
                    if(fotos.size() == 1){
                        carousel.setInfiniteCarousel(false);
                        String urlFoto = fotos.get(0);
                        list.add(new CarouselItem(urlFoto));
                    }
                    else{
                        for (int i = 0; i < fotos.size(); i++) {
                            String urlFoto = fotos.get(i);
                            list.add(new CarouselItem(urlFoto));
                        }
                    }

                    carousel.setData(list);

                }

                // Obtener la referencia a la colección "usuarios" y al documento con la UID del post
                CollectionReference usuariosRef = FirebaseFirestore.getInstance().collection("usuarios");
                DocumentReference usuarioDocRef = usuariosRef.document(post.uid);

                // Consultar los datos del usuario
                usuarioDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Obtener la foto de perfil y el nombre del usuario del documento
                            String fotoPerfilFirebase = documentSnapshot.getString("fotoPerfil");
                            String nombreUsuario = documentSnapshot.getString("nombre");

                            if (fotoPerfilFirebase != null) {
                                Glide.with(requireActivity())
                                        .load(fotoPerfilFirebase)
                                        .into(fotoPerfil);
                                userName.setText(nombreUsuario);
                            } else {
                                Glide.with(requireActivity())
                                        .load(R.drawable.anonymo)
                                        .into(fotoPerfil);
                                userName.setText(nombreUsuario);
                            }

                        }
                    }
        });
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