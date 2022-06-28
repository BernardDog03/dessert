package com.example.dessertin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper{

    //constructor
    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase. CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
            database.execSQL(sql);

    }

    //insertData
    public void insertData (String Name, String Resep, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record on data base table
        String sql = "INSERT INTO SAVE VALUES (NULL, ?, ?, ?)"; //where "SAVE" is table name in database we will create in mainactivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, Name);
        statement.bindString(2, Resep);
        statement.bindBlob(3, image);

        statement.executeInsert();
    }

    //updateData
    public void updateData(String Name, String Resep, byte[] image, int id){
         SQLiteDatabase database = getWritableDatabase();
         //query to update record
        String sql = "UPDATE SAVE SET Name=?, Resep=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, Name);
        statement.bindString(2, Resep);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double)id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM SAVE WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        statement.close();
    }

    public Cursor get(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

        @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

    }
}
