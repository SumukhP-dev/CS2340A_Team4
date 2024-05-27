package com.example.androidstudioassignmentsu2024;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class CreateFlashcardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard);

        final EditText etQuestion = findViewById(R.id.etQuestion);
        final EditText etAnswer = findViewById(R.id.etAnswer);
        Button btnSaveFlashcard = findViewById(R.id.btnSaveFlashcard);


        // TODO 3: Program the btnSaveFlashcard button as a callback for saveFlashcardData
        // After the data is saved, the button should also have the application return to the main screen automatically
    }


    /**
     * DO NOT MODIFY
     */
    protected void saveFlashcardData(EditText etQuestion, EditText etAnswer) {
        String question = etQuestion.getText().toString();
        String answer = etAnswer.getText().toString();
        Flashcard flashcard = new Flashcard(question, answer);
        FlashcardManagerSingleton.getInstance().addFlashcard(flashcard);
        // Hide the keyboard
        hideKeyboard();
    }


    /**
     * DO NOT MODIFY
     */
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
