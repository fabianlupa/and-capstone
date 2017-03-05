package com.flaiker.sc2profiler.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.flaiker.sc2profiler.persistence.LadderContract.LadderEntry;

public class Ranking {
    public final int ladderId;
    public final int characterId;
    public final String displayName;
    public final String clanName;
    public final String clanTag;
    public final String profilePath;
    public final int points;
    public final int wins;
    public final int losses;
    public final Race race;

    public Ranking(int ladderId, int characterId, String displayName, String clanName,
                   String clanTag, String profilePath, int points, int wins, int losses, Race race) {
        this.ladderId = ladderId;
        this.characterId = characterId;
        this.displayName = displayName;
        this.clanName = clanName;
        this.clanTag = clanTag;
        this.profilePath = profilePath;
        this.points = points;
        this.wins = wins;
        this.losses = losses;
        this.race = race;
    }

    public static Ranking ofValues(@NonNull ContentValues values) {
        return new Ranking(
                values.getAsInteger(LadderEntry.COLUMN_LADDER_ID),
                values.getAsInteger(LadderEntry.COLUMN_CHARACTER_ID),
                values.getAsString(LadderEntry.COLUMN_DISPLAY_NAME),
                values.getAsString(LadderEntry.COLUMN_CLAN_NAME),
                values.getAsString(LadderEntry.COLUMN_CLAN_TAG),
                values.getAsString(LadderEntry.COLUMN_PROFILE_PATH),
                values.getAsInteger(LadderEntry.COLUMN_POINTS),
                values.getAsInteger(LadderEntry.COLUMN_WINS),
                values.getAsInteger(LadderEntry.COLUMN_LOSSES),
                Race.valueOf(values.getAsString(LadderEntry.COLUMN_RACE)));
    }

    public static Ranking ofCursor(@NonNull Cursor cursor) {
        return new Ranking(
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_LADDER_ID)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_CHARACTER_ID)),
                cursor.getString(cursor.getColumnIndex(LadderEntry.COLUMN_DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(LadderEntry.COLUMN_CLAN_NAME)),
                cursor.getString(cursor.getColumnIndex(LadderEntry.COLUMN_CLAN_TAG)),
                cursor.getString(cursor.getColumnIndex(LadderEntry.COLUMN_PROFILE_PATH)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_POINTS)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_WINS)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_LOSSES)),
                Race.valueOf(cursor.getString(cursor.getColumnIndex(LadderEntry.COLUMN_RACE))));
    }
}
