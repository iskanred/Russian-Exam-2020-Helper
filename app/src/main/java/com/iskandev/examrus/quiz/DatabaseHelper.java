package com.iskandev.examrus.quiz;

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

    //private static final String PARONYMS_TABLE_NAME = "paronyms_table";

    private static final String STRESSES_TABLE_NAME = "stresses_table";
    private static final String STRESSES_TABLE_RIGHT_OPTION_COLUMN_NAME = "right_option";
    private static final String STRESSES_TABLE_OPTION1_COLUMN_NAME = "option1";
    private static final String STRESSES_TABLE_OPTION2_COLUMN_NAME = "option2";
    private static final String STRESSES_TABLE_OPTION3_COLUMN_NAME = "option3";
    private static final String STRESSES_TABLE_OPTION4_COLUMN_NAME = "option4";
    private static final String STRESSES_TABLE_OPTION5_COLUMN_NAME = "option5";

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
    public ArrayList<QuizTaskData> getStressesData() {
        final Cursor cursor = dataBase.query(STRESSES_TABLE_NAME, null, null, null, null, null, null);
        final ArrayList<QuizTaskData> stressesData = new ArrayList<>();

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

                stressesData.add(new QuizTaskData(cursor.getString(OPTION1_COLUMN_INDEX).toLowerCase(), answerOptions));
            } while (cursor.moveToNext());
        }
        else {
            final Error error = new Error("stresses table is empty");
            error.printStackTrace();
            Log.e(LogTag.ERROR.toString(), "stresses table is empty", error);
        }

        cursor.close();
        return stressesData;
    }
}
