package com.example.flashcardproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Collections;
import java.util.List;

public class Flashcard_View_Screen extends AppCompatActivity {

    private List<Flashcard> flashcardList;
    private int currentIndex = 0;
    private boolean isShowingQuestion = true;
    private TextView flashcardContent;
    private Button markAsKnownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view_screen);

        flashcardContent = findViewById(R.id.flashcardContent);
        markAsKnownButton = findViewById(R.id.button);
        Button shuffleButton = findViewById(R.id.shuffleButton);

        // Initialize flashcard list
        flashcardList = getFlashcards();
        displayFlashcard();

        flashcardContent.setOnClickListener(v -> flipFlashcard());
        markAsKnownButton.setOnClickListener(v -> markAsKnown());
        shuffleButton.setOnClickListener(v -> shuffleFlashcards());

    }

    private List<Flashcard> getFlashcards() {
        // Retrieve flashcards (this could be from a database or static list)
        return List.of(
                new Flashcard("Question 1", "Answer 1"),
                new Flashcard("Question 2", "Answer 2")
        );
    }

    private void displayFlashcard() {
        Flashcard flashcard = flashcardList.get(currentIndex);
        flashcardContent.setText(isShowingQuestion ? flashcard.getQuestion() : flashcard.getAnswer());
    }

    private void flipFlashcard() {
        isShowingQuestion = !isShowingQuestion;
        displayFlashcard();
        flashcardContent.animate().rotationYBy(180).setDuration(300).start();
    }

    private void markAsKnown() {
        // Mark the current flashcard as known (this could involve updating a database)
        flashcardList.remove(currentIndex);
        if (flashcardList.isEmpty()) {
            finish(); // Close the activity if no flashcards are left
        } else {
            currentIndex = currentIndex % flashcardList.size();
            displayFlashcard();
        }
    }

    private void shuffleFlashcards() {
        Collections.shuffle(flashcardList);
        currentIndex = 0;
        displayFlashcard();
    }
}