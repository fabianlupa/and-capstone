package com.flaiker.sc2profiler.models;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.flaiker.sc2profiler.persistence.LadderContract.ProfileEntry;

public class Profile {
    public final String name;
    public final int id;
    public final int realm;
    public final int rankWithinLeague;
    public final League league;
    public final Race race;
    public final int losses;
    public final int wins;

    public Profile(String name, int id, int realm, int rankWithinLeague, League league,
                   Race race, int losses, int wins) {
        this.name = name;
        this.id = id;
        this.realm = realm;
        this.rankWithinLeague = rankWithinLeague;
        this.league = league;
        this.race = race;
        this.losses = losses;
        this.wins = wins;
    }

    public static Profile ofCursor(@NonNull Cursor cursor) {
        return new Profile(
                cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_DISPLAY_NAME)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_CHARACTER_ID)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_REALM)),
                1,
                League.GRANDMASTER,
                Race.valueOf(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_RACE))),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_WINS)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_LOSSES)));
    }

    @SuppressLint("DefaultLocale")
    public String getFormattedRankingText() {
        return String.format("%s League Rank %d", league.toString(), rankWithinLeague);
    }
}
