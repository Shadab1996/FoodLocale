package com.stinkinsweet.foodlocale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Funkies PC on 8/20/2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper
{
    public static final int database_version=1;
    public String CREATE_QUERY="CREATE TABLE "+ TableData.TableInfo.TABLE_NAME+"("+ TableData.TableInfo.USER_NAME+" TEXT ,"+ TableData.TableInfo.USER_PASS+" TEXT );";

    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("DatabaseOperations: ","YEAH BABY DATABASE CREATED");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb .execSQL(CREATE_QUERY);
        Log.d("DatabaseOperations: ","YEAH BABY TABLE CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertInformation(DatabaseOperations dop,String name,String pass)
    {
        SQLiteDatabase SQ=dop.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TableData.TableInfo.USER_NAME,name);
        cv.put(TableData.TableInfo.USER_PASS,pass);
        long k=SQ.insert(TableData.TableInfo.TABLE_NAME,null,cv);
        Log.d("DatabaseOperations: ","YEAH BABY 1 ROW INSERTED");


    }

    public Cursor getInformation(DatabaseOperations dop) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.USER_NAME, TableData.TableInfo.USER_PASS};
        Cursor CR = SQ.query(TableData.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return CR;

    }
}
