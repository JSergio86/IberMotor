package com.example.ibermotor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class AnunciosFragment extends Fragment {
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

        Query query = FirebaseFirestore.getInstance().collection("posts").whereEqualTo("uid",uid).limit(50).orderBy("date", Query.Direction.DESCENDING);

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
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_post_perfil, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Post post) {
            holder.precioText.setText(post.precio+"€");
            holder.kilometrosText.setText(post.kilometros+"km -");
            holder.añosText.setText(post.año+" - ");
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
                holder.ciudadText.setText(postCity+ " - ");
            }

            final String postKey = getSnapshots().getSnapshot(position).getId();

            holder.borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Borrar el post en Firebase
                    FirebaseFirestore.getInstance().collection("posts").document(postKey)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Borrado exitoso, realizar acciones adicionales si es necesario
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error al borrar el post, manejar el error si es necesario
                                }
                            });
                }
            });


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
            ImageView fotoCoche, borrar;
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
                borrar = itemView.findViewById(R.id.borrar);

            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anuncios, container, false);
    }
}