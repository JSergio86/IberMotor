package com.example.ibermotor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<Usuario> users;
    private OnUserClickListener onUserClickListener;

    public UsersAdapter(OnUserClickListener onUserClickListener) {
        this.users = new ArrayList<>();
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<Usuario> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view, onUserClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnUserClickListener {
        void onUserClick(Usuario user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView correoNameTextView, usernameTextView;
        private ImageView profile_image;
        private OnUserClickListener onUserClickListener;

        public ViewHolder(@NonNull View itemView, OnUserClickListener onUserClickListener) {
            super(itemView);
            correoNameTextView = itemView.findViewById(R.id.displayNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            profile_image = itemView.findViewById(R.id.profile_image);
            this.onUserClickListener = onUserClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Usuario user) {
            usernameTextView.setText(user.nombre);
            correoNameTextView.setText(user.correo);

            if(user.getFotoPerfil() == null){
                Glide.with(itemView.getContext())
                        .load(R.drawable.anonymo)
                        .circleCrop()
                        .into(profile_image);
            }
            else{
                // Cargar imagen de perfil con Glide
                Glide.with(itemView.getContext())
                        .load(user.getFotoPerfil())
                        .circleCrop()
                        .into(profile_image);
            }



        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onUserClickListener != null) {
                onUserClickListener.onUserClick(UsersAdapter.this.users.get(position));
            }
        }

    }
}
