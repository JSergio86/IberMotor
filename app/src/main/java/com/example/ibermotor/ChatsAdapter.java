package com.example.ibermotor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private List<Chat> chats;
    private OnChatClickListener onChatClickListener;

    public ChatsAdapter(OnChatClickListener onChatClickListener) {
        this.chats = new ArrayList<>();
        this.onChatClickListener = onChatClickListener;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view, onChatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.bind(chat, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profile_image;
        private TextView displayNameTextView, lastMessageTextView;
        private OnChatClickListener onChatClickListener;

        public ViewHolder(@NonNull View itemView, OnChatClickListener onChatClickListener) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            this.onChatClickListener = onChatClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Chat chat, Context context) {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<String> participants = chat.getUsers();
            String otherUserId = null;

            for (String participant : participants) {
                if (!participant.equals(currentUserId)) {
                    otherUserId = participant;
                    break;
                }
            }

            if (otherUserId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String finalOtherUserId = otherUserId;
                String finalOtherUserId1 = otherUserId;
                db.collection("usuarios")
                        .document(otherUserId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Usuario otherUser = documentSnapshot.toObject(Usuario.class);
                                if (otherUser != null) {
                                    if (otherUser.getNombre() == null || otherUser.getNombre().isEmpty()) {
                                        displayNameTextView.setText("Usuario no encontrado");
                                    } else {
                                        displayNameTextView.setText(otherUser.getNombre());
                                    }

                                    if(otherUser.getFotoPerfil() == null){
                                        Glide.with(context)
                                                .load(R.drawable.anonymo)
                                                .into(profile_image);
                                    }

                                    if (otherUser.getFotoPerfil() != null && !otherUser.getFotoPerfil().isEmpty()) {
                                        Glide.with(context)
                                                .load(otherUser.getFotoPerfil())
                                                .into(profile_image);
                                    }

                                    // Obtener el último mensaje
                                    db.collection("chats").document(chat.getChatId()).collection("messages")
                                            .orderBy("timestamp", Query.Direction.DESCENDING)
                                            .limit(1)
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    Message lastMessage = queryDocumentSnapshots.getDocuments().get(0).toObject(Message.class);
                                                    if (lastMessage != null) {
                                                        lastMessageTextView.setText(lastMessage.getContent());
                                                    } else {lastMessageTextView.setText("No hay mensajes");}
                                                }
                                            })
                                            .addOnFailureListener(e -> Log.d("UID", finalOtherUserId1));

                                }
                            } else {
                                Log.e("ChatsAdapter", "User not found");
                                Log.d("UID", finalOtherUserId1);

                            }
                        })
                        .addOnFailureListener(e -> Log.e("ChatsAdapter", "Error loading user details", e));
            } else {
                displayNameTextView.setText("usuario no encontrado");
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onChatClickListener != null) {
                onChatClickListener.onChatClick(ChatsAdapter.this.chats.get(position));
            }
        }
    }
}