package de.htw_berlin.gma.feedyourkitty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DemoSQLiteOpenHelper extends SQLiteOpenHelper
{
    private static final String TAG                 = DemoSQLiteOpenHelper.class.getSimpleName();

    private static final String DATABASE_NAME       = "highscores.db";
    private static final int    DATABASE_VERSION    = 1;

    private static final String FIRSTTABLE_NAME     = "highscores";
    private static final String COLUMN_ID           = "id";
    private static final String COLUMN_PLAYERNAME   = "playername";
    private static final String COLUMN_SCORE        = "score";

    //SQL
    private static final String CREATE_FIRSTTABLE   = "CREATE TABLE " + FIRSTTABLE_NAME +
                                                      " (" + COLUMN_ID + " INTEGER PRIMARY_KEY," +
                                                      COLUMN_PLAYERNAME + " VARCHAR(32)," +
                                                      COLUMN_SCORE + " INTEGER" + ")";

    private static final String DROP_FIRSTTABLE     =  "DROP TABLE IF EXISTS " + FIRSTTABLE_NAME;

    public DemoSQLiteOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_FIRSTTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "Upgrade from version: " + oldVersion + " to " + newVersion);
        clearTable();
    }

    public void insert(String playerName, int score)
    {
        long row = -1;

        try
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYERNAME, playerName);
            values.put(COLUMN_SCORE, score);
            row = db.insert(FIRSTTABLE_NAME, null, values);
        }
        catch(SQLiteException e)
        {
            Log.e(TAG, "insert error 1",e);
        }
        catch(Exception e)
        {
            Log.e(TAG, "insert error 2",e);
        }
        finally
        {
            Log.d(TAG, "inserted: " + row);
        }
    }

    public String query()
    {
        SQLiteDatabase db = getWritableDatabase();
        String q = "SELECT * FROM " + FIRSTTABLE_NAME + " ORDER BY SCORE DESC";
        Cursor mCursor = db.rawQuery(q,null);
        StringBuilder sb = new StringBuilder();
        String id = null;
        String name = null;
        String score = null;
        if (mCursor.moveToFirst())
        {
            do
            {
                name    = mCursor.getString(mCursor.getColumnIndex(COLUMN_PLAYERNAME));
                score   = mCursor.getString(mCursor.getColumnIndex(COLUMN_SCORE));
                if(score.length() == 1)
                    score = "0" + score;    //make the number display 2 digits
                sb.append("" + score + " - " + name + "\n" );
            }
            while(mCursor.moveToNext());
        }
        mCursor.close();
        return sb.toString();
    }

    public ArrayList<String> queryList()
    {
        ArrayList<String> returnList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String q = "SELECT * FROM " + FIRSTTABLE_NAME + " ORDER BY SCORE DESC";
        Cursor mCursor = db.rawQuery(q,null);
        StringBuilder sb = new StringBuilder();
        String id = null;
        String name = null;
        String score = null;
        if(mCursor.moveToFirst())
        {
            do
            {
                name    = mCursor.getString(mCursor.getColumnIndex(COLUMN_PLAYERNAME));
                score   = mCursor.getString(mCursor.getColumnIndex(COLUMN_SCORE));
                if(score.length() == 1)
                    score = "0" + score;    //make the number display 2 digits
                returnList.add("" + score + " - " + name);
            }
            while(mCursor.moveToNext());
        }
        mCursor.close();
        return returnList;
    }

    public void clearTable()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DROP_FIRSTTABLE);
        db.execSQL(CREATE_FIRSTTABLE);
    }
}