package com.example.ibermotor;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class CrearPost extends Fragment {
    Button publishButton;
    EditText precioTotal, precioText;
    NavController navController;
    public AppViewModel appViewModel;
    private View rootView;
    String mediaTipo;
    Uri mediaUri;
    LinearLayout photoContainer;
    List<ImageView> photoViews;
    int maxPhotos = 4; // Define el número máximo de fotos

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publishButton = view.findViewById(R.id.publishButton);
        navController = Navigation.findNavController(view);
        precioTotal = view.findViewById(R.id.precioTotal);
        precioText = view.findViewById(R.id.precioText);
        photoContainer = view.findViewById(R.id.photoContainer);
        photoViews = new ArrayList<>();

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
            agregarNuevaFoto(media.uri);
        });

        Button addPhotoButton = view.findViewById(R.id.btn_add_photo);
        ImageView galleryIcon = view.findViewById(R.id.galleryIcon);
        TextView photoText = view.findViewById(R.id.photoText);
        ImageView selectedImage = view.findViewById(R.id.selectedImage);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí debes implementar la lógica para seleccionar una imagen
                // y asignar la URI de la imagen seleccionada a la variable 'mediaUri'

                // Mostrar la imagen seleccionada y ocultar el icono de la galería y el texto
                selectedImage.setImageURI(mediaUri);
                selectedImage.setVisibility(View.VISIBLE);
                galleryIcon.setVisibility(View.GONE);
                photoText.setVisibility(View.GONE);
            }
        });
    }

    private void publicar() {
        String precioTotalString = precioTotal.getText().toString();
        String precioString = precioText.getText().toString();

        publishButton.setEnabled(false);
        if (mediaTipo == null) {
            guardarEnFirestore(precioTotalString, precioString, null);
        } else {
            pujaIguardarEnFirestore(precioTotalString, precioString);
        }
    }

    private void guardarEnFirestore(String precioTotalString, String precioString, String mediaUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Post post = new Post(user.getUid(), mediaUrl, mediaTipo, precioTotalString, precioString);
        FirebaseFirestore.getInstance().collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        navController.popBackStack();
                        appViewModel.setMediaSeleccionado(null, null);
                        documentReference.update("postId", documentReference.getId());
                    }
                });
    }

    private void pujaIguardarEnFirestore(final String precioTotalString, final String precioString) {
        FirebaseStorage.getInstance().getReference(mediaTipo + "/" + UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url -> guardarEnFirestore(precioTotalString, precioString, url.toString()));
    }

    private final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                appViewModel.setMediaSeleccionado(uri, mediaTipo);
            });

    private void seleccionarImagen() {
        mediaTipo = "image";
        galeria.launch("image/*");
    }

    private void agregarNuevaFoto(Uri uri) {
        if (photoViews.size() >= maxPhotos) {
            return; // Si se alcanza el número máximo de fotos, no se agrega una nueva
        }

        ImageView imageView = new ImageView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.photo_size),
                getResources().getDimensionPixelSize(R.dimen.photo_size)
        );
        params.setMargins(
                getResources().getDimensionPixelSize(R.dimen.photo_margin),
                0,
                getResources().getDimensionPixelSize(R.dimen.photo_margin),
                0
        );
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(uri).into(imageView);

        photoContainer.addView(imageView);
        photoViews.add(imageView);

        if (photoViews.size() >= maxPhotos) {
            //view.findViewById(R.id.imagen_galeria).setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crear_post, container, false);
        return rootView;
    }
}
