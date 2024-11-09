// Flashcard_View_Screen.java
package com.example.flashcardproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flashcard_View_Screen extends AppCompatActivity {

    private List<Flashcard> flashcardList;
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
        flashcardList = new ArrayList<>();

        // Get id, question, and answer from intent
        String id = getIntent().getStringExtra("id");
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");

        // Create a flashcard list with only the selected flashcard
        flashcardList.add(new Flashcard(id, question, answer));
        displayFlashcard();

        flashcardContent.setOnClickListener(v -> flipFlashcard());
        KnownButton.setOnClickListener(v -> markAsKnown());
        shuffleButton.setOnClickListener(v -> shuffleFlashcards());
    }

    private void displayFlashcard() {
        Flashcard flashcard = flashcardList.get(0);
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
        flashcardList.remove(0);
        if (flashcardList.isEmpty()) {
            finish();
        } else {
            displayFlashcard();
        }
    }

    private void shuffleFlashcards() {
        Random random = new Random();
        int index = random.nextInt(flashcardList.size());
        Flashcard flashcard = flashcardList.get(index);
        flashcardList.remove(index);
        flashcardList.add(0, flashcard);
        displayFlashcard();
    }
}