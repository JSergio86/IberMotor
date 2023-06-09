package com.example.ibermotor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterFragment extends Fragment {
    NavController navController;
    private EditText emailEditText, passwordEditText, nombreEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    TextView loginTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        nombreEditText = view.findViewById(R.id.nombreEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginTextView = view.findViewById(R.id.loginTextView);
        registerButton = view.findViewById(R.id.emailSignInButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.signInFragment);
            }
        });



        mAuth = FirebaseAuth.getInstance();

    }

    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }
 
        registerButton.setEnabled(false);

        String correo = emailEditText.getText().toString().trim();
        String nombre = nombreEditText.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereEqualTo("correo", correo).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                // Usuario existente con el mismo correo
                Snackbar.make(requireView(), "Este correo ya está registrado ", Snackbar.LENGTH_LONG).show();
            } else {
                usuariosRef.whereEqualTo("nombre", nombre).get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                        // Usuario existente con el mismo nombre
                        Snackbar.make(requireView(), "Este nombre ya está registrado", Snackbar.LENGTH_LONG).show();
                    } else {
                        // El correo y el nombre no están registrados, proceder con el registro
                        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String uid = mAuth.getCurrentUser().getUid();
                                            Usuario usuario = new Usuario(uid, null , nombre, correo);
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

                                        } else {
                                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                                        }
                                        registerButton.setEnabled(true);
                                    }
                                });

                    }
                });
            }
            registerButton.setEnabled(true);

        });



    }

    private boolean validarFormulario() {
        boolean valid = true;


        // Validar contraseña
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(requireView(), "Contraseña requerida.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else if (password.length() < 8) {
            Snackbar.make(requireView(), "La contraseña debe tener al menos 8 caracteres.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        // Validar correo electrónico
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(requireView(), "Correo electrónico requerido.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(requireView(), "Formato de correo electrónico inválido.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        // Validar nombre
        String nombre = nombreEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Snackbar.make(requireView(), "Nombre requerido.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else {
            nombreEditText.setError(null);
        }
        return valid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}