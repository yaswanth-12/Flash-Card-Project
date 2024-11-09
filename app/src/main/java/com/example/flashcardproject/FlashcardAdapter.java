package com.example.flashcardproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcardList;
    private OnFlashcardClickListener listener;

    public interface OnFlashcardClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public FlashcardAdapter(List<Flashcard> flashcardList, OnFlashcardClickListener listener) {
        this.flashcardList = flashcardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_flash_card, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);
        holder.question.setText(flashcard.getQuestion());
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        Button editButton, deleteButton;

        FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(v -> listener.onEditClick(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }
    }
}