package com.iskandev.examrus.stresses;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

final class WordStress {

    private static final char[] VOWELS = new char[]{'а', 'е', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я', 'ё'};

    private String wordLowerCase;
    private int rightOptionIndex;

    /**
     * Will be shuffled
     */
    private ArrayList<String> wordOptions;


    WordStress (@NonNull final String word, final int rightStressNumber) {
        if (word.isEmpty() || rightStressNumber == 0)
            throw new IllegalArgumentException("Empty word or zero right stress num");

        this.wordLowerCase = word.toLowerCase();
        this.wordOptions = new ArrayList<>();
        formWordOptions(rightStressNumber);
    }

    void shuffleWordOptions(final String rightWordOption) {
        Collections.shuffle(this.wordOptions);
        this.rightOptionIndex = this.wordOptions.indexOf(rightWordOption);
    }

    private void formWordOptions(final int rightStressNumber) {
        String rightWordOption = "";

        for (int letterPos = 0; letterPos < this.wordLowerCase.length(); ++letterPos) {
            final char letter = this.wordLowerCase.charAt(letterPos);

            if (isLetterVowel(letter)) {
                this.wordOptions.add(getWordVowelUpperCase(letterPos));

                if (this.wordOptions.size() == rightStressNumber)
                    rightWordOption = this.wordOptions.get(this.wordOptions.size() - 1);
            }
        }

        /* Shuffling and getting index of right Words Option in array of all options x*/
        shuffleWordOptions(rightWordOption);
    }

    @NonNull
    private String getWordVowelUpperCase(final int vowelPos) {
        return wordLowerCase.substring(0, vowelPos) +
                Character.toUpperCase(wordLowerCase.charAt(vowelPos)) +
                wordLowerCase.substring(vowelPos + 1);
    }

    private static boolean isLetterVowel(final char letter) {
        for (char vowel : VOWELS)
            if (letter == vowel)
                return true;

        return false;
    }

    @NonNull
    String getWordLowerCase() {
        return wordLowerCase;
    }

    @NonNull
    ArrayList<String> getWordOptions() {
        return new ArrayList<>(wordOptions);
    }

    int getRightOptionIndex() {
        return rightOptionIndex;
    }
}
