package com.example.ibermotor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends Fragment {
    NavController navController;
    private ImageView googleSignInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private EditText emailEditText, passwordEditText;
    private Button emailSignInButton;
    private ScrollView signInForm;
    private FirebaseAuth mAuth;
    private TextView registrateTextView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        emailSignInButton = view.findViewById(R.id.emailSignInButton);
        signInForm = view.findViewById(R.id.constraint);
        registrateTextView = view.findViewById(R.id.registrateTextView);


        mAuth = FirebaseAuth.getInstance();

        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConEmail();
            }
        });

        registrateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.registerFragment);
            }
        });


        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            try {
                                firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
                            } catch (ApiException e) {
                                Log.e("ABCD", "signInResult:failed code=" +
                                        e.getStatusCode());
                            }
                        }
                    }
                });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConGoogle();
            }
        });
    }

    private void accederConGoogle() {
        GoogleSignInClient googleSignInClient =
                GoogleSignIn.getClient(requireActivity(), new
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build());
        activityResultLauncher.launch(googleSignInClient.getSignInIntent());
    }

    // Después de autenticar con Google, obtiene los datos del usuario
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if(acct == null) return;
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null))
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtiene la información del usuario
                                String uid = user.getUid();
                                String nombre = user.getDisplayName();
                                String correo = user.getEmail();
                                Uri fotoPerfilUri = user.getPhotoUrl();
                                String fotoPerfil = fotoPerfilUri != null ? fotoPerfilUri.toString() : null;

                                // Verifica si el usuario existe en Firebase Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference usuariosRef = db.collection("usuarios");
                                usuariosRef.whereEqualTo("correo", correo).get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                                                // Usuario existente, navegar a Fragment1
                                                navController.navigate(R.id.homeFragment);
                                            } else {
                                                Usuario usuario = new Usuario(uid, fotoPerfil, nombre, correo);
                                                usuariosRef.document(uid).set(usuario)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                navController.navigate(R.id.homeFragment);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
    }



    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController.navigate(R.id.homeFragment);
        }
    }

    private void accederConEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            //Hacer rectangulo rojo
            Snackbar.make(requireView(), "Ingresa tu correo electrónico", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(requireView(), "Introduce una dirección de correo electrónico válida", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(requireView(), "Ingresa tu contraseña", Snackbar.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                            signInForm.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (task.isSuccessful()) {
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            signInForm.setVisibility(View.VISIBLE);
                            Snackbar.make(requireView(), "Credenciales incorrectas. Inténtalo de nuevo.", Snackbar.LENGTH_LONG).show();
                        }

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }
}
