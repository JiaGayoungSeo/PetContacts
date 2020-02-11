package com.cornez.petcontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "petManager";
    private static final String TABLE_NAME = "contacts";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IMAGEURI = "imageUri";

    public DBHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Called when the database is created for the first time
    //When it is created for the first time it creates a table with columns needed
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DETAIL + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_IMAGEURI + " TEXT)" );
    }

    //Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    //Called when the user want to insert new Pet instance into the database
    public void createContact(Pet pet){
        //Get database in which to insert pet's data
        SQLiteDatabase db = getWritableDatabase();
        //Create insert statement
        String insert = "INSERT or replace INTO " + TABLE_NAME +  "("
                + KEY_NAME +", "
                + KEY_DETAIL + ", "
                + KEY_PHONE +", "
                + KEY_IMAGEURI + ") " +
                "VALUES('"
                + pet.getName() + "','"
                + pet.getDetails() + "','"
                + pet.getPhone() + "','"
                + pet.getPhotoURI() +"')" ;
        //Execute SQL
        db.execSQL(insert);
        //Close the database
        db.close();
    }

    //Returns Pet instance(Pet information) when inputting its id.
    public Pet getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_DETAIL, KEY_PHONE, KEY_IMAGEURI}, KEY_ID + "=?", new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        Pet pet = new Pet(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4)));
        db.close();
        cursor.close();

        return pet;
    }

    //Delete pet in database
    public void deleteContact(Pet pet){
        //Get writableDatabase
        SQLiteDatabase db = getWritableDatabase();
        //Execute delete query using pet's id
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(pet.getId())});
        //db.execSQL();
        db.close();
    }
    //Returns the number of pets in database using cursor
    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        //Runs the provided SQL and returns a Cursor over the result set
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        //Returns the number of rows in the cursor
        int count = cursor.getCount();

        db.close();
        cursor.close();

        return count;
    }


    public int updateContact(Pet pet){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pet.getName());
        values.put(KEY_DETAIL, pet.getDetails());
        values.put(KEY_PHONE, pet.getPhone());
        values.put(KEY_IMAGEURI, pet.getPhotoURI().toString());

        int rowsAffected = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[] {String.valueOf(pet.getId())});
        db.close();
        Log.i("db",rowsAffected+ " is updated");
        return rowsAffected;
    }

    public List<Pet> getAllContacts(){
        //Create ArrayList of Pet class to hold Pet instances
        List<Pet> allPets = new ArrayList<Pet>();
        //Get writable database and runs SQL with cursor
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        if(cursor.moveToFirst()){
            //If cursor can move to the next row, it keeps creating Pet instances and adds it to ArrayList
            do{
                allPets.add(new Pet(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4))));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allPets;
    }

}
