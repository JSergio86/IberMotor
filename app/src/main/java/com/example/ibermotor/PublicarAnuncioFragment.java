package com.example.ibermotor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PublicarAnuncioFragment extends Fragment{
    NavController navController;
    public AppViewModel appViewModel;
    String mediaTipo, marca, modelo, año ,combustibe, puertas, color, kilometros, tipoDeCambio, descripcion;
    Uri mediaUri;
    LinearLayout photoContainer;
    List<RelativeLayout> photoViews;
    ImageView iconoGaleria;
    TextView photoText;
    int maxPhotos = 8;
    FitButton subirAnuncio;
    Button subirFotosBoton;
    AutoCompleteTextView marcaAutoCompleteTextView, combustibleAutoCompleteTextView, colorAutoCompleteTextView, cambioAutoCompleteTextView;
    TextInputEditText modeloEditText, añoEditText, kmEditText, descripcionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publicar_anuncio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        photoContainer = view.findViewById(R.id.photoContainer);
        iconoGaleria = view.findViewById(R.id.galleryIcon);
        photoText = view.findViewById(R.id.photoText);
        subirAnuncio = view.findViewById(R.id.subiranuncio);
        subirFotosBoton = view.findViewById(R.id.subirFotosBoton);
        descripcionEditText = view.findViewById(R.id.descripcionEditText);
        modeloEditText = view.findViewById(R.id.modeloEditText);
        añoEditText = view.findViewById(R.id.añoEditText);
        kmEditText = view.findViewById(R.id.kmEditText);

        photoViews = new ArrayList<RelativeLayout>();

        subirFotosBoton.setEnabled(photoViews.size() < maxPhotos);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        subirAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

        subirFotosBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            this.mediaUri = media.uri;
            this.mediaTipo = media.tipo;
            agregarNuevaFoto(media.uri);
        });

        marcaAutoCompleteTextView = view.findViewById(R.id.marcaAutoCompleteTextView);
        marcaAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarcaSearchDialog(marcaAutoCompleteTextView);
            }
        });

        combustibleAutoCompleteTextView = view.findViewById(R.id.combustibleAutoCompleteTextView);
        combustibleAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCombustibleSearchDialog(combustibleAutoCompleteTextView);
            }
        });

        colorAutoCompleteTextView = view.findViewById(R.id.colorAutoCompleteTextView);
        colorAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSearchDialog(colorAutoCompleteTextView);
            }
        });

        cambioAutoCompleteTextView = view.findViewById(R.id.cambioAutoCompleteTextView);
        cambioAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCambioSearchDialog(cambioAutoCompleteTextView);
            }
        });


        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        int childCount = gridLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = gridLayout.getChildAt(i);

            if (childView instanceof Button) {
               Button button = (Button) childView;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Deseleccionar todos los botones
                        for (int j = 0; j < childCount; j++) {
                            View siblingView = gridLayout.getChildAt(j);
                            siblingView.setSelected(false);
                            if (siblingView instanceof Button) {
                                Button siblingButton = (Button) siblingView;
                                siblingButton.setTextColor(getResources().getColor(R.color.black));
                                siblingButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorButtonUnselected));
                            }
                        }

                        // Seleccionar el botón clicado
                        button.setSelected(true);
                        button.setTextColor(getResources().getColor(R.color.lila));
                        button.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));

                        puertas = button.getText().toString();


                    }
                });
            }
        }

    }

    private void publicar() {
        marca = marcaAutoCompleteTextView.getText().toString();
        modelo = modeloEditText.getText().toString();
        año = añoEditText.getText().toString();
        combustibe = combustibleAutoCompleteTextView.getText().toString();
        //puertas
        color = colorAutoCompleteTextView.getText().toString();
        tipoDeCambio = cambioAutoCompleteTextView.getText().toString();
        kilometros = kmEditText.getText().toString();
        descripcion = descripcionEditText.getText().toString();


        Snackbar.make(requireView(), "Botónn: "+ puertas, Snackbar.LENGTH_LONG).show();


        /*String precioTotalString = precioTotal.getText().toString();
        String precioString = precioText.getText().toString();

        publishButton.setEnabled(false);
        if (mediaTipo == null) {
            guardarEnFirestore(precioTotalString, precioString, null);
        } else {
            pujaIguardarEnFirestore(precioTotalString, precioString);
        }

         */
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

        // Verificar si se ha seleccionado una foto de la galería
        if (uri == null) {
            return; // Si no se seleccionó ninguna foto, no se agrega una nueva
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
        closeIcon.setImageResource(R.drawable.baseline_cancel_white);
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
            subirFotosBoton.setEnabled(false);
        }
    }

    private void eliminarFoto(RelativeLayout photoLayout) {
        photoContainer.removeView(photoLayout);
        photoViews.remove(photoLayout);

        if (photoViews.size() == 0) {
            iconoGaleria.setVisibility(View.VISIBLE);
            photoText.setVisibility(View.VISIBLE);
        }

        if (photoViews.size() < maxPhotos) {
            subirFotosBoton.setEnabled(true);
        }
    }

    private void showMarcaSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        MarcaSearchDialogFragment dialogFragment = new MarcaSearchDialogFragment();
        dialogFragment.setMarcaSelectedListener(new MarcaSearchDialogFragment.OnMarcaSelectedListener() {
            @Override
            public void onMarcaSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }

    private void showCombustibleSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        CombustibleSearchDialogFragment dialogFragment = new CombustibleSearchDialogFragment();
        dialogFragment.setCombustibleSelectedListener(new CombustibleSearchDialogFragment.OnCombustibleSelectedListener() {
            @Override
            public void onCombustibleSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "combustible_search_dialog");
    }

    private void showColorSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        ColorSearchDialogFragment dialogFragment = new ColorSearchDialogFragment();
        dialogFragment.setColorSelectedListener(new ColorSearchDialogFragment.OnColorSelectedListener() {
            @Override
            public void onColorSelected(String modelo) {
                autoCompleteTextView.setText(modelo);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }

    private void showCambioSearchDialog(AutoCompleteTextView autoCompleteTextView) {
        CambioSearchDialogFragment dialogFragment = new CambioSearchDialogFragment();
        dialogFragment.setCambioSelectedListener(new CambioSearchDialogFragment.OnCambioSelectedListener() {
            @Override
            public void onCambioSelected(String cambio) {
                autoCompleteTextView.setText(cambio);
            }
        });

        dialogFragment.show(getChildFragmentManager(), "marca_search_dialog");
    }


}