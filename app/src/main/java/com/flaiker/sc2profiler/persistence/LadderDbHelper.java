package com.flaiker.sc2profiler.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flaiker.sc2profiler.persistence.LadderContract.ProfileEntry;

import static com.flaiker.sc2profiler.persistence.LadderContract.LadderEntry;

public final class LadderDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ladder.db";
    private static final int DATABASE_VERSION = 4;

    public LadderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //@formatter:off
        final String SQL_CREATE_LADDER_TABLE =
                "CREATE TABLE " + LadderEntry.TABLE_NAME + " (" +
                    LadderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LadderEntry.COLUMN_LADDER_ID + " INTEGER," +
                    LadderEntry.COLUMN_CHARACTER_ID + " INTEGER, " +
                    LadderEntry.COLUMN_DISPLAY_NAME + " TEXT NOT NULL," +
                    LadderEntry.COLUMN_CLAN_NAME + " TEXT," +
                    LadderEntry.COLUMN_CLAN_TAG + " TEXT," +
                    LadderEntry.COLUMN_PROFILE_PATH + " TEXT NOT NULL," +
                    LadderEntry.COLUMN_POINTS + " INTEGER," +
                    LadderEntry.COLUMN_WINS + " INTEGER," +
                    LadderEntry.COLUMN_LOSSES + " INTEGER," +
                    LadderEntry.COLUMN_RACE + " TEXT," +
                    " UNIQUE (" +
                        LadderEntry.COLUMN_LADDER_ID + ", " +
                        LadderEntry.COLUMN_LADDER_ID +
                    ") ON CONFLICT REPLACE" +
                ");";
        //@formatter:on

        //@formatter:off
        final String SQL_CREATE_PROFILES_TABLE =
                "CREATE TABLE " + ProfileEntry.TABLE_NAME + " (" +
                    ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProfileEntry.COLUMN_CHARACTER_ID + " INTEGER," +
                    ProfileEntry.COLUMN_DISPLAY_NAME + " TEXT NOT NULL," +
                    ProfileEntry.COLUMN_CLAN_NAME + " TEXT," +
                    ProfileEntry.COLUMN_CLAN_TAG + " TEXT," +
                    ProfileEntry.COLUMN_PROFILE_PATH + " TEXT NOT NULL," +
                    ProfileEntry.COLUMN_PORTRAIT_LINK + " TEXT NOT NULL," +
                    ProfileEntry.COLUMN_RACE + " TEXT NOT NULL," +
                    ProfileEntry.COLUMN_FAVORITE + " INTEGER," +
                    " UNIQUE (" + ProfileEntry.COLUMN_CHARACTER_ID + ")" +
                    " ON CONFLICT REPLACE" +
                ");";
        //@formatter:on

        db.execSQL(SQL_CREATE_LADDER_TABLE);
        db.execSQL(SQL_CREATE_PROFILES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LadderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME);
        onCreate(db);
    }
}
