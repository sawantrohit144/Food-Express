package com.yummy.foodonmytrain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FoodUserDB";

    private ContentValues values;
    private Cursor cursor;
    private SQLiteDatabase sqLiteDb;
    private Context context;

    private static final String SQL_MEMBER_CREATE_ENTRIES =
            "CREATE TABLE " + TableEntries.MemberEntry.TABLE_NAME        + " (" +
                   // TableEntries.MemberEntry.MEMBER_ID                   + " TEXT PRIMARY KEY," +
                    TableEntries.MemberEntry.MEMBER_FIRST_NAME           + " TEXT," +
                    TableEntries.MemberEntry.MEMBER_MIDDLE_NAME          + " TEXT," +
                    TableEntries.MemberEntry.MEMBER_LAST_NAME            + " TEXT," +
                    TableEntries.MemberEntry.MEMBER_USER_TYPE            + " TEXT," +
                    TableEntries.MemberEntry.MEMBER_PHONE_NUMBER         + " TEXT PRIMARY KEY," +
                    TableEntries.MemberEntry.MEMBER_DOB                  + " TEXT," +
                    TableEntries.MemberEntry.MEMBER_TRAIN_NO             + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_MEMBER_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_MEMBER_CREATE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void deleteDB(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }


    // MEMBER-----------------------------------------------------------------------------------

    public void insertMember(Personal_details personal_details) {
        sqLiteDb = this.getWritableDatabase();
        values = new ContentValues();

        //values.put(TableEntries.MemberEntry.MEMBER_ID, personal_details.getId());
        values.put(TableEntries.MemberEntry.MEMBER_FIRST_NAME, personal_details.getFirstname());
        values.put(TableEntries.MemberEntry.MEMBER_MIDDLE_NAME, personal_details.getMiddlename());
        values.put(TableEntries.MemberEntry.MEMBER_LAST_NAME, personal_details.getLastname());
        values.put(TableEntries.MemberEntry.MEMBER_USER_TYPE, personal_details.getUserType());
        values.put(TableEntries.MemberEntry.MEMBER_PHONE_NUMBER, personal_details.getPersonalno());
        values.put(TableEntries.MemberEntry.MEMBER_DOB, personal_details.getPersonalDOB());
        values.put(TableEntries.MemberEntry.MEMBER_TRAIN_NO, personal_details.getTrainNo());

        sqLiteDb.insert(TableEntries.MemberEntry.TABLE_NAME, null, values);
    }

    public String updateMember(Personal_details personal_details) {

        String memberId = personal_details.getId();
        sqLiteDb = this.getWritableDatabase();
        values = new ContentValues();

        //values.put(TableEntries.MemberEntry.MEMBER_ID, memberId);
        values.put(TableEntries.MemberEntry.MEMBER_FIRST_NAME, personal_details.getFirstname());
        values.put(TableEntries.MemberEntry.MEMBER_MIDDLE_NAME, personal_details.getMiddlename());
        values.put(TableEntries.MemberEntry.MEMBER_LAST_NAME, personal_details.getLastname());
        values.put(TableEntries.MemberEntry.MEMBER_USER_TYPE, personal_details.getUserType());
        values.put(TableEntries.MemberEntry.MEMBER_DOB, personal_details.getPersonalDOB());
        values.put(TableEntries.MemberEntry.MEMBER_TRAIN_NO, personal_details.getTrainNo());

        String selection = TableEntries.MemberEntry.MEMBER_PHONE_NUMBER + " LIKE ?";
        String[] selectionArgs = {String.valueOf(personal_details.getPersonalno())};

        sqLiteDb.update(
                TableEntries.MemberEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return memberId;
    }

    public Personal_details getMemberDetails() {

        SQLiteDatabase db = this.getReadableDatabase();
        Personal_details personal_details=new Personal_details();

        Cursor cursor = db.query(
                TableEntries.MemberEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            //personal_details.setId(cursor.getString(0));
            personal_details.setFirstname(cursor.getString(0));
            personal_details.setMiddlename(cursor.getString(1));
            personal_details.setLastname(cursor.getString(2));
            personal_details.setUserType(cursor.getString(3));
            personal_details.setPersonalno(cursor.getString(4));
            personal_details.setPersonalDOB(cursor.getString(5));
            personal_details.setTrainNo(cursor.getString(6));


            cursor.close();
            return personal_details;

        }
        return null;
    }
}
