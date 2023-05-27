package com.example.ibermotor;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DescripcionCocheFragment extends Fragment {
    NavController navController;
    ImageView volver, iconoFavorito, chatIconBackground, iconoCompartir;
    AppViewModel appViewModel;
    TextView descripcion, nombreText, horasText, precioText, nombreUbicacion, kilometrosText, añosText, combustibleText, puertasText, cambioText, potenciaText, colorText, userName;
    CircleImageView fotoPerfil;
    String uidPost, uidUsuarioPost;

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
        kilometrosText = view.findViewById(R.id.kilometrosText);
        añosText = view.findViewById(R.id.añosText);
        combustibleText = view.findViewById(R.id.combustibleText);
        puertasText = view.findViewById(R.id.puertasText);
        cambioText = view.findViewById(R.id.cambioText);
        potenciaText = view.findViewById(R.id.potenciaText);
        colorText = view.findViewById(R.id.colorText);
        fotoPerfil = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.userName);
        iconoFavorito = view.findViewById(R.id.iconoFavorito);
        chatIconBackground = view.findViewById(R.id.chatIconBackground);
        iconoCompartir = view.findViewById(R.id.iconoCompartir);


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

         uidPost = getArguments().getString("postId");
         uidUsuarioPost = getArguments().getString("uid");


        String uidUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getUid();


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
                kilometrosText.setText(post.kilometros + "km");
                añosText.setText(post.año + "");
                combustibleText.setText(post.combustible);
                puertasText.setText(post.puertas + " puertas");
                cambioText.setText(post.cambioMarchas + "");
                potenciaText.setText(post.potencia + "CV");
                colorText.setText(post.color + "");

                List<String> favoritos = post.favoritos;
                if (favoritos != null && favoritos.contains(uidUsuarioActual)) {
                    iconoFavorito.setImageResource(R.drawable.baseline_star_24);
                } else {
                    iconoFavorito.setImageResource(R.drawable.baseline_star_border_24);
                }

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
                iconoFavorito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore.getInstance().collection("posts").document(uidPost)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        List<String> favoritos = (List<String>) documentSnapshot.get("favoritos");
                                        if (favoritos != null && favoritos.contains(uidUsuarioActual)) {
                                            // Si el usuario ya está en la lista de favoritos, lo quitamos
                                            favoritos.remove(uidUsuarioActual);
                                            iconoFavorito.setImageResource(R.drawable.baseline_star_border_24);
                                        } else {
                                            // Si el usuario no está en la lista de favoritos, lo agregamos
                                            if (favoritos == null) {
                                                favoritos = new ArrayList<>();
                                            }
                                            favoritos.add(uidUsuarioActual);
                                            iconoFavorito.setImageResource(R.drawable.baseline_star_24);
                                        }

                                        // Actualizar la lista de favoritos en el documento del post
                                        FirebaseFirestore.getInstance().collection("posts").document(uidPost)
                                                .update("favoritos", favoritos);
                                    }
                                });
                    }
                });
            }
        });

        chatIconBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uidUsuarioPost.equals(uidUsuarioActual)){

                }
                else{
                    checkChatExistence();
                }
            }
        });

    }


    private void checkChatExistence() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String postUserId = uidUsuarioPost;

        // Combinar los ID de usuario en orden alfabético
        String userPair;
        if (currentUserId.compareTo(postUserId) < 0) {
            userPair = currentUserId + "_" + postUserId;
        } else {
            userPair = postUserId + "_" + currentUserId;
        }

        // Consulta la colección de chats
        FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("users", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Chat chat = document.toObject(Chat.class);
                            if (chat != null && chat.getUsers().contains(postUserId)) {
                                // Si se encuentra un chat existente, navega al ChatFragment
                                navigateToChatFragment(chat);
                                return;
                            }
                        }

                        // Si no se encuentra un chat existente, crea uno nuevo
                        createNewChat(postUserId);
                    } else {
                        // Maneja el error
                    }
                });
    }

    private void createNewChat(String postUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Crear un nuevo documento de chat en Firestore
        List<String> participants = Arrays.asList(currentUserId, postUserId);
        long timestamp = System.currentTimeMillis();
        Chat newChat = new Chat(null, participants, timestamp);

        // Crear un nuevo chat en la colección de chats
        db.collection("chats")
                .add(newChat)
                .addOnSuccessListener(documentReference -> {
                    String chatId = documentReference.getId();
                    newChat.setChatId(chatId); // Establece el ID del chat en el objeto Chat

                    // Actualizar el documento de chat con el nuevo chatId
                    db.collection("chats").document(chatId)
                            .update("chatId", chatId)
                            .addOnSuccessListener(aVoid -> {
                                // Navegar al ChatFragment
                                navigateToChatFragment(newChat);
                            })
                            .addOnFailureListener(e -> Log.e("ChatsHomeFragment", "Error updating chatId", e));
                })
                .addOnFailureListener(e -> Log.e("ChatsHomeFragment", "Error creating chat", e));
    }

    private void navigateToChatFragment(Chat chat) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_chat", chat);
        navController.navigate(R.id.chatFragment, bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_descripcion_coche, container, false);
    }
}