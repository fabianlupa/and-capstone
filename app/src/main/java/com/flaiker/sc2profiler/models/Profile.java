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
    public final Portrait portrait;
    public final boolean favorite;

    public Profile(String name, int id, int realm, int rankWithinLeague, League league,
                   Race race, int losses, int wins, String url, int x, int y, int w, int h,
                   int offset, boolean favorite) {
        this.name = name;
        this.id = id;
        this.realm = realm;
        this.rankWithinLeague = rankWithinLeague;
        this.league = league;
        this.race = race;
        this.losses = losses;
        this.wins = wins;
        this.favorite = favorite;
        this.portrait = new Portrait(url, x, y, w, h, offset);
    }

    public static Profile ofCursor(@NonNull Cursor cursor) {
        return new Profile(
                cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_DISPLAY_NAME)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_CHARACTER_ID)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_REALM)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_RANK)),
                League.valueOf(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_LEAGUE))),
                Race.valueOf(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_RACE))),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_LOSSES)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_WINS)),
                cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_LINK)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_X)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_Y)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_W)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_H)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_PORTRAIT_OFFSET)),
                cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_FAVORITE)) == 1);
    }

    @SuppressLint("DefaultLocale")
    public String getFormattedRankingText() {
        return String.format("%s League Rank %d", league.toString(), rankWithinLeague);
    }

    public static class Portrait {
        public final String url;
        public final int x;
        public final int y;
        public final int w;
        public final int h;
        public final int offset;

        public Portrait(String url, int x, int y, int w, int h, int offset) {
            this.url = url;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.offset = offset;
        }
    }
}
