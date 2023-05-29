package com.example.ibermotor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    NavController navController;
    ImageView cerrarSesion;
    CircleImageView fotoPerfil;
    TextView nombreText, correoText;
    Button botonEditarPerfil;
    String uid;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        cerrarSesion = view.findViewById(R.id.cerrarSesion);
        fotoPerfil = view.findViewById(R.id.perfil);
        botonEditarPerfil = view.findViewById(R.id.botonEditarPerfil);
        nombreText = view.findViewById(R.id.nombre);
        correoText = view.findViewById(R.id.correo);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid= user.getUid();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            String correo = documentSnapshot.getString("correo");
                            String fotoperfil = documentSnapshot.getString("fotoPerfil");
                            if (fotoperfil != null) {
                                Glide.with(requireActivity())
                                        .load(fotoperfil)
                                        .into(fotoPerfil);
                            } else {
                                Glide.with(requireActivity())
                                        .load(R.drawable.anonymo)
                                        .into(fotoPerfil);
                            }
                            nombreText.setText(nombre);
                            correoText.setText(correo);

                        } else {
                            // El documento no existe
                            Log.d("Documento", "El documento no existe");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener los datos
                        Log.e("Error", "Error al obtener los datos: " + e.getMessage());
                    }
                });


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(view);
                GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build()).signOut();

                FirebaseAuth.getInstance().signOut();

                Navigation.findNavController(view).navigate(R.id.signInFragment);

            }
        });


        botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.editarPerfilFragment);

            }
        });

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        final FrameLayout fragmentContainer = view.findViewById(R.id.fragmentContainer);

        // Crea tus fragments
        Fragment anunciosFragment = new AnunciosFragment();
        Fragment valoracionesFragment = new FavoritosFragment();

        // Configura el TabLayout y asigna los fragments a los tabs
        tabLayout.addTab(tabLayout.newTab().setText("Anuncios Activos"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoritos"));

        // Maneja el evento de selección de tab en el TabLayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = anunciosFragment;
                        break;
                    case 1:
                        selectedFragment = valoracionesFragment;
                        break;
                    default:
                        return;
                }

                // Reemplaza el contenido del fragmentContainer con el fragment seleccionado
                getChildFragmentManager().beginTransaction()
                        .replace(fragmentContainer.getId(), selectedFragment)
                        .commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No es necesario implementar nada aquí
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No es necesario implementar nada aquí
            }
        });

        // Establece el primer fragment (anunciosFragment) como el fragment inicial
        getChildFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), anunciosFragment)
                .commit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
}