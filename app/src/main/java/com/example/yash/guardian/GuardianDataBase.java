package com.example.yash.guardian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GuardianDataBase extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "GuardianData.db";
    public final static String TABLE_NAME = "GuardianTABLE";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "NAME";
    public final static String COL_3 = "PHONENUMBER";



    public GuardianDataBase(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PHONENUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertdata(String name,String phonenumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phonenumber);
        long success = db.insert(TABLE_NAME,null,contentValues);
        if(success == -1)
            return false;
        else
            return true;

    }


    public Cursor viewalldata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updatedata(String id,String name,String phonenumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phonenumber);
        int effected = db.update(TABLE_NAME,contentValues,"ID = ?",new String[] { id } );
        if(effected>0)
            return true;
        else
            return false;
    }

    public boolean deletedata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int effected = db.delete(TABLE_NAME,"ID = ?",new String[] { id });
        if(effected>0)
            return true;
        else
            return false;
    }

    public List<String> NamesForCA()
    {
        ArrayList<String> names = new ArrayList();
        SQLiteDatabase mydb = this.getWritableDatabase();
        Cursor res = mydb.rawQuery("select * from "+TABLE_NAME,null);
        res.moveToFirst();
        if(res.getCount()>0) {
            do {
                names.add(res.getString(1));
            }
            while (res.moveToNext());
        }
        else
        {
            names.add("No Entered Guardians");
        }
        return names;
    }

    public List<String> PhoneNoForCA()
    {
        ArrayList<String> phonenumbers = new ArrayList();
        SQLiteDatabase mydb = this.getWritableDatabase();
        Cursor res = mydb.rawQuery("select * from "+TABLE_NAME,null);
        res.moveToFirst();
        if(res.getCount()>0) {
            do {
                phonenumbers.add(res.getString(2));
            }
            while (res.moveToNext());
        }
        else
        {
            phonenumbers.add("No Entered Guardians");
        }
        return phonenumbers;
    }

    public  List<String> IdForCA()
    {
        List<String> ids = new ArrayList<String>();
        SQLiteDatabase mydb = this.getWritableDatabase();
        Cursor res = mydb.rawQuery("select * from "+TABLE_NAME,null);
        res.moveToFirst();
        if(res.getCount()>0) {
            do {
                ids.add(res.getString(0));
            }
            while (res.moveToNext());
        }
        else
        {
            ids.add("No Entered Guardians");
        }
        return ids;
    }
}
