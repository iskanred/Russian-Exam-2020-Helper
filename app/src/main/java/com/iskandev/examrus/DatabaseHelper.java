package com.iskandev.examrus;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iskandev.examrus.stresses.WordStress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final File DB_FILE;
    private static final String DB_NAME = "words.db";
    private static final int DB_VERSION = 1;

    private static final String STRESSES_TABLE_NAME = "stresses_words";
    //private static final String PARONYMS_TABLE_NAME = "paronyms_words";

    private static final String STRESSES_TABLE_WORD_COLUMN_NAME = "word";
    private static final String STRESSES_TABLE_STRESS_NUMBER_COLUMN_NAME = "stress_number";

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
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {}

    public void createDataBase() {
        if (!DB_FILE.exists()) {
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
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
    public ArrayList<WordStress> getWordStresses() {
        final Cursor cursor = dataBase.query(STRESSES_TABLE_NAME, null, null, null, null, null, null);
        final ArrayList<WordStress> wordStresses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            final int WORD_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_WORD_COLUMN_NAME);
            final int STRESS_NUMBER_COLUMN_INDEX = cursor.getColumnIndex(STRESSES_TABLE_STRESS_NUMBER_COLUMN_NAME);

            do {
                final String word = cursor.getString(WORD_COLUMN_INDEX);
                final int stressNumber = cursor.getInt(STRESS_NUMBER_COLUMN_INDEX);

                wordStresses.add(new WordStress(word, stressNumber));

            } while (cursor.moveToNext());
        } else {
            final Error error = new Error("stresses table is empty");
            Log.e(LogTag.ERROR.toString(), "stresses table is empty", error);
        }

        cursor.close();
        return wordStresses;
    }
}
