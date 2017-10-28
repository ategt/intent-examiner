package com.example.ateg.intentexperiments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ateg.intentexperiments.annotations.ApplicationContext;
import com.example.ateg.intentexperiments.annotations.DatabaseInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 10/27/2017.
 */

public class IntentRepository {

    private static final String TAG = "Intent SQLite";

    private Gson gson;
    private DatabaseHelper databaseHelper;

    public static final String INTENT_EXAMINER_TABLE_NAME = "intent_examiner";
    private static final String INTENT_EXAMINER_COLUMN_INTENT = "intent";

    private static final String SQL_CREATE_INTENT_TABLE = "CREATE TABLE IF NOT EXISTS intent_examiner (intent TEXT, archived BIT DEFAULT false)";
    private static final String SQL_DROP_INTENT_TABLE = "DROP TABLE IF EXISTS intent_examiner";

    private static final String SQL_INSERT_INTENT = "INSERT INTO intent_examiner (intent) VALUES (?)";
    private static final String SQL_GET_ALL_INTENT = "SELECT * FROM intent_examiner";
    private static final String SQL_GET_DIFFERENTIAL_INTENT = "SELECT * FROM intent_examiner WHERE archived = 'false'";
    private static final String SQL_MARK_ARCHIVED = "UPDATE intent_examiner SET archived = 'true' WHERE archived = 'false'";

    public IntentRepository(@ApplicationContext Context context,
                            @DatabaseInfo String name,
                            @DatabaseInfo int version) {
        databaseHelper = new DatabaseHelper(context, name, null, version);
        gson = new GsonBuilder().create();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            tableCreateStatements(sqLiteDatabase);
        }

        private void tableCreateStatements(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(SQL_CREATE_INTENT_TABLE);
            } catch (SQLException ex) {
                Log.d(TAG, "Table creation problem.", ex);
                ex.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            resetDatabase(sqLiteDatabase);
        }
    }
    public Intent create(Intent intent) {
        if (intent == null)
            return null;

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT, gson.toJson(intent));

        sqLiteDatabase.insertOrThrow(INTENT_EXAMINER_TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return intent;
    }

    public List<Intent> getAll() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_GET_ALL_INTENT, null);

        List<Intent> intentList = buildIntentList(cursor);

        sqLiteDatabase.close();
        return intentList;
    }

    public List<Intent> getDifferential() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_GET_DIFFERENTIAL_INTENT, null);

        List<Intent> intentList = buildIntentList(cursor);

        sqLiteDatabase.close();
        return intentList;
    }

    @NonNull
    private List<Intent> buildIntentList(Cursor cursor) {
        List<Intent> intentList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                intentList.add(getIntent(cursor));
            }
            while (cursor.moveToNext());
        } else {
            // Database empty
        }
        return intentList;
    }

    public void markAllArchived(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_MARK_ARCHIVED);
    }

    public void deleteAll(){
        resetDatabase(databaseHelper.getWritableDatabase());
    }

    private Intent getIntent(Cursor cursor) {
        String resultJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_INTENT));
        return gson.fromJson(resultJson, Intent.class);
    }

    private void resetDatabase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_INTENT_TABLE);
        databaseHelper.onCreate(sqLiteDatabase);
    }
}