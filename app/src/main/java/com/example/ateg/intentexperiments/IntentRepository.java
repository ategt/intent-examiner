package com.example.ateg.intentexperiments;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ateg.intentexperiments.annotations.ApplicationContext;
import com.example.ateg.intentexperiments.annotations.DatabaseInfo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
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


    private static final String INTENT_EXAMINER_COLUMN_EXTRAS = "extras";
    private static final String INTENT_EXAMINER_COLUMN_ACTION = "action";
    private static final String INTENT_EXAMINER_COLUMN_CATEGORIES = "categories";
    private static final String INTENT_EXAMINER_COLUMN_FLAGS = "flags";
    private static final String INTENT_EXAMINER_COLUMN_TYPE = "type";
    private static final String INTENT_EXAMINER_COLUMN_DATA_STRING = "dataString";
    private static final String INTENT_EXAMINER_COLUMN_INTENT_PACKAGE = "package";
    private static final String INTENT_EXAMINER_COLUMN_COMPONENT = "component";
    private static final String INTENT_EXAMINER_COLUMN_SCHEME = "scheme";

    private static final String SQL_CREATE_INTENT_TABLE = "CREATE TABLE IF NOT EXISTS intent_examiner (intent TEXT, " +
            "extras TEXT, " +
            "action TEXT, " +
            "categories TEXT, " +
            "flags INTEGER, " +
            "type TEXT, " +
            "dataString TEXT, " +
            "package TEXT, " +
            "component TEXT, " +
            "scheme TEXT, " +
            "archived BIT DEFAULT false)";
    private static final String SQL_DROP_INTENT_TABLE = "DROP TABLE IF EXISTS intent_examiner";

    private static final String SQL_GET_ALL_INTENT = "SELECT * FROM intent_examiner";
    private static final String SQL_GET_DIFFERENTIAL_INTENT = "SELECT * FROM intent_examiner WHERE archived = 'false'";
    private static final String SQL_MARK_ARCHIVED = "UPDATE intent_examiner SET archived = 'true' WHERE archived = 'false'";

    public IntentRepository(@ApplicationContext Context context,
                            @DatabaseInfo String name,
                            @DatabaseInfo int version) {
        databaseHelper = new DatabaseHelper(context, name, null, version);
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if (clazz.isInstance(java.lang.ClassLoader.class))
                    return true;
                else
                    return false;
            }
        }).create();
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

        contentValues.put(INTENT_EXAMINER_COLUMN_EXTRAS, gson.toJson(intent.getExtras()));
        contentValues.put(INTENT_EXAMINER_COLUMN_ACTION, intent.getAction());
        contentValues.put(INTENT_EXAMINER_COLUMN_CATEGORIES, gson.toJson(intent.getCategories()));
        contentValues.put(INTENT_EXAMINER_COLUMN_FLAGS, intent.getFlags());
        contentValues.put(INTENT_EXAMINER_COLUMN_TYPE, intent.getType());
        contentValues.put(INTENT_EXAMINER_COLUMN_DATA_STRING, intent.getDataString());
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT_PACKAGE, intent.getPackage());
        contentValues.put(INTENT_EXAMINER_COLUMN_COMPONENT, gson.toJson(intent.getComponent()));
        contentValues.put(INTENT_EXAMINER_COLUMN_SCHEME, intent.getScheme());

        //ComponentName componentName = intent.getComponent();

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

    public void markAllArchived() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_MARK_ARCHIVED);
    }

    public void deleteAll() {
        resetDatabase(databaseHelper.getWritableDatabase());
    }

    private Intent getIntent(Cursor cursor) {
        String intentJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_INTENT));
        String action = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_ACTION));
        String extrasJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_EXTRAS));
        String categoriesJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_CATEGORIES));
        Integer flags = cursor.getInt(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_FLAGS));
        String type = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_TYPE));
        String dataString = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_DATA_STRING));
        String packageName = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_INTENT_PACKAGE));
        String componentJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_COMPONENT));
        String scheme = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_SCHEME));
        //String dataJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_DATA));
        //String clipDataJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_CLIP_DATA));

        ComponentName componentName = gson.fromJson(componentJson, ComponentName.class);
        Bundle extras = gson.fromJson(extrasJson, Bundle.class);
        //Uri data = gson.fromJson(dataJson, Uri.class);
        //ClipData clipData = gson.fromJson(clipDataJson, ClipData.class);
        String[] categories = gson.fromJson(categoriesJson, String[].class);

        Intent intent = new Intent();

        intent.setAction(action);
        intent.setComponent(componentName);
        intent.setFlags(flags);
        intent.putExtras(extras);
        intent.setType(type);
        //intent.setData(data);
        //intent.setClipData(clipData);
        intent.setPackage(packageName);

        for (String category : categories) {
            intent.addCategory(category);
        }

        return intent;
    }

    private void resetDatabase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_INTENT_TABLE);
        databaseHelper.onCreate(sqLiteDatabase);
    }
}