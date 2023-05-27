package com.example.ibermotor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


public class QueryFiltros extends Fragment {
    NavController navController;
    public AppViewModel appViewModel;
    FirebaseUser user;
    ImageView iconoFiltro, fotoPerfil, menuDrawer;
    String uid;
    String modelo, potenciaDesde, potenciaHasta, kmDesde, kmHasta, precioDesde, precioHasta, marca, combustible, color, cambio, ciudad, añoDesde, añoHasta, puertaDesde, puertaHasta;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        iconoFiltro = view.findViewById(R.id.iconoFiltro);
        fotoPerfil = view.findViewById(R.id.perfil);
        menuDrawer = view.findViewById(R.id.menuDrawer);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Glide.with(requireView())
                    .load(user.getPhotoUrl())
                    .transform(new CircleCrop())
                    .into(fotoPerfil);
        }

        if (user.getPhotoUrl() == null) {
            Glide.with(requireView())
                    .load(R.drawable.anonymo)
                    .transform(new CircleCrop())
                    .into(fotoPerfil);
        }
        uid = user.getUid();


        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.perfilFragment);
            }
        });

        iconoFiltro.setOnClickListener(new View.OnClickListener() {
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
                            case R.id.notificaciones:
                                navController.navigate(R.id.notificacionesFragment);
                                return true;
                            case R.id.privacidad:
                                navController.navigate(R.id.privacidadFragment);
                                return true;
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

        Query filteredQuery = baseQuery;

        if (!añoDesde.equalsIgnoreCase("1970") && !añoHasta.equalsIgnoreCase("2023")) {
            Query añoQuery = baseQuery.whereGreaterThanOrEqualTo("año", Integer.parseInt(añoDesde))
                    .whereLessThanOrEqualTo("año", Integer.parseInt(añoHasta));
            filteredQuery = añoQuery.orderBy("año");
        }

        if (!potenciaDesde.equalsIgnoreCase("Todos") && !potenciaHasta.equalsIgnoreCase("Todos")) {
            Query potenciaQuery = baseQuery.whereGreaterThanOrEqualTo("potencia", Integer.parseInt(potenciaDesde))
                    .whereLessThanOrEqualTo("potencia", Integer.parseInt(potenciaHasta));
            filteredQuery = potenciaQuery.orderBy("potencia");
        }

        if (!kmDesde.equalsIgnoreCase("Todos") && !kmHasta.equalsIgnoreCase("Todos")) {
            Query kmQuery = baseQuery.whereGreaterThanOrEqualTo("kilometros", Integer.parseInt(kmDesde))
                    .whereLessThanOrEqualTo("kilometros", Integer.parseInt(kmHasta));
            filteredQuery = kmQuery.orderBy("kilometros");
        }

        if (!puertaHasta.equalsIgnoreCase("1") && !puertaDesde.equalsIgnoreCase("5")) {
            Query puertasQuery = baseQuery.whereGreaterThanOrEqualTo("puertas", Integer.parseInt(puertaDesde))
                    .whereLessThanOrEqualTo("puertas", Integer.parseInt(puertaHasta));
            filteredQuery = puertasQuery.orderBy("puertas");
        }

        if (!precioDesde.equalsIgnoreCase("Todos") && !precioHasta.equalsIgnoreCase("Todos")) {
            Query precioQuery = baseQuery.whereGreaterThanOrEqualTo("precio", Integer.parseInt(precioDesde))
                    .whereLessThanOrEqualTo("precio", Integer.parseInt(precioHasta));
            filteredQuery = precioQuery.orderBy("precio");
        }

        filteredQuery = filteredQuery.limit(50);


        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(filteredQuery, Post.class)
                .setLifecycleOwner(this)
                .build();




        postsRecyclerView.setAdapter(new PostsAdapter(options));

        user = FirebaseAuth.getInstance().getCurrentUser();

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
                garantia = itemView.findViewById(R.id.garantia);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            modelo = args.getString("modelo");
            potenciaDesde = args.getString("potenciaDesde");
            potenciaHasta = args.getString("potenciaHasta");
            kmDesde = args.getString("kmDesde");
            kmHasta = args.getString("kmHasta");
            precioDesde = args.getString("precioDesde");
            precioHasta = args.getString("precioHasta");
            marca = args.getString("marca");
            combustible = args.getString("combustible");
            color = args.getString("color");
            cambio = args.getString("cambio");
            ciudad = args.getString("ciudad");
            añoDesde = args.getString("añoDesde");
            añoHasta = args.getString("añoHasta");
            puertaDesde = args.getString("puertaDesde");
            puertaHasta = args.getString("puertaHasta");
        }
        return inflater.inflate(R.layout.fragment_query_filtros, container, false);

    }
}