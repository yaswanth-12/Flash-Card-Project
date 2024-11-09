// HomeScreen.java
package com.example.flashcardproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements FlashcardAdapter.OnFlashcardClickListener {

    private List<Flashcard> flashcardList;
    private FlashcardAdapter adapter;
    private FirebaseFirestore db;
    public static final int ADD_FLASHCARD_REQUEST = 1;
    public static final int EDIT_FLASHCARD_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        flashcardList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlashcardAdapter(flashcardList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, FlashcardEditActivity.class);
            startActivityForResult(intent, ADD_FLASHCARD_REQUEST);
        });

        fetchFlashcardsFromFirestore();
    }

    private void fetchFlashcardsFromFirestore() {
        db.collection("flashcards")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        flashcardList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Flashcard flashcard = document.toObject(Flashcard.class);
                            flashcardList.add(flashcard);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle error
                    }
                });
    }

    @Override
    public void onEditClick(int position) {
        Flashcard flashcard = flashcardList.get(position);
        Intent intent = new Intent(HomeScreen.this, FlashcardEditActivity.class);
        intent.putExtra("id", flashcard.getId());
        intent.putExtra("question", flashcard.getQuestion());
        intent.putExtra("answer", flashcard.getAnswer());
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_FLASHCARD_REQUEST);
    }

    @Override
    public void onFlashcardClick(int position) {
        Flashcard flashcard = flashcardList.get(position);
        Intent intent = new Intent(HomeScreen.this, Flashcard_View_Screen.class);
        intent.putExtra("id", flashcard.getId());
        intent.putExtra("question", flashcard.getQuestion());
        intent.putExtra("answer", flashcard.getAnswer());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String question = data.getStringExtra("question");
            String answer = data.getStringExtra("answer");

            if (requestCode == ADD_FLASHCARD_REQUEST) {
                String id = data.getStringExtra("id");
                Flashcard newFlashcard = new Flashcard(id, question, answer, false);
                flashcardList.add(newFlashcard);
                adapter.notifyItemInserted(flashcardList.size() - 1);

            } else if (requestCode == EDIT_FLASHCARD_REQUEST) {
                int position = data.getIntExtra("position", -1);

                if (position != -1) {
                    flashcardList.get(position).setQuestion(question);
                    flashcardList.get(position).setAnswer(answer);
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        flashcardList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}