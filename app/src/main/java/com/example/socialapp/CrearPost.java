package com.example.socialapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrearPost extends Fragment {

    Button publishButton;
    EditText precioTotal, precioText;
    NavController navController;   // <-----------------
    public AppViewModel appViewModel;
    String mediaTipo;
    Uri mediaUri;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publishButton = view.findViewById(R.id.publishButton);
        navController = Navigation.findNavController(view);
        precioTotal = view.findViewById(R.id.precioTotal);
        precioText = view.findViewById(R.id.precioText);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        view.findViewById(R.id.imagen_galeria).setOnClickListener(v ->
                seleccionarImagen());
        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            this.mediaUri = media.uri;
            this.mediaTipo = media.tipo;
            Glide.with(this).load(media.uri).into((ImageView) view.findViewById(R.id.previsualizacion));
        });
    }

    private void publicar() {
        String precioTotalString = precioTotal.getText().toString();
        String precioString = precioText.getText().toString();

        publishButton.setEnabled(false);
        if (mediaTipo == null) {
            guardarEnFirestore(precioTotalString, precioString, null);
        }
        else {
            pujaIguardarEnFirestore(precioTotalString, precioString);
        }
    }
    private void guardarEnFirestore(String precioTotalString, String precioString, String mediaUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Post post = new Post(user.getUid(), mediaUrl, mediaTipo, precioTotalString, precioString);
        post.setPrecioTotal(precioTotalString);
        post.setPrecioText(precioString);
        FirebaseFirestore.getInstance().collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        navController.popBackStack();
                        appViewModel.setMediaSeleccionado( null, null);
                        documentReference.update("postId", documentReference.getId());
                    }
                });
    }
    private void pujaIguardarEnFirestore(final String precioTotalString, final String precioString) {
        FirebaseStorage.getInstance().getReference(mediaTipo + "/" + UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url -> guardarEnFirestore(precioTotalString,precioString, url.toString()));
    }

    private final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                appViewModel.setMediaSeleccionado(uri, mediaTipo);
            });

    private void seleccionarImagen() {
        mediaTipo = "image";
        galeria.launch("image/*");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_post, container, false);
    }
}