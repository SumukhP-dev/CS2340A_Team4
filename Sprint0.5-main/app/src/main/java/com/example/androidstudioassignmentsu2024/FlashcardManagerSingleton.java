// DO NOT MODIFY THIS CLASS!
package com.example.androidstudioassignmentsu2024;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManagerSingleton {
    // DO NOT MODIFY!
    private static FlashcardManagerSingleton instance;
    private List<Flashcard> flashcards;

    private FlashcardManagerSingleton() {
        flashcards = new ArrayList<>();
    }

    public static FlashcardManagerSingleton getInstance() {
        if (instance == null) {
            instance = new FlashcardManagerSingleton();
        }
        return instance;
    }


    // TODO 2: Implement addFlashcard functionality
    // HINT: look at the data structure type!
    public void addFlashcard(Flashcard flashcard) {
    }


    // DO NOT MODIFY!
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }
}
