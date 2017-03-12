package com.flaiker.sc2profiler.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ContentProvider for the ladder database
 */
public class LadderContentProvider extends ContentProvider {
    private static final int CODE_LADDER = 100;
    private static final int CODE_PROFILE = 200;
    private static final int CODE_PROFILE_BY_ID = 201;
    private static final UriMatcher sUriMatcher;
    private LadderDbHelper mDbHelper;


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(LadderContract.CONTENT_AUTHORITY, LadderContract.PATH_LADDER,
                CODE_LADDER);
        sUriMatcher.addURI(LadderContract.CONTENT_AUTHORITY, LadderContract.PATH_PROFILE,
                CODE_PROFILE);
        sUriMatcher.addURI(LadderContract.CONTENT_AUTHORITY, LadderContract.PATH_PROFILE + "/#",
                CODE_PROFILE_BY_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new LadderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_LADDER:
                cursor = mDbHelper.getReadableDatabase().query(
                        LadderContract.LadderEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PROFILE:
                cursor = mDbHelper.getReadableDatabase().query(
                        LadderContract.ProfileEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PROFILE_BY_ID:
                String profileId = uri.getLastPathSegment();
                selectionArgs = new String[]{profileId};
                cursor = mDbHelper.getReadableDatabase().query(
                        LadderContract.ProfileEntry.TABLE_NAME,
                        projection,
                        LadderContract.ProfileEntry.COLUMN_CHARACTER_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("getType is not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri resultUri = null;

        switch (sUriMatcher.match(uri)) {
            case CODE_LADDER:
                mDbHelper.getWritableDatabase().insert(
                        LadderContract.LadderEntry.TABLE_NAME,
                        null,
                        values);
                break;
            case CODE_PROFILE:
                mDbHelper.getWritableDatabase().insert(
                        LadderContract.ProfileEntry.TABLE_NAME,
                        null,
                        values);
                resultUri = LadderContract.ProfileEntry.CONTENT_URI.buildUpon()
                        .appendPath(values.getAsString(LadderContract.LadderEntry
                                .COLUMN_CHARACTER_ID))
                        .build();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        if (selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_LADDER:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        LadderContract.LadderEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_PROFILE:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        LadderContract.ProfileEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }

        if (numRowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numRowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case CODE_PROFILE:
                numRowsUpdated = mDbHelper.getWritableDatabase().update(
                        LadderContract.ProfileEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Update not implemented for uri: " + uri);
        }

        if (numRowsUpdated != 0) getContext().getContentResolver().notifyChange(uri, null);
        return numRowsUpdated;
    }
}
