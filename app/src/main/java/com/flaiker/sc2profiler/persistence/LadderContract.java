package com.flaiker.sc2profiler.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * SQLite column and table definitions for {@link LadderDbHelper}
 */
public final class LadderContract {
    public static final String CONTENT_AUTHORITY = "com.flaiker.sc2profiler";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths
    public static final String PATH_PROFILE = "profile";
    public static final String PATH_LADDER = "ladder";

    private LadderContract() {
    }

    public static final class LadderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_LADDER)
                .build();

        public static final String TABLE_NAME = "ladders";

        // Columns
        public static final String COLUMN_LADDER_ID = "ladder_id";
        public static final String COLUMN_CHARACTER_ID = "character_id";
        public static final String COLUMN_REALM = "realm";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_CLAN_NAME = "clan_name";
        public static final String COLUMN_CLAN_TAG = "clan_tag";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_WINS = "wins";
        public static final String COLUMN_LOSSES = "losses";
        public static final String COLUMN_RACE = "race";
    }

    public static final class ProfileEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PROFILE)
                .build();

        public static final String TABLE_NAME = "profiles";

        // Columns
        public static final String COLUMN_CHARACTER_ID = "character_id";
        public static final String COLUMN_REALM = "realm";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_CLAN_NAME = "clan_name";
        public static final String COLUMN_CLAN_TAG = "clan_tag";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_PORTRAIT_LINK = "portrait_link";
        public static final String COLUMN_RACE = "race";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_WINS = "wins";
        public static final String COLUMN_LOSSES = "losses";
        public static final String COLUMN_LEAGUE = "league";
        public static final String COLUMN_RANK = "rank";
    }
}
