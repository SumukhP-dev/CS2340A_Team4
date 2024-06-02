package com.example.androidstudioassignmentsu2024;
import com.example.androidstudioassignmentsu2024.FlashcardManagerSingleton;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    // TODO 7: Write a unit test for ensuring correct behavior of the addFlashcard function you implemented in TODO 2 in FlashcardManagerSingletons
    @Test
    public void add_flashcard_test() {
        List<Flashcard> flashcards = new ArrayList<>();
    //
        String question = "Question1";
        String answer = "1";
        Flashcard flashcard = new Flashcard(question, answer);
        flashcards.add(flashcard);

        FlashcardManagerSingleton.getInstance().addFlashcard(flashcard);

        assertEquals(flashcards, FlashcardManagerSingleton.getInstance().getFlashcards());

    }
}