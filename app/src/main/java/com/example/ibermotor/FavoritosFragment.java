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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


public class FavoritosFragment extends Fragment {
    NavController navController;
    public AppViewModel appViewModel;
    String uid;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerViewPerfil);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid= user.getUid();

        Query query = FirebaseFirestore.getInstance().collection("posts").whereArrayContains("favoritos",uid).limit(50).orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();


        postsRecyclerView.setAdapter(new PostsAdapter(options));


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
            holder.precioText.setText(post.precio+"€");
            holder.kilometrosText.setText(post.kilometros+"km -");
            holder.añosText.setText(post.año+" - ");
            holder.ciudadText.setText(post.ciudad+" - ");
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

            final String postKey = getSnapshots().getSnapshot(position).getId();



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
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }
}