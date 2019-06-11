package com.lamtt.learnenglish.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lamtt.learnenglish.object.Category;
import com.lamtt.learnenglish.object.Phrase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static String DB_NAME = "data.db";
    private SQLiteDatabase myDataBase;
    private Context myContext;
    private final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(DB_NAME).toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "[onCreate]");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {

        } else {
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    public List<Category> getAllCategory() {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Category> categoryList = new ArrayList<>();
        String sql = "Select * from topic";
        Cursor cursor = myDataBase.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            categoryList.add(new Category(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return categoryList;
    }

    public int getNumCategoryActive(){
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Category> categoryList = new ArrayList<>();
        String sql = "Select * from topic where active = 1 ";
        Cursor cursor = myDataBase.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            categoryList.add(new Category(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return categoryList.size();
    }

    public int getSizeCategory(){
        return getAllCategory().size();
    }

    public List<Phrase> getListPhraseByTag(String tag) {

        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Phrase> phraseList = new ArrayList<>();
        String sql = "Select * from phrases where tag = '" + tag + "'";
        Cursor cursor = myDataBase.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            phraseList.add(new Phrase(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6)));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return phraseList;
    }

    public List<Phrase> getListFavourite(){
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Phrase> phraseList = new ArrayList<>();
        String sql = "Select * from phrases where  favorite = '1'";
        Cursor cursor = myDataBase.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            phraseList.add(new Phrase(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6)));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return phraseList;
    }

    public void updatePhrase(Phrase phrase) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues ct = new ContentValues();
        ct.put("favorite", phrase.getFavorite());
        int k = myDataBase.update("phrases", ct, "idphrases=" + phrase.getIdPhrase(), null);
        close();
        Log.d("Update", k + "___////");
    }

    public void updateCategory(int id , int active){
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues ct = new ContentValues();
        ct.put("active", active);
        myDataBase.update("topic", ct, "id=" +  id, null);
        close();
    }

    public int getIdByTag(String tag){
        int id = 0;
        try {
            openDataBase();
            String sql = "Select * from topic where unsignvi = '" + tag + "'";
            Cursor cursor = myDataBase.rawQuery(sql, null);
            cursor.moveToFirst();
            id = cursor.getInt(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return id;
    }

    public List<Phrase> randomListQuiz() {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Phrase> phraseList = new ArrayList<>();
        String sql = "select * from phrases ORDER BY RANDOM() LIMIT 20";
        Cursor cursor = myDataBase.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            phraseList.add(new Phrase(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6)));
            cursor.moveToNext();
        }
        close();
        return phraseList;
    }
}
