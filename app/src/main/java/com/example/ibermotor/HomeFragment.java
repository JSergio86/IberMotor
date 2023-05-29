package com.example.ibermotor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    NavController navController;
    public AppViewModel appViewModel;
    ImageView menuDrawer;
    RelativeLayout filtroLayout;
    CircleImageView fotoPerfil;
    EditText barraBusqueda;
    String uid;
    FirestoreRecyclerAdapter<Post, PostsAdapter.PostViewHolder> postsAdapter;
    Query postsQuery;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        filtroLayout = view.findViewById(R.id.filtroLayout);
        fotoPerfil = view.findViewById(R.id.perfil);
        menuDrawer = view.findViewById(R.id.menuDrawer);
        barraBusqueda = view.findViewById(R.id.barraBusqueda);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        // Obtener la referencia a la colección "usuarios" y al documento con la UID del post
        CollectionReference usuariosRef = FirebaseFirestore.getInstance().collection("usuarios");
        DocumentReference usuarioDocRef = usuariosRef.document(uid);

        // Consultar los datos del usuario
        usuarioDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Obtener la foto de perfil y el nombre del usuario del documento
                    String fotoPerfilFirebase = documentSnapshot.getString("fotoPerfil");

                    if (fotoPerfilFirebase != null) {
                        Glide.with(requireActivity())
                                .load(fotoPerfilFirebase)
                                .into(fotoPerfil);
                    } else {
                        Glide.with(requireActivity())
                                .load(R.drawable.anonymo)
                                .into(fotoPerfil);
                    }

                }
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.perfilFragment);
            }
        });

        filtroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.filtrosFragment);
            }
        });


        menuDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.menudrawer);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.ayuda:
                                navController.navigate(R.id.ayudaFragment);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Los permisos no están concedidos, solicita los permisos
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1); // El número 1 es un código de solicitud que puedes usar para identificar esta solicitud de permisos en el método onRequestPermissionsResult
        }


        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        postsQuery = FirebaseFirestore.getInstance().collection("posts").limit(50).orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .setLifecycleOwner(this)
                .build();

        postsAdapter = new PostsAdapter(options);
        postsRecyclerView.setAdapter(postsAdapter);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // El mismo código de solicitud que se usó al solicitar los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos, realiza las acciones necesarias
            } else {
                // Los permisos fueron denegados, maneja esta situación de acuerdo a tus necesidades
                Toast.makeText(requireContext(), "Los permisos de ubicación fueron denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.PostViewHolder> {

        public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
            super(options);
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_post, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Post post) {
            holder.precioText.setText(post.precio+"€");
            holder.kilometrosText.setText(post.kilometros+" km -");
            holder.añosText.setText(post.año+" -");
            holder.nombreText.setText(post.marca+" "+ post.modelo);
            holder.combustibleText.setText(post.combustible);

            // Obtener la lista de fotos del post
            List<String> fotos = post.fotoCoche;

            // Verificar si hay fotos disponibles
            if (!fotos.isEmpty()) {
                // Obtener la URL de la primera foto
                String urlPrimeraFoto = fotos.get(0);

                // Cargar la imagen en el ImageView usando Glide
                Glide.with(holder.itemView)
                        .load(urlPrimeraFoto)
                        .into(holder.fotoCoche);
            }

            // Obtener el ancho máximo en dp para el TextView ciudadText
            int maxCityWidthDp = 80;

            // Convertir el ancho máximo de dp a píxeles
            float maxCityWidthPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    maxCityWidthDp,
                    holder.itemView.getResources().getDisplayMetrics()
            );

            // Obtener la ciudad del post
            String postCity = post.ciudad;

            // Obtener el ancho del texto de la ciudad actual
            float currentCityWidthPx = holder.ciudadText.getPaint().measureText(postCity);

            // Verificar si el ancho actual excede el ancho máximo
            if (currentCityWidthPx > maxCityWidthPx) {
                // Obtener la subcadena del texto que se ajuste al ancho máximo
                String truncatedCityText = TextUtils.ellipsize(postCity, holder.ciudadText.getPaint(), maxCityWidthPx, TextUtils.TruncateAt.END).toString();

                // Agregar puntos suspensivos al final de la subcadena truncada
                String finalCityText = truncatedCityText + ".. -";

                // Establecer el texto final en el TextView ciudadText
                holder.ciudadText.setText(finalCityText);
            } else {
                // Establecer la ciudad del post directamente en el TextView ciudadText
                holder.ciudadText.setText(postCity+ " -");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToDescripcionCocheFragment(post);
                }
            });


        }
        public void navigateToDescripcionCocheFragment(Post post) {
            appViewModel.postSeleccionado.setValue(post);
            Bundle args = new Bundle();
            args.putString("postId", post.postId);
            args.putString("uid", post.uid);
            navController.navigate(R.id.descripcionCocheFragment, args);
        }

        class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView fotoCoche;
            TextView precioText, nombreText, ciudadText, kilometrosText, añosText, combustibleText;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);
                fotoCoche = itemView.findViewById(R.id.fotoCoche);
                precioText = itemView.findViewById(R.id.precioText);
                nombreText = itemView.findViewById(R.id.nombreText);
                ciudadText = itemView.findViewById(R.id.ciudadText);
                kilometrosText = itemView.findViewById(R.id.kilometrosText);
                añosText = itemView.findViewById(R.id.añosText);
                combustibleText = itemView.findViewById(R.id.combustibleText);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}