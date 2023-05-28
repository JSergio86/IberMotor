package com.example.ibermotor;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ibermotor.databinding.FragmentAntesDeComenzarBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.Manifest;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AntesDeComenzar extends Fragment implements OnMapReadyCallback {
    NavController navController;
    private GoogleMap mMap;
    private FragmentAntesDeComenzarBinding binding;
    private double currentLatitude;
    private double currentLongitude;
    private Marker currentMarker;
    private boolean cameraMoved = false;
    private FirebaseFirestore db;
    private Bitmap markerIcon;

    public AntesDeComenzar() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAntesDeComenzarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        // Check location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        }
    }

    private void initializeMap() {
        db = FirebaseFirestore.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }

        if (currentLatitude != 0 && currentLongitude != 0) {
            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        } else {
            LatLng spainLocation = new LatLng(40.4637, -3.7492); // Coordenadas de España
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spainLocation, 5));
        }

        // establece la variable cameraMoved a true cuando la cámara se mueve manualmente
        mMap.setOnCameraMoveListener(() -> {
            cameraMoved = true;
        });

        // Consultar la colección "posts" en Firestore
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Iterar sobre los documentos obtenidos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener las coordenadas de latitud y longitud
                            double latitude = document.getDouble("latitud");
                            double longitude = document.getDouble("longitud");

                            // Obtener la lista de fotos del documento
                            List<String> fotos = (List<String>) document.get("fotoCoche");

                            if (fotos != null && !fotos.isEmpty()) {
                                // Obtener la URL de la primera foto
                                String primeraFotoUrl = fotos.get(0);

                                // Crear un LatLng con las coordenadas
                                LatLng postLocation = new LatLng(latitude, longitude);

                                // Cargar la imagen del marcador desde la URL
                                getMarkerIconFromUrl(primeraFotoUrl, postLocation);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void getMarkerIconFromUrl(String url, LatLng postLocation) {
        Glide.with(requireContext())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().override(100, 100).transform(new CircleCrop()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        markerIcon = resource;

                        // Agregar el marcador al mapa
                        addMarkerToMap(postLocation);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // No se necesita ninguna acción adicional cuando se borra la carga
                    }
                });
    }

    private void addMarkerToMap(LatLng postLocation) {
        if (markerIcon != null) {
            Bitmap circularIcon = Bitmap.createBitmap(markerIcon.getWidth(), markerIcon.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(circularIcon);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(markerIcon, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawCircle(markerIcon.getWidth() / 2, markerIcon.getHeight() / 2, markerIcon.getWidth() / 2, paint);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(postLocation)
                    .icon(BitmapDescriptorFactory.fromBitmap(circularIcon));

            mMap.addMarker(markerOptions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            }
        }
    }
}
