package com.iskandev.examrus.stresses;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

final class StressesDataPerformer {

    final static int WORDS_AMOUNT = 5;

    private ArrayList<WordStress> wordStressesOrdered;
    private ArrayList<WordStress> wordStressesShuffled;

    StressesDataPerformer() {
        extractWordStressesFromDB();
        shuffleWordStresses();
    }

    @Nullable
    WordStress getWordStress(final int index) {
        return wordStressesShuffled.get(index);
    }

    void update () {
        shuffleWordStresses();
    }

    private void extractWordStressesFromDB() {
        wordStressesOrdered = new ArrayList<>();
        wordStressesOrdered.add(new WordStress("обвзонить", 3));
        wordStressesOrdered.add(new WordStress("обвзонит", 3));
        wordStressesOrdered.add(new WordStress("облегчить", 3));
        wordStressesOrdered.add(new WordStress("облегчит", 3));
        wordStressesOrdered.add(new WordStress("опломбировать", 5));
    }

    private void shuffleWordStresses() {
        wordStressesShuffled = new ArrayList<>(wordStressesOrdered);
        Collections.shuffle(wordStressesShuffled);

        for (WordStress wordStress : wordStressesShuffled) {
            wordStress.shuffleWordOptions(wordStress.getWordOptions().get(wordStress.getRightOptionIndex()));
        }
    }
}