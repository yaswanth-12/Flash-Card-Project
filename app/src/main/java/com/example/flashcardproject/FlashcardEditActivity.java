// FlashcardEditActivity.java
package com.example.flashcardproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class FlashcardEditActivity extends AppCompatActivity {

    private EditText questionInput;
    private EditText answerInput;
    private Button saveButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_edit);

        questionInput = findViewById(R.id.questionInput);
        answerInput = findViewById(R.id.answerInput);
        saveButton = findViewById(R.id.saveButton);
        db = FirebaseFirestore.getInstance();

        saveButton.setOnClickListener(v -> saveFlashcard());
    }

    private void saveFlashcard() {
        String question = questionInput.getText().toString().trim();
        String answer = answerInput.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            Toast.makeText(this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("question", question);
        resultIntent.putExtra("answer", answer);

        int position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            resultIntent.putExtra("position", position);
            updateFlashcardInFirestore(question, answer, position);
        } else {
            addFlashcardToFirestore(question, answer);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void addFlashcardToFirestore(String question, String answer) {
        String id = db.collection("flashcards").document().getId();
        Flashcard flashcard = new Flashcard(id, question, answer, false);
        db.collection("flashcards")
                .document(id)
                .set(flashcard)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Flashcard added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding flashcard", Toast.LENGTH_SHORT).show());
    }

    private void updateFlashcardInFirestore(String question, String answer, int position) {
        String documentId = getDocumentIdForPosition(position);
        db.collection("flashcards").document(documentId)
                .update("question", question, "answer", answer)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Flashcard updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating flashcard", Toast.LENGTH_SHORT).show());
    }

    private String getDocumentIdForPosition(int position) {
        // Implement this method to return the document ID for the flashcard at the given position
        return "";
    }
}