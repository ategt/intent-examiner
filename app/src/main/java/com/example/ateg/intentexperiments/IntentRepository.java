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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ateg.intentexperiments.annotations.ApplicationContext;
import com.example.ateg.intentexperiments.annotations.DatabaseInfo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ATeg on 10/27/2017.
 */

public class IntentRepository {

    private static final String TAG = "Intent SQLite";

    private Gson gson;
    private DatabaseHelper databaseHelper;

    private static final String INTENT_EXAMINER_TABLE_NAME = "intent_examiner";

    private static final String INTENT_EXAMINER_COLUMN_ROWID = "id";
    private static final String INTENT_EXAMINER_COLUMN_EXTRAS = "extras";
    private static final String INTENT_EXAMINER_COLUMN_INTENT = "intent";
    private static final String INTENT_EXAMINER_COLUMN_ACTION = "action";
    private static final String INTENT_EXAMINER_COLUMN_CATEGORIES = "categories";
    private static final String INTENT_EXAMINER_COLUMN_FLAGS = "flags";
    private static final String INTENT_EXAMINER_COLUMN_TYPE = "type";
    private static final String INTENT_EXAMINER_COLUMN_DATA_STRING = "dataString";
    private static final String INTENT_EXAMINER_COLUMN_INTENT_PACKAGE = "package";
    private static final String INTENT_EXAMINER_COLUMN_COMPONENT = "component";
    private static final String INTENT_EXAMINER_COLUMN_SCHEME = "scheme";
    private static final String INTENT_EXAMINER_COLUMN_DATA = "data";
    private static final String INTENT_EXAMINER_COLUMN_CLIP_DATA = "clipData";

    private static final String SQL_CREATE_INTENT_TABLE = "CREATE TABLE IF NOT EXISTS " + INTENT_EXAMINER_TABLE_NAME + " (" +
            INTENT_EXAMINER_COLUMN_INTENT + " TEXT, " +
            INTENT_EXAMINER_COLUMN_EXTRAS + " TEXT, " +
            INTENT_EXAMINER_COLUMN_ACTION + " TEXT, " +
            INTENT_EXAMINER_COLUMN_CATEGORIES + " TEXT, " +
            INTENT_EXAMINER_COLUMN_FLAGS + " INTEGER, " +
            INTENT_EXAMINER_COLUMN_TYPE + " TEXT, " +
            INTENT_EXAMINER_COLUMN_DATA_STRING + " TEXT, " +
            INTENT_EXAMINER_COLUMN_INTENT_PACKAGE + " TEXT, " +
            INTENT_EXAMINER_COLUMN_COMPONENT + " TEXT, " +
            INTENT_EXAMINER_COLUMN_SCHEME + " TEXT, " +
            INTENT_EXAMINER_COLUMN_DATA + " TEXT, " +
            INTENT_EXAMINER_COLUMN_CLIP_DATA + " TEXT, " +
            "archived BIT DEFAULT false)";
    private static final String SQL_DROP_INTENT_TABLE = "DROP TABLE IF EXISTS " + INTENT_EXAMINER_TABLE_NAME;

    private static final String SQL_GET_ALL_INTENT = "SELECT *, ROWID AS id FROM " + INTENT_EXAMINER_TABLE_NAME;
    private static final String SQL_GET_DIFFERENTIAL_INTENT = "SELECT *, ROWID AS id FROM " + INTENT_EXAMINER_TABLE_NAME + " WHERE archived = 'false'";
    private static final String SQL_MARK_ARCHIVED = "UPDATE " + INTENT_EXAMINER_TABLE_NAME + " SET archived = 'true' WHERE archived = 'false'";

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

    public IntentWrapper create(IntentWrapper intentWrapper) {
        if (intentWrapper == null)
            return null;

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(INTENT_EXAMINER_COLUMN_SCHEME, intentWrapper.getScheme());
        contentValues.put(INTENT_EXAMINER_COLUMN_DATA_STRING, intentWrapper.getDataString());
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT, intentWrapper.getIntentJson());

        Intent intent = intentWrapper.getIntent();

        //contentValues.put(INTENT_EXAMINER_COLUMN_EXTRAS, gson.toJson(intent.getExtras()));
        contentValues.put(INTENT_EXAMINER_COLUMN_ACTION, intent.getAction());
        contentValues.put(INTENT_EXAMINER_COLUMN_CATEGORIES, gson.toJson(intent.getCategories()));
        contentValues.put(INTENT_EXAMINER_COLUMN_FLAGS, intent.getFlags());
        contentValues.put(INTENT_EXAMINER_COLUMN_TYPE, intent.getType());
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT_PACKAGE, intent.getPackage());
        contentValues.put(INTENT_EXAMINER_COLUMN_COMPONENT, gson.toJson(intent.getComponent()));
        contentValues.put(INTENT_EXAMINER_COLUMN_CLIP_DATA, gson.toJson(intent.getClipData()));
        contentValues.put(INTENT_EXAMINER_COLUMN_DATA, gson.toJson(intent.getData()));

        Map<String, Parcelable> parcelableMap = new HashMap<>();
        Set<String> keys = intent.getExtras().keySet();
        for (String key : keys) {
            Parcelable parcelable = intent.getExtras().getParcelable(key);
            parcelableMap.put(key, parcelable);
        }
        contentValues.put(INTENT_EXAMINER_COLUMN_EXTRAS, gson.toJson(parcelableMap));

        Long id = sqLiteDatabase.insertOrThrow(INTENT_EXAMINER_TABLE_NAME, null, contentValues);

        intentWrapper.setId(id.intValue());

        sqLiteDatabase.close();
        return intentWrapper;
    }

    public List<IntentWrapper> getAll() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_GET_ALL_INTENT, null);

        List<IntentWrapper> intentList = buildIntentList(cursor);

        sqLiteDatabase.close();
        return intentList;
    }

    public List<IntentWrapper> getDifferential() {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_GET_DIFFERENTIAL_INTENT, null);

        List<IntentWrapper> intentList = buildIntentList(cursor);

        sqLiteDatabase.close();
        return intentList;
    }

    @NonNull
    private List<IntentWrapper> buildIntentList(Cursor cursor) {
        List<IntentWrapper> intentList = new ArrayList<>();
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

    private IntentWrapper getIntent(Cursor cursor) {
        Long longId = cursor.getLong(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_ROWID));
        Integer id = (longId > Integer.MAX_VALUE) ? null : longId.intValue();
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
        String dataJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_DATA));
        String clipDataJson = cursor.getString(cursor.getColumnIndex(INTENT_EXAMINER_COLUMN_CLIP_DATA));
        ////////////////
        /*
        contentValues.put(INTENT_EXAMINER_COLUMN_SCHEME, intentWrapper.getScheme());

        contentValues.put(INTENT_EXAMINER_COLUMN_DATA_STRING, intentWrapper.getDataString());
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT, intentWrapper.getIntentJson());

        Intent intent = intentWrapper.getIntent();

        contentValues.put(INTENT_EXAMINER_COLUMN_EXTRAS, gson.toJson(intent.getExtras()));
        contentValues.put(INTENT_EXAMINER_COLUMN_ACTION, intent.getAction());
        contentValues.put(INTENT_EXAMINER_COLUMN_CATEGORIES, gson.toJson(intent.getCategories()));
        contentValues.put(INTENT_EXAMINER_COLUMN_FLAGS, intent.getFlags());
        contentValues.put(INTENT_EXAMINER_COLUMN_TYPE, intent.getType());
        contentValues.put(INTENT_EXAMINER_COLUMN_INTENT_PACKAGE, intent.getPackage());
        contentValues.put(INTENT_EXAMINER_COLUMN_COMPONENT, gson.toJson(intent.getComponent()));
        contentValues.put(INTENT_EXAMINER_COLUMN_SCHEME, intent.getScheme());
        */

        ComponentName componentName = gson.fromJson(componentJson, ComponentName.class);


        //ClassLoader classLoader = new ClassLoader

        //Bundle bundle = new Bundle();
        //bundle.

//        new GsonBuilder().registerTypeHierarchyAdapter(ClassLoader.class, new TypeAdapter<ClassLoader>() {
//            @Override
//            public void write(JsonWriter out, ClassLoader value) throws IOException {
//                out.name(value.getClass().getCanonicalName());
//
//                Enumeration<URL> urls = value.getResources("");
//                while (urls.hasMoreElements()) {
//                    URL url = urls.nextElement();
//                    out.value(url.toString());
//                }
//            }
//
//            @Override
//            public ClassLoader read(JsonReader in) throws IOException {
////                new Parcelable.ClassLoaderCreator().
////                //ClassLoader classLoader = new ClassLoader
////
////                while (in.hasNext()) {
////                    in.nextString()
////                }
//                return null;
//            }
//        }).create();


        Map<String, Parcelable> parcelableMap = gson.fromJson(extrasJson, HashMap.class);

        //Bundle extras = gson.fromJson(extrasJson, Bundle.class);

        Bundle extras = new Bundle();

        for (String key : parcelableMap.keySet()) {
            extras.putParcelable(key, parcelableMap.get(key));
        }

        Uri data = gson.fromJson(dataJson, Uri.class);
        ClipData clipData = gson.fromJson(clipDataJson, ClipData.class);
        String[] categories = gson.fromJson(categoriesJson, String[].class);

        Intent intent = new Intent();

        intent.setAction(action);
        intent.setComponent(componentName);
        intent.setFlags(flags);
        intent.putExtras(extras);
        intent.setType(type);
        intent.setData(data);
        intent.setClipData(clipData);
        intent.setPackage(packageName);

        if (categories != null)
            for (String category : categories) {
                intent.addCategory(category);
            }

        IntentWrapper intentWrapper = new IntentWrapper(intent);

        intentWrapper.setIntentJson(intentJson);
        intentWrapper.setId(id);

        return intentWrapper;
    }

    private void resetDatabase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_INTENT_TABLE);
        databaseHelper.onCreate(sqLiteDatabase);
    }
}