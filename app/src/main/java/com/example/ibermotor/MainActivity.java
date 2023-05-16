package com.example.ibermotor;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.ibermotor.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NavController navController;
    BottomNavigationView bottomNavigationView;
    //Lista de fragments sin el bottom nav
    List<Integer> fragmentsWithoutBottomNav = Arrays.asList(R.id.signInFragment, R.id.registerFragment, R.id.ayudaFragment, R.id.notificacionesFragment, R.id.privacidadFragment, R.id.editarPerfilFragment, R.id.antesDeComenzar);
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Instancia de la autentificacion del firebase
        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(this,R.id.mainLayout);

        navController = Navigation.findNavController(this, R.id.mainLayout);

        //Establecer el graph en el NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainLayout);
        navHostFragment.getNavController().setGraph(R.navigation.mobile_navigation);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Poner bien los fragments cuando esten acabados
                switch (item.getItemId()) {
                    case R.id.buscar:
                        navController.navigate(R.id.homeFragment);
                        break;
                    case R.id.publicar:
                        navController.navigate(R.id.marcaSearchDialogFragment);
                        break;
                    case R.id.chat:
                        navController.navigate(R.id.conversacionesFragment);
                        break;
                    case R.id.perfil:
                        navController.navigate(R.id.perfilFragment);
                        break;
                }
                return true;
            }
        });

        //Aqui solo se muestra el bottom navigation en los fragments que no hemos quitado en la lista de fragmentsWithoutBottomNav
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // verifica si el ID del fragmento actual está en la lista de fragmentos sin BottomNavigationView
                if (fragmentsWithoutBottomNav.contains(destination.getId())) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);

                    // verifica si el fragmento actual pertenece a un elemento del BottomNavigationView
                    MenuItem item = bottomNavigationView.getMenu().findItem(destination.getId());
                    if (item != null) {
                        bottomNavigationView.setSelectedItemId(item.getItemId());
                    }
                }
            }
        });
/*
        // Verifica que el usuario anteriormente se haya conectado e iniciar sesion directamente
        if (mAuth.getCurrentUser() == null) {
            navController.navigate(R.id.signInFragment);
        }
        else {
            navController.navigate(R.id.homeFragment);
        }

 */

    }

    public NavController getNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainLayout);
        return navHostFragment.getNavController();
    }

}