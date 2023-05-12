package com.example.socialapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.socialapp.databinding.FragmentAntesDeComenzarBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.Manifest;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class AntesDeComenzar extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FragmentAntesDeComenzarBinding binding;
    private double currentLatitude;
    private double currentLongitude;
    private Marker currentMarker;
    private String uid, nombre, foto, correo;

    // variable para determinar si la cámara se ha movido manualmente
    private boolean cameraMoved = false;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentAntesDeComenzarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener el objeto Usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Establecer el OnClickListener del botón "Continuar"
        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirUsuarioFirebase();
            }
        });

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void subirUsuarioFirebase() {
        // Actualizar la latitud y longitud del usuario actual en Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        Usuario usuario = new Usuario(uid, foto, nombre, correo, currentLatitude, currentLongitude, true);

        usuariosRef.document().set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Maneja el éxito
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja el error
                    }
                });

        // Navegar al HomeFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mobile_navigation);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.homeFragment);
    }

    public void informacionUsuario(String uid, String foto, String nombre, String correo){
        this.uid = uid;
        this.foto = foto;
        this.nombre = nombre;
        this.correo = correo;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        }
    }

    private void initializeMap() {
        db = FirebaseFirestore.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // mueve la camara a la ubicacion actual
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



        System.out.println("Latitud: " + currentLatitude + " Longitud: " + currentLongitude);

        // establece la variable cameraMoved a true cuando la cámara se mueve manualmente
        mMap.setOnCameraMoveListener(() -> {
            cameraMoved = true;
        });

       /* // consulta los datos de ubicaciones en Firebase
        db.collection("ubicaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            double latitude = document.getDouble("latitude");
                            double longitude = document.getDouble("longitude");
                            LatLng location1 = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions().position(location1).title("Ubicación");
                            mMap.addMarker(markerOptions);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        */
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

            if (currentMarker != null) {
                currentMarker.remove(); // Elimina el marcador anterior
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(currentLocation)
                    .title("Ubicación Actual");
            currentMarker = mMap.addMarker(markerOptions); // Asigna la referencia al nuevo marcador

            if (!cameraMoved) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }

            /*FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("ubicaciones");

            Ubicacion ubicacion = new Ubicacion(currentLatitude, currentLongitude);

            FirebaseFirestore.getInstance().collection("ubicaciones").add(ubicacion);
             */
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

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