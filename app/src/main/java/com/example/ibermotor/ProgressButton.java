package com.example.ibermotor;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    Animation fade_in;

    public ProgressButton(View view) {
        cardView = view.findViewById(R.id.cardViewProgressBar);
        layout = view.findViewById(R.id.constraintProgressBar);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textViewProgressBar);

    }

    void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Subiendo anuncio...");
    }

    void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.teal_200));
        progressBar.setVisibility(View.GONE);
        textView.setText("Subido correctamente");
    }
}
