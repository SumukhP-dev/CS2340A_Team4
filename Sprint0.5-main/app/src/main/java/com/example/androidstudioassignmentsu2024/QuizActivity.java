package com.example.androidstudioassignmentsu2024;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private int currentFlashcardIndex = 0;
    private List<Flashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO 5: Set content view to activity_quiz.xml file and assign respective TextViews and button to XML bindings
        // HINT: look at the error messages below to see what you should name the differnt entities
        setContentView(R.layout.activity_quiz);

        TextView tvQuestion = findViewById(R.id.tvQuestion);
        TextView tvAnswer = findViewById(R.id.tvAnswer);
        Button btnShowAnswer = findViewById(R.id.btnShowAnswer);
        Button btnNextFlashcard = findViewById(R.id.btnNextFlashcard);
        Button btnMainMenu = findViewById(R.id.btnMainMenu);

        // TODO 6: Retreive flashcard list from singleton implementation



        // DO NOT MODIFY!
        updateQuestionAndAnswer(tvQuestion, tvAnswer);

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flashcards.isEmpty()) {
                    tvAnswer.setText(flashcards.get(currentFlashcardIndex).getAnswer());
                }
            }
        });

        btnNextFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flashcards.isEmpty()) {
                    currentFlashcardIndex = (currentFlashcardIndex + 1) % flashcards.size();
                    updateQuestionAndAnswer(tvQuestion, tvAnswer);
                }
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    // DO NOT MODIFY
    private void updateQuestionAndAnswer(TextView tvQuestion, TextView tvAnswer) {
        if (!flashcards.isEmpty()) {
            tvQuestion.setText(flashcards.get(currentFlashcardIndex).getQuestion());
            tvAnswer.setText("");  // Clear the answer when moving to the next flashcard
        }
    }
}
