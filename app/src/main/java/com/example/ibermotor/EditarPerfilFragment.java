package com.example.ibermotor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilFragment extends Fragment {
    NavController navController;
    ImageView volver, foto;
    Button actualizar;
    TextInputEditText nombreText;
    String uid;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imagenSeleccionada;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volverAtras);
        actualizar = view.findViewById(R.id.actualizar);
        foto = view.findViewById(R.id.perfil);
        nombreText = view.findViewById(R.id.nombreEditText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid= user.getUid();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        if (user != null) {
            Glide.with(requireView())
                    .load(user.getPhotoUrl())
                    .transform(new CircleCrop())
                    .into(foto);
        }

        if (user.getPhotoUrl() == null) {
            Glide.with(requireView())
                    .load(R.drawable.anonymo)
                    .transform(new CircleCrop())
                    .into(foto);
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.perfilFragment);

            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = nombreText.getText().toString();

                if(nuevoNombre.isEmpty()){
                    return;
                }

                String userId = uid;

                // Verifica que el ID de usuario no sea nulo o vacío
                if (userId != null && !userId.isEmpty()) {
                    // Si se ha seleccionado una imagen
                    if (imagenSeleccionada != null) {
                        // Crea una referencia única para el archivo en el Firebase Storage
                        StorageReference imagenRef = storageRef.child("imagenes_perfil/" + uid + ".jpg");

                        // Sube la imagen al Firebase Storage
                        imagenRef.putFile(imagenSeleccionada)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Obtiene la URL de descarga de la imagen
                                        imagenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUrl) {
                                                String imageUrl = downloadUrl.toString();
                                                actualizarFotoPerfil(imageUrl, nuevoNombre);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        actualizarFotoPerfil(null, nuevoNombre);
                    }
                } else {
                    //Toast.makeText(getActivity(), "No se pudo obtener el ID de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void actualizarFotoPerfil(String imageUrl, String nuevoNombre) {
        // Aquí debes obtener el ID del usuario actualmente autenticado
        String userId = uid;

        // Verifica que el ID de usuario no sea nulo o vacío
        if (userId != null && !userId.isEmpty()) {
            DocumentReference userRef = db.collection("usuarios").document(userId);

            Map<String, Object> updates = new HashMap<>();
            if (imageUrl != null) {
                updates.put("fotoPerfil", imageUrl);
            }
            updates.put("nombre", nuevoNombre);

            userRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Foto de perfil y nombre actualizados correctamente", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.perfilFragment);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error al actualizar la foto de perfil y el nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private static final int REQUEST_GALLERY = 1;

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            imagenSeleccionada = data.getData();
            foto.setImageURI(imagenSeleccionada);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false);
    }
}