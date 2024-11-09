package com.example.flashcardproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FlashcardEditActivity extends AppCompatActivity {

    private EditText questionInput;
    private EditText answerInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_edit);

        questionInput = findViewById(R.id.questionInput);
        answerInput = findViewById(R.id.answerInput);
        saveButton = findViewById(R.id.saveButton);

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
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

}