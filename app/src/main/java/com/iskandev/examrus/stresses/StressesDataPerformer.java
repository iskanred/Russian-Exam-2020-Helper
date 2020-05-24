package com.iskandev.examrus.stresses;

import android.content.Context;
import android.util.Log;

import com.iskandev.examrus.DatabaseHelper;
import com.iskandev.examrus.LogTag;

import java.util.ArrayList;
import java.util.Collections;

final class StressesDataPerformer {

    private int currentWordIndex;
    private WordStress currentWordStress;

    private ArrayList<WordStress> wordStressesOrdered;
    private ArrayList<WordStress> wordStressesShuffled;

    StressesDataPerformer(final Context context) {
        currentWordIndex = 0;
        extractWordStressesFromDB(context);
        shuffleWordStresses();
    }

    /**
     *
     * @throws IndexOutOfBoundsException if tasks is run out
     */
    void performNextTaskData() throws IndexOutOfBoundsException {
        currentWordStress = wordStressesShuffled.get(currentWordIndex);
        ++currentWordIndex;
    }

    String getQuizWord() {
        return currentWordStress.getWordLowerCase();
    }

    ArrayList<String> getWordOptions() {
        return currentWordStress.getWordOptions();
    }

    int getCorrectOptionIndex() {
        return currentWordStress.getCorrectOptionIndex();
    }

    private void extractWordStressesFromDB(final Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.openDataBase();
        wordStressesOrdered = databaseHelper.getWordStresses();
        Log.i(LogTag.INFO.toString(), "Data extracted");
    }

    private void shuffleWordStresses() {
        wordStressesShuffled = new ArrayList<>(wordStressesOrdered);
        Collections.shuffle(wordStressesShuffled);

        for (WordStress wordStress : wordStressesShuffled) {
            wordStress.shuffleWordOptions(wordStress.getWordOptions().get(wordStress.getCorrectOptionIndex()));
        }
    }
}