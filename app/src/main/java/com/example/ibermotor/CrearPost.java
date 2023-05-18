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
import android.widget.RelativeLayout;
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
    List<RelativeLayout> photoViews;
    int maxPhotos = 4;
    ImageView iconoGaleria;
    TextView photoText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publishButton = view.findViewById(R.id.publishButton);
        navController = Navigation.findNavController(view);
        precioTotal = view.findViewById(R.id.precioTotal);
        precioText = view.findViewById(R.id.precioText);
        photoContainer = view.findViewById(R.id.photoContainer);
        iconoGaleria = view.findViewById(R.id.galleryIcon);
        photoText = view.findViewById(R.id.photoText);
        photoViews = new ArrayList<RelativeLayout>();

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        view.findViewById(R.id.btn_add_photo).setOnClickListener(v -> seleccionarImagen());

        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            this.mediaUri = media.uri;
            this.mediaTipo = media.tipo;
            agregarNuevaFoto(media.uri);
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.photo_size),
                getResources().getDimensionPixelSize(R.dimen.photo_size)
        );
        layoutParams.setMargins(
                getResources().getDimensionPixelSize(R.dimen.photo_margin),
                0,
                getResources().getDimensionPixelSize(R.dimen.photo_margin),
                0
        );

        RelativeLayout relativeLayout = new RelativeLayout(requireContext());
        ImageView imageView = new ImageView(requireContext());
        ImageView closeIcon = new ImageView(requireContext());

        relativeLayout.setLayoutParams(layoutParams);

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(uri).into(imageView);

        closeIcon.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        closeIcon.setImageResource(R.drawable.baseline_cancel_24);
        closeIcon.setScaleType(ImageView.ScaleType.CENTER);
        closeIcon.setVisibility(View.VISIBLE);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarFoto(relativeLayout);
            }
        });

        relativeLayout.addView(imageView);
        relativeLayout.addView(closeIcon);
        photoContainer.addView(relativeLayout);
        photoViews.add(relativeLayout);

        iconoGaleria.setVisibility(View.INVISIBLE);
        photoText.setVisibility(View.INVISIBLE);
        if (photoViews.size() >= maxPhotos) {
            //addImagen.setVisibility(View.GONE);
        }
    }


    private void eliminarFoto(RelativeLayout photoLayout) {
        photoContainer.removeView(photoLayout);
        photoViews.remove(photoLayout);
        if (photoViews.size() == 0) {
            iconoGaleria.setVisibility(View.VISIBLE);
            photoText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crear_post, container, false);
        return rootView;
    }
}
