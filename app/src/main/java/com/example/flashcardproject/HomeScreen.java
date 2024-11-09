package com.example.flashcardproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements FlashcardAdapter.OnFlashcardClickListener {

    private List<Flashcard> flashcardList;
    private FlashcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        flashcardList = new ArrayList<>();
        // Add some sample flashcards
        flashcardList.add(new Flashcard("Question 1", "Answer 1"));
        flashcardList.add(new Flashcard("Question 2", "Answer 2"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlashcardAdapter(flashcardList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            // Handle adding a new flashcard
            // For example, start a new activity to add a flashcard
        });
    }

    @Override
    public void onEditClick(int position) {
        // Handle editing the flashcard at position
    }

    @Override
    public void onDeleteClick(int position) {
        flashcardList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}