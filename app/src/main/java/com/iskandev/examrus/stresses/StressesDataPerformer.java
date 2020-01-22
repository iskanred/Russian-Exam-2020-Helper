package com.iskandev.examrus.stresses;

import android.content.Context;


import androidx.annotation.Nullable;

import com.iskandev.examrus.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

final class StressesDataPerformer {

    final static int WORDS_AMOUNT = 24;

    private ArrayList<WordStress> wordStressesOrdered;
    private ArrayList<WordStress> wordStressesShuffled;

    StressesDataPerformer(final Context context) {
        extractWordStressesFromDB(context);
        shuffleWordStresses();
    }

    @Nullable
    WordStress getWordStress(final int index) {
        return wordStressesShuffled.get(index);
    }

    void update () {
        shuffleWordStresses();
    }

    private void extractWordStressesFromDB(final Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.createDataBase();
        databaseHelper.openDataBase();
        wordStressesOrdered = databaseHelper.getWordStresses();
    }

    private void shuffleWordStresses() {
        wordStressesShuffled = new ArrayList<>(wordStressesOrdered);
        Collections.shuffle(wordStressesShuffled);

        for (WordStress wordStress : wordStressesShuffled) {
            wordStress.shuffleWordOptions(wordStress.getWordOptions().get(wordStress.getRightOptionIndex()));
        }
    }
}