package com.example.ibermotor;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment {
    NavController navController;   // <-----------------
    public AppViewModel appViewModel;
    FirebaseUser user;
    ImageView iconoFiltro, fotoPerfil, menuDrawer;
    String uid;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        iconoFiltro = view.findViewById(R.id.iconoFiltro);
        fotoPerfil = view.findViewById(R.id.perfil);
        menuDrawer = view.findViewById(R.id.menuDrawer);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Glide.with(requireView())
                    .load(user.getPhotoUrl())
                    .transform(new CircleCrop())
                    .into(fotoPerfil);
        }

        if(user.getPhotoUrl() == null){
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

        view.findViewById(R.id.iconoVista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.crearPost);
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

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Los permisos no están concedidos, solicita los permisos
            ActivityCompat.requestPermissions(requireActivity(), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1); // El número 1 es un código de solicitud que puedes usar para identificar esta solicitud de permisos en el método onRequestPermissionsResult
        }


        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        Query query = FirebaseFirestore.getInstance().collection("posts").limit(50);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();

        postsRecyclerView.setAdapter(new PostsAdapter(options));

        user = FirebaseAuth.getInstance().getCurrentUser();

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
        public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {super(options);}

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_post, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Post post) {
            if (post.mediaUrl != null) {
                holder.fotoCoche.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .override(Target.SIZE_ORIGINAL);

                Glide.with(requireView())
                        .load(post.mediaUrl)
                        .apply(options)
                        .into(holder.fotoCoche);

                holder.fotoCoche.setOnClickListener(view -> {
                    appViewModel.postSeleccionado.setValue(post);
                    navController.navigate(R.id.fotoCoche);
                });

            } else {
                holder.fotoCoche.setVisibility(View.GONE);
            }
            holder.precioTotal.setText(post.precioTotal);
            holder.precioText.setText(post.precioText);
        }

        class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView fotoCoche;
            TextView precioTotal, precioText, nombreText, ciudadText, kilometrosText, añosText, combustibleText, garantia;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);
                fotoCoche = itemView.findViewById(R.id.fotoCoche);
                precioTotal = itemView.findViewById(R.id.precioTotal);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}