package com.clock;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBAdapter  {
    
	public static final String KEY_TEXT = "TEXT";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_DATE_PAST = "DATE_PAST";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PIC = "PIC_ID";
    public static final String KEY_PIC_BACK = "PIC_BACK_ID";
    public static final String KEY_PIC_TICK = "PIC_TICK_ID";
    

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    /**
     * Database creation sql statement
     */


    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "times";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table "+ DATABASE_TABLE +"("
            + KEY_ROWID + " Integer primary key autoincrement, "
            + KEY_TEXT + " Text not null, "
            + KEY_DATE + " Integer not null, "
            + KEY_DATE_PAST + " Smallint not null, "
            + KEY_PIC + " Integrer not null, "
            + KEY_PIC_BACK + " Integrer not null, "
            + KEY_PIC_TICK + " Integrer not null "
            +");";
    
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Log.d("114",DATABASE_TABLE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            
        	Log.w("112", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
            
        }
    }
    
    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

public DBAdapter open() throws SQLException {
    mDbHelper = new DatabaseHelper(mCtx);
    mDb = mDbHelper.getWritableDatabase();
    return this;
}

public void close() {
    mDbHelper.close();
}


/**
 * Create a new note using the title and body provided. If the note is
 * successfully created return the new rowId for that note, otherwise return
 * a -1 to indicate failure.
 * 
 * @param title the title of the note
 * @param body the body of the note
 * @return rowId or -1 if failed
 */
public long createNote(String text, long date,boolean datePast, int picID, int tickID, int backID) {
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_TEXT, text);
    initialValues.put(KEY_DATE, date);
    initialValues.put(KEY_DATE_PAST, datePast);
    initialValues.put(KEY_PIC, picID);
    initialValues.put(KEY_PIC_BACK, backID);
    initialValues.put(KEY_PIC_TICK, tickID);
    

    return mDb.insert(DATABASE_TABLE, null, initialValues);
}

/**
 * Delete the note with the given rowId
 * 
 * @param rowId id of note to delete
 * @return true if deleted, false otherwise
 */
public boolean deleteNote(long rowId) {

    return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
}

/**
 * Return a Cursor over the list of all notes in the database
 * 
 * @return Cursor over all notes
 */
public Cursor fetchAllNotes() {

    return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TEXT,
            KEY_DATE, KEY_DATE_PAST, KEY_PIC, KEY_PIC_BACK, KEY_PIC_TICK}, null, null, null, null, null);
}

/**
 * Return a Cursor positioned at the note that matches the given rowId
 * 
 * @param rowId id of note to retrieve
 * @return Cursor positioned to matching note, if found
 * @throws SQLException if note could not be found/retrieved
 */
public Cursor fetchNote(long rowId) throws SQLException {

    Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
        		KEY_TEXT, KEY_DATE, KEY_DATE_PAST, KEY_PIC, KEY_PIC_BACK, KEY_PIC_TICK}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    return mCursor;

}

/**
 * Update the note using the details provided. The note to be updated is
 * specified using the rowId, and it is altered to use the title and body
 * values passed in
 * 
 * @param rowId id of note to update
 * @param title value to set note title to
 * @param body value to set note body to
 * @return true if the note was successfully updated, false otherwise
 */
public boolean updateNote(long rowId, String text, long date, boolean date_past, int picID, int tickID, int backID) {
    ContentValues args = new ContentValues();
    args.put(KEY_TEXT, text);
    args.put(KEY_DATE, date);
    if (date_past == true)
    	args.put(KEY_DATE_PAST, 1);
    else
        args.put(KEY_DATE_PAST, 0);
    
    args.put(KEY_PIC, picID);
    args.put(KEY_PIC_BACK, backID);
    args.put(KEY_PIC_TICK, tickID);
    

    return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
}
}
