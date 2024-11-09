package com.example.flashcardproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flashcard_View_Screen extends AppCompatActivity {

    private List<Flashcard> flashcardList;
    private int currentIndex = 0;
    private boolean isShowingQuestion = true;
    private TextView flashcardContent;
    private Button KnownButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view_screen);

        flashcardContent = findViewById(R.id.flashcardContent);
        KnownButton = findViewById(R.id.button);
        Button shuffleButton = findViewById(R.id.shuffleButton);
        db = FirebaseFirestore.getInstance();

        // Get id, question, and answer from intent
        String id = getIntent().getStringExtra("id");
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");

        // Create a flashcard list with only the selected flashcard
        flashcardList = new ArrayList<>();
        flashcardList.add(new Flashcard(id, question, answer, false));
        displayFlashcard();

        flashcardContent.setOnClickListener(v -> flipFlashcard());
        KnownButton.setOnClickListener(v -> markAsKnown());
        shuffleButton.setOnClickListener(v -> shuffleFlashcards());
    }

    private void displayFlashcard() {
        Flashcard flashcard = flashcardList.get(currentIndex);
        flashcardContent.setText(isShowingQuestion ? flashcard.getQuestion() : flashcard.getAnswer());
    }

    private void flipFlashcard() {
        flashcardContent.animate().rotationY(90).setDuration(150).withEndAction(() -> {
            // Toggle between question and answer
            isShowingQuestion = !isShowingQuestion;
            displayFlashcard();
            flashcardContent.setRotationY(-90); // Reset rotation
            flashcardContent.animate().rotationY(0).setDuration(150).start();
        }).start();
    }

    private void markAsKnown() {
        Flashcard flashcard = flashcardList.get(currentIndex);
        flashcard.setKnown(true);
        db.collection("flashcards").document(flashcard.getId())
                .update("known", true)
                .addOnSuccessListener(aVoid -> {
                    flashcardList.remove(currentIndex);
                    if (flashcardList.isEmpty()) {
                        finish();
                    } else {
                        currentIndex = currentIndex % flashcardList.size();
                        displayFlashcard();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    private void shuffleFlashcards() {
        Collections.shuffle(flashcardList);
        currentIndex = 0;
        displayFlashcard();
    }
}