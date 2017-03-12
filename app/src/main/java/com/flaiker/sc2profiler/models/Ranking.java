package com.flaiker.sc2profiler.models;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.flaiker.sc2profiler.persistence.LadderContract.LadderEntry;

public class Ranking {
    public final int ladderId;
    public final int characterId;
    public final int realm;
    public final String displayName;
    public final String clanName;
    public final String clanTag;
    public final String profilePath;
    public final int points;
    public final int wins;
    public final int losses;
    public final Race race;

    public Ranking(int ladderId, int characterId, int realm, String displayName, String clanName,
                   String clanTag, String profilePath, int points, int wins, int losses,
                   Race race) {
        this.ladderId = ladderId;
        this.characterId = characterId;
        this.realm = realm;
        this.displayName = displayName;
        this.clanName = clanName;
        this.clanTag = clanTag;
        this.profilePath = profilePath;
        this.points = points;
        this.wins = wins;
        this.losses = losses;
        this.race = race;
    }

    public static Ranking ofCursor(@NonNull Cursor cursor) {
        return new Ranking(
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_LADDER_ID)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_CHARACTER_ID)),
                cursor.getInt(cursor.getColumnIndex(LadderEntry.COLUMN_REALM)),
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
