package com.example.ibermotor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
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


public class QueryFiltros extends Fragment {
    NavController navController;
    public AppViewModel appViewModel;
    ImageView iconoFiltro, menuDrawer;
    CircleImageView fotoPerfil;
    String uid;
    String modelo, potencia, km, precioDesde, precioHasta, marca, combustible, color, cambio, ciudad, año, puertas;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        iconoFiltro = view.findViewById(R.id.iconoFiltro);
        fotoPerfil = view.findViewById(R.id.perfil);
        menuDrawer = view.findViewById(R.id.menuDrawer);

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


        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        Query baseQuery = FirebaseFirestore.getInstance().collection("posts");

        if (!marca.equalsIgnoreCase("Todas")) {
            baseQuery = baseQuery.whereEqualTo("marca", marca);
        }

        if (!modelo.equalsIgnoreCase("Todos")) {
            baseQuery = baseQuery.whereEqualTo("modelo", modelo);
        }

        if (!combustible.equalsIgnoreCase("Todos")) {
            baseQuery = baseQuery.whereEqualTo("combustible", combustible);
        }

        if (!color.equalsIgnoreCase("Todos")) {
            baseQuery = baseQuery.whereEqualTo("color", color);
        }

        if (!cambio.equalsIgnoreCase("Todos")) {
            baseQuery = baseQuery.whereEqualTo("cambioMarchas", cambio);
        }

        if (!ciudad.equalsIgnoreCase("Todas")) {
            baseQuery = baseQuery.whereEqualTo("ciudad", ciudad);
        }

        if (!año.equalsIgnoreCase("0")) {
            baseQuery = baseQuery.whereEqualTo("año", Integer.parseInt(año));
        }

        if (!potencia.equalsIgnoreCase("Todos")) {
             baseQuery = baseQuery.whereEqualTo("potencia", Integer.parseInt(potencia));
        }

        if (!km.equalsIgnoreCase("Todos")) {
            baseQuery = baseQuery.whereEqualTo("kilometros", Integer.parseInt(km));
        }

        if (!puertas.equalsIgnoreCase("0")) {
             baseQuery = baseQuery.whereEqualTo("puertas", Integer.parseInt(puertas));
        }

        if (!precioDesde.equalsIgnoreCase("Todos") && !precioHasta.equalsIgnoreCase("Todos")) {
             baseQuery = baseQuery.whereGreaterThanOrEqualTo("precio", Integer.parseInt(precioDesde))
                    .whereLessThanOrEqualTo("precio", Integer.parseInt(precioHasta));
        }

        baseQuery = baseQuery.limit(50);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(baseQuery, Post.class)
                .setLifecycleOwner(this)
                .build();

        postsRecyclerView.setAdapter(new PostsAdapter(options));

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
            holder.precioText.setText(post.precio + "€");
            holder.kilometrosText.setText(post.kilometros + "km -");
            holder.añosText.setText(post.año + " - ");
            holder.ciudadText.setText(post.ciudad + " - ");
            holder.nombreText.setText(post.marca + " " + post.modelo);
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
            TextView precioText, nombreText, ciudadText, kilometrosText, añosText, combustibleText, garantia;

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
        Bundle args = getArguments();
        if (args != null) {
            modelo = args.getString("modelo");
            potencia = args.getString("potencia");
            km = args.getString("km");
            precioDesde = args.getString("precioDesde");
            precioHasta = args.getString("precioHasta");
            marca = args.getString("marca");
            combustible = args.getString("combustible");
            color = args.getString("color");
            cambio = args.getString("cambio");
            ciudad = args.getString("ciudad");
            año = args.getString("año");
            puertas = args.getString("puertas");
        }
        return inflater.inflate(R.layout.fragment_query_filtros, container, false);

    }
}