package com.example.ibermotor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PublicarAnuncioFragment extends Fragment{
    NavController navController;
    public AppViewModel appViewModel;
    String mediaTipo, marca, modelo ,combustibe,año, puertas,color,kilometros,potencia, tipoDeCambio,descripcion,precio, ciudad, codigoPostal;
    int añoInt;

    Uri mediaUri;
    LinearLayout photoContainer;
    List<RelativeLayout> photoViews;
    ImageView iconoGaleria;
    TextView photoText;
    int maxPhotos = 6;
    FitButton subirAnuncio;
    Button subirFotosBoton, button2;
    AutoCompleteTextView marcaAutoCompleteTextView, combustibleAutoCompleteTextView, colorAutoCompleteTextView, cambioAutoCompleteTextView;
    TextInputEditText modeloEditText, añoEditText, kmEditText, potenciaEditText,precioEditText,descripcionEditText, ciudadEditText, codigoPostalEditText;
    TextInputLayout potenciaTextInputLayout, colorTextInputLayout, marcaTextInputLayout, modeloTextInputLayout, añoTextInputLayout, combustibleTextInputLayout, colortextInputLayout, kmTextInputLayout, cambioTextInputLayout, precioTextInputLayout, descripcionTextInputLayout, ciudadTextInputLayout, codigoPostalTextInputLayout;
    private List<String> imageUrls = new ArrayList<>();
    Date date = new Date();
    double currentLatitude = 0, currentLongitude=0;

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
        potenciaEditText = view.findViewById(R.id.potenciaEditText);
        precioEditText = view.findViewById(R.id.precioEditText);
        ciudadEditText = view.findViewById(R.id.ciudadEditText);
        codigoPostalEditText = view.findViewById(R.id.codigoPostalEditText);

        potenciaTextInputLayout = view.findViewById(R.id.potenciaTextInputLayout);
        colorTextInputLayout = view.findViewById(R.id.colortextInputLayout);
        marcaTextInputLayout = view.findViewById(R.id.marcaTextInputLayout);
        modeloTextInputLayout = view.findViewById(R.id.modeloTextInputLayout);
        añoTextInputLayout = view.findViewById(R.id.añoTextInputLayout);
        combustibleTextInputLayout = view.findViewById(R.id.combustibleTextInputLayout);
        kmTextInputLayout = view.findViewById(R.id.kmTextInputLayout);
        cambioTextInputLayout = view.findViewById(R.id.cambioTextInputLayout);
        descripcionTextInputLayout = view.findViewById(R.id.descripcionTextInputLayout);
        ciudadTextInputLayout = view.findViewById(R.id.ciudadTextInputLayout);
        codigoPostalTextInputLayout = view.findViewById(R.id.codigoPostalTextInputLayout);
        precioTextInputLayout = view.findViewById(R.id.precioTextInputLayout);

        button2 = view.findViewById(R.id.button1);

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

                        puertas = (button.getText().toString());
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
        potencia = potenciaEditText.getText().toString();
        precio = precioEditText.getText().toString();
        descripcion = descripcionEditText.getText().toString();
        ciudad = ciudadEditText.getText().toString();
        codigoPostal = codigoPostalEditText.getText().toString();

        // Validar si al menos un campo está en blanco
        boolean camposVacios = marca.isEmpty() || modelo.isEmpty() || año.isEmpty() || año.length() != 4 ||
                combustibe.isEmpty() || puertas == null || color.isEmpty() || tipoDeCambio.isEmpty() ||
                kilometros.isEmpty() || potencia.isEmpty() || precio.isEmpty() ||
                descripcion.isEmpty() || descripcion.length() > 280 || ciudad.isEmpty() || codigoPostal.isEmpty();

        if (camposVacios) {
            // Mostrar un mensaje de error o realizar alguna acción apropiada
            if (marca.isEmpty()) {
                marcaTextInputLayout.setError("Campo obligatorio");
            }else{
                marcaTextInputLayout.setError(null);
            }
            if (modelo.isEmpty()) {
                modeloTextInputLayout.setError("Campo obligatorio");
            }else{
                modeloTextInputLayout.setError(null);
            }
            if (año.isEmpty()) {
                añoTextInputLayout.setError("Campo obligatorio");
            }else{
                añoTextInputLayout.setError(null);
            }
            if (año.length() != 4) {
                añoTextInputLayout.setError("El año debe tener 4 dígitos");
            }else{
                añoTextInputLayout.setError(null);
            }
            if (combustibe.isEmpty()) {
                combustibleTextInputLayout.setError("Campo obligatorio");
            }else{
                combustibleTextInputLayout.setError(null);
            }
            if (color.isEmpty()) {
                colorTextInputLayout.setError("Campo obligatorio");
            }else{
                colorTextInputLayout.setError(null);
            }
            if (puertas == null) {
                Snackbar.make(requireView(), "Elige cuantas puertas tiene tu coche", Snackbar.LENGTH_LONG).show();
            }
            if (tipoDeCambio.isEmpty()) {
                cambioTextInputLayout.setError("Campo obligatorio");
            }else{
                cambioTextInputLayout.setError(null);
            }
            if (kilometros.isEmpty()) {
                kmTextInputLayout.setError("Campo obligatorio");
            }else{
                kmTextInputLayout.setError(null);
            }
            if (potencia.isEmpty()) {
                potenciaTextInputLayout.setError("Campo obligatorio");
            }else{
                potenciaTextInputLayout.setError(null);
            }
            if (precio.isEmpty()) {
                precioTextInputLayout.setError("Campo obligatorio");
            }else{
                precioTextInputLayout.setError(null);
            }
            if (descripcion.isEmpty() || descripcion.length() > 280) {
                descripcionTextInputLayout.setError("Campo obligatorio con un máximo de 280 caracteres");
            }else{
                descripcionTextInputLayout.setError(null);
            }
            if (ciudad.isEmpty()) {
                ciudadTextInputLayout.setError("Campo obligatorio");
            }else{
                ciudadTextInputLayout.setError(null);
            }
            if (codigoPostal.isEmpty()) {
                codigoPostalTextInputLayout.setError("Campo obligatorio");
            }else{
                codigoPostalTextInputLayout.setError(null);
            }

            return; // Salir del método si al menos un campo está en blanco
        }

        añoInt = Integer.parseInt(año);
        for (int i = 0; i < photoContainer.getChildCount(); i++) {
            RelativeLayout photoLayout = (RelativeLayout) photoContainer.getChildAt(i);
            ImageView imageView = (ImageView) photoLayout.getChildAt(0);
            Uri uri = (Uri) imageView.getTag();
            if (uri != null) {
                imageUrls.add(uri.toString());
            }
        }

        if(imageUrls.size() == 0){
            Snackbar.make(requireView(), "Ingresa una foto de tu coche", Snackbar.LENGTH_LONG).show();
            return;
        }

        LatLng cityLocation = getLocationFromCity(ciudad);

        if (cityLocation == null) {
            cityLocation = getLocationFromPostalCode(codigoPostal);
        }
        if (cityLocation != null) {
            currentLatitude = cityLocation.latitude;
            currentLongitude = cityLocation.longitude;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            subirImagenesAlmacenamiento(imageUrls);
        });
    }

    private void subirImagenesAlmacenamiento(List<String> imageUrls) {
        List<Task<Uri>> uploadTasks = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            String imageFileName = UUID.randomUUID().toString();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("image").child(imageFileName);
            Uri imageUri = Uri.parse(imageUrl);

            uploadTasks.add(storageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }));
        }

        // Esperar a que se completen todas las tareas de carga
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener(task -> {
            List<String> downloadUrls = new ArrayList<>();

            for (Task<Uri> uploadTask : uploadTasks) {
                if (uploadTask.isSuccessful()) {
                    Uri downloadUri = uploadTask.getResult();
                    if (downloadUri != null) {
                        downloadUrls.add(downloadUri.toString());
                    }
                }
            }
            // Llama al método guardarEnFirestore() pasando la lista downloadUrls
            guardarEnFirestore(marca, modelo, añoInt, combustibe, puertas, color, kilometros, tipoDeCambio, potencia, precio, descripcion, ciudad, currentLatitude, currentLongitude, date, downloadUrls);
        });
    }

    private void guardarEnFirestore(String marca, String modelo, int año, String combustible, String puertas, String color, String kilometros, String tipoDeCambio, String potencia, String precio, String descripcion, String ciudad, double latitude, double longitude, Date date, List<String> downloadUrls) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Post post = new Post(user.getUid(), downloadUrls, marca, modelo, año, combustible, puertas, color, kilometros, tipoDeCambio, potencia, precio, descripcion, ciudad, latitude, longitude, date);
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

    private LatLng getLocationFromCity(String cityName) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses;
        LatLng location = null;

        try {
            addresses = geocoder.getFromLocationName(cityName, 1);

            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                location = new LatLng(latitude, longitude);
            } else {
                //Toast.makeText(requireContext(), "No se encontraron resultados para la ciudad especificada", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }
    private final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                appViewModel.setMediaSeleccionado(uri, mediaTipo);
            });

    private LatLng getLocationFromPostalCode(String postalCode) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses;
        LatLng location = null;

        try {
            addresses = geocoder.getFromLocationName(postalCode, 1);

            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                location = new LatLng(latitude, longitude);
            } else {
                //Toast.makeText(requireContext(), "No se encontraron resultados para el código postal especificado", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

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
        Glide.with(this).load(uri).into(imageView);

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarFoto(relativeLayout);

                // Obtener el índice del photoLayout
                int index = photoContainer.indexOfChild(relativeLayout);

                if (index >= 0 && index < imageUrls.size()) {
                    // Eliminar la URL de la imagen de la lista usando el índice
                    imageUrls.remove(index);
                }
            }
        });

        relativeLayout.addView(imageView);
        relativeLayout.addView(closeIcon);
        photoContainer.addView(relativeLayout);
        photoViews.add(relativeLayout);

        // Guardar la URL de la imagen en la lista
        imageUrls.add(uri.toString());

        iconoGaleria.setVisibility(View.INVISIBLE);
        photoText.setVisibility(View.INVISIBLE);
        if (photoViews.size() >= maxPhotos) {
            subirFotosBoton.setEnabled(false);
            subirImagenesAlmacenamiento(imageUrls); // Llamar al método para subir las imágenes a Firebase
        }
    }


    private void eliminarFoto(RelativeLayout photoLayout) {
        int index = photoContainer.indexOfChild(photoLayout);

        if (index >= 0 && index < imageUrls.size()) {
            photoContainer.removeView(photoLayout);
            photoViews.remove(index);
            imageUrls.remove(index);

            // Eliminar la URL de la imagen de la lista usando el índice
            if (index < imageUrls.size()) {
                imageUrls.remove(index);
            }
        }

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