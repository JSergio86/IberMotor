package com.example.socialapp;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    NavController navController;
    private EditText emailEditText, passwordEditText, nombreEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    ImageView facebook;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        nombreEditText = view.findViewById(R.id.nombreEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        facebook = view.findViewById(R.id.facebook);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }
 
        registerButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("uid", mAuth.getCurrentUser().getUid());
                            bundle.putString("nombre", nombreEditText.getText().toString());
                            bundle.putString("correo", emailEditText.getText().toString());
                            bundle.putString("fotoPerfil", String.valueOf(R.drawable.anonymo));

                            AntesDeComenzar antesDeComenzar = new AntesDeComenzar();
                            antesDeComenzar.setArguments(bundle);
                            navController.navigate(R.id.antesDeComenzar, bundle);

                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();

                        }
                        registerButton.setEnabled(true);
                    }
                });

    }

    private boolean validarFormulario() {
        boolean valid = true;

        // Validar nombre
        String nombre = nombreEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Snackbar.make(requireView(), "Nombre requerido.", Snackbar.LENGTH_LONG).show();
            valid = false;
        } else {
            nombreEditText.setError(null);
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
        return valid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}