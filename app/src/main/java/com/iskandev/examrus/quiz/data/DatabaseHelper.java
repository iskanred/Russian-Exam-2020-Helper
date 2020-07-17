package com.iskandev.examrus.quiz.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iskandev.examrus.LogTag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final File DB_FILE;
    private static final String DB_NAME = "russian_exam_database.db";
    private static final int DB_VERSION = 1;

    private static final String STRESSES_TABLE_NAME = "stresses_table";
    private static final String STRESSES_TABLE_RIGHT_OPTION_COLUMN_NAME = "right_option";
    private static final String STRESSES_TABLE_OPTION1_COLUMN_NAME = "option1";
    private static final String STRESSES_TABLE_OPTION2_COLUMN_NAME = "option2";
    private static final String STRESSES_TABLE_OPTION3_COLUMN_NAME = "option3";
    private static final String STRESSES_TABLE_OPTION4_COLUMN_NAME = "option4";
    private static final String STRESSES_TABLE_OPTION5_COLUMN_NAME = "option5";

    private static final String PARONYMS_CONFORMITY_TABLE_NAME = "paronyms_conformity_table";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM_WORD_COLUMN_NAME = "paronym_word";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM1_COLUMN_NAME = "paronym1";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM2_COLUMN_NAME = "paronym2";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM3_COLUMN_NAME = "paronym3";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM4_COLUMN_NAME = "paronym4";
    private static final String PARONYMS_CONFORMITY_TABLE_PARONYM5_COLUMN_NAME = "paronym5";


    private SQLiteDatabase dataBase;
    private final Context mContext;


    public DatabaseHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
        DB_FILE = mContext.getDatabasePath(DB_NAME);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {}

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) { }

    private void createDataBase() {
        if (!DB_FILE.exists()) {
            this.getReadableDatabase();
            Log.i(LogTag.INFO.toString(), "Database created");

            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LogTag.ERROR.toString(), "Error copying database", e);
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {
        final InputStream input = mContext.getAssets().open(DB_NAME);
        final String outFileName = DB_FILE.getAbsolutePath() + DB_NAME;
        final OutputStream output = new FileOutputStream(outFileName);
        final byte[] buffer = new byte[1024];
        int length;

        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    public void openDataBase() throws SQLException {
        createDataBase();
        final String path = DB_FILE.getAbsolutePath() + DB_NAME;
        dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (dataBase != null)
            dataBase.close();
        super.close();
    }

    @NonNull
    public ArrayList<CardQuizDataUnit> getStressesCardQuizData() {
        final Cursor cursor = dataBase.query(STRESSES_TABLE_NAME, null, null, null, null, null, null);
        final ArrayList<CardQuizDataUnit> stressesData = new ArrayList<>();

        if (cursor.moveToFirst()) {
            final int RIGHT_OPTION_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_RIGHT_OPTION_COLUMN_NAME);
            final int OPTION1_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_OPTION1_COLUMN_NAME);
            final int OPTION2_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_OPTION2_COLUMN_NAME);
            final int OPTION3_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_OPTION3_COLUMN_NAME);
            final int OPTION4_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_OPTION4_COLUMN_NAME);
            final int OPTION5_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_OPTION5_COLUMN_NAME);

            do {
                ArrayList<String> answerOptions = new ArrayList<>();
                answerOptions.add(cursor.getString(RIGHT_OPTION_COLUMN_INDEX));

                if (!cursor.isNull(OPTION1_COLUMN_INDEX))
                    answerOptions.add(cursor.getString(OPTION1_COLUMN_INDEX));
                if (!cursor.isNull(OPTION2_COLUMN_INDEX))
                    answerOptions.add(cursor.getString(OPTION2_COLUMN_INDEX));
                if (!cursor.isNull(OPTION3_COLUMN_INDEX))
                    answerOptions.add(cursor.getString(OPTION3_COLUMN_INDEX));
                if (!cursor.isNull(OPTION4_COLUMN_INDEX))
                    answerOptions.add(cursor.getString(OPTION4_COLUMN_INDEX));
                if (!cursor.isNull(OPTION5_COLUMN_INDEX))
                    answerOptions.add(cursor.getString(OPTION5_COLUMN_INDEX));

                stressesData.add(new CardQuizDataUnit(cursor.getString(OPTION1_COLUMN_INDEX).toLowerCase(), answerOptions));
            } while (cursor.moveToNext());
        }
        else {
            final Error error = new Error("stresses table is empty");
            error.printStackTrace();
            Log.e(LogTag.ERROR.toString(), error.getMessage(), error);
        }

        cursor.close();
        return stressesData;
    }

    public ArrayList<InputQuizDataUnit> getParonymsCardQuizData() {
        final Cursor cursor = dataBase.query(PARONYMS_CONFORMITY_TABLE_NAME, null, null, null, null, null, null);
        final ArrayList<InputQuizDataUnit> paronymsData = new ArrayList<>();

        if (cursor.moveToFirst()) {
            final int PARONYM_WORD_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM_WORD_COLUMN_NAME);
            final int PARONYM1_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM1_COLUMN_NAME);
            final int PARONYM2_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM2_COLUMN_NAME);
            final int PARONYM3_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM3_COLUMN_NAME);
            final int PARONYM4_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM4_COLUMN_NAME);
            final int PARONYM5_COLUMN_INDEX = cursor.getColumnIndex(PARONYMS_CONFORMITY_TABLE_PARONYM5_COLUMN_NAME);

            do {
                ArrayList<String> answers = new ArrayList<>();
                String paronymWord = cursor.getString(PARONYM_WORD_COLUMN_INDEX);

                if (!cursor.isNull(PARONYM1_COLUMN_INDEX))
                    answers.add(cursor.getString(PARONYM1_COLUMN_INDEX));
                if (!cursor.isNull(PARONYM2_COLUMN_INDEX))
                    answers.add(cursor.getString(PARONYM2_COLUMN_INDEX));
                if (!cursor.isNull(PARONYM3_COLUMN_INDEX))
                    answers.add(cursor.getString(PARONYM3_COLUMN_INDEX));
                if (!cursor.isNull(PARONYM4_COLUMN_INDEX))
                    answers.add(cursor.getString(PARONYM4_COLUMN_INDEX));
                if (!cursor.isNull(PARONYM5_COLUMN_INDEX))
                    answers.add(cursor.getString(PARONYM5_COLUMN_INDEX));

                paronymsData.add(new InputQuizDataUnit(paronymWord, answers));
            } while (cursor.moveToNext());
        }
        else {
            final Error error = new Error("paronyms conformity table is empty");
            error.printStackTrace();
            Log.e(LogTag.ERROR.toString(), error.getMessage(), error);
        }

        cursor.close();
        return paronymsData;
    }
}
