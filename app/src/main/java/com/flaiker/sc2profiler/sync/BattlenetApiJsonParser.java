package com.flaiker.sc2profiler.sync;

import android.content.ContentValues;

import com.flaiker.sc2profiler.models.Race;
import com.flaiker.sc2profiler.persistence.LadderContract.LadderEntry;
import com.flaiker.sc2profiler.persistence.LadderContract.ProfileEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Static helper class to parse JSON responses received from https://dev.battle.net/io-docs
 */
public final class BattlenetApiJsonParser {
    // JSON attribute names
    private static final String LADDER_MEMBERS_ARR = "ladderMembers";
    private static final String CHARACTER_OBJ = "character";
    private static final String ID = "id";
    private static final String REALM = "realm";
    private static final String DISPLAY_NAME = "displayName";
    private static final String CLAN_NAME = "clanName";
    private static final String CLAN_TAG = "clanTag";
    private static final String PROFILE_PATH = "profilePath";
    private static final String POINTS = "points";
    private static final String WINS = "wins";
    private static final String LOSSES = "losses";
    private static final String FAVORITE_RACE = "favoriteRaceP1";
    private static final String PORTRAIT_OBJ = "portrait";
    private static final String PORTRAIT_LINK = "url";
    private static final String CAREER_OBJ = "career";
    private static final String PRIMARY_RACE = "primaryRace";
    private static final String SEASON_OBJ = "season";
    private static final String STATS_ARR = "stats";
    private static final String STATS_TYPE = "type";
    private static final String STATS_WINS = "wins";
    private static final String STATS_GAMES = "games";
    private static final String STATS_TYPE_1V1 = "1v1";

    private BattlenetApiJsonParser() {
        // Static helper class non instantiatable
    }

    public static ContentValues[] getContentValuesFromLadderJson(String json) throws JSONException {
        JSONObject ladderObject = new JSONObject(json);

        JSONArray ladderJsonArray = ladderObject.getJSONArray(LADDER_MEMBERS_ARR);
        ContentValues[] contentValues = new ContentValues[ladderJsonArray.length()];

        for (int i = 0; i < ladderJsonArray.length(); i++) {
            JSONObject memberObject = ladderJsonArray.getJSONObject(i);
            JSONObject characterObject = memberObject.getJSONObject(CHARACTER_OBJ);

            ContentValues values = new ContentValues();
            values.put(LadderEntry.COLUMN_CHARACTER_ID,
                    characterObject.getInt(ID));
            values.put(LadderEntry.COLUMN_REALM,
                    characterObject.getInt(REALM));
            values.put(LadderEntry.COLUMN_DISPLAY_NAME,
                    characterObject.getString(DISPLAY_NAME));
            values.put(LadderEntry.COLUMN_CLAN_NAME,
                    characterObject.getString(CLAN_NAME));
            values.put(LadderEntry.COLUMN_CLAN_TAG,
                    characterObject.getString(CLAN_TAG));
            values.put(LadderEntry.COLUMN_PROFILE_PATH,
                    characterObject.getString(PROFILE_PATH));
            values.put(LadderEntry.COLUMN_POINTS, memberObject.getInt(POINTS));
            values.put(LadderEntry.COLUMN_WINS, memberObject.getInt(WINS));
            values.put(LadderEntry.COLUMN_LOSSES, memberObject.getInt(LOSSES));
            values.put(LadderEntry.COLUMN_RACE,
                    memberObject.optString(FAVORITE_RACE, Race.UNKNOWN.toString()));
            contentValues[i] = values;
        }

        return contentValues;
    }

    public static ContentValues getContentValuesFromProfileJson(String json)
            throws JSONException {
        JSONObject profileObject = new JSONObject(json);
        //JSONObject portraitObject = profileObject.getJSONObject(PORTRAIT_OBJ);
        JSONObject careerObject = profileObject.getJSONObject(CAREER_OBJ);

        JSONObject seasonObject = profileObject.optJSONObject(SEASON_OBJ);
        JSONArray statsArray = null;
        if (seasonObject != null) statsArray = seasonObject.optJSONArray(STATS_ARR);

        ContentValues values = new ContentValues();

        values.put(ProfileEntry.COLUMN_CHARACTER_ID, profileObject.getInt(ID));
        values.put(ProfileEntry.COLUMN_REALM, profileObject.getInt(REALM));
        values.put(ProfileEntry.COLUMN_DISPLAY_NAME, profileObject.getString(DISPLAY_NAME));
        values.put(ProfileEntry.COLUMN_CLAN_NAME, profileObject.getString(CLAN_NAME));
        values.put(ProfileEntry.COLUMN_CLAN_TAG, profileObject.getString(CLAN_TAG));
        values.put(ProfileEntry.COLUMN_PROFILE_PATH, profileObject.getString(PROFILE_PATH));
        //values.put(ProfileEntry.COLUMN_PORTRAIT_LINK, portraitObject.getString(PORTRAIT_LINK));
        values.put(ProfileEntry.COLUMN_RACE, careerObject.getString(PRIMARY_RACE));
        if (statsArray != null) {
            for (int i = 0; i < statsArray.length(); i++) {
                JSONObject statsObject = statsArray.getJSONObject(i);
                if (statsObject.optString(STATS_TYPE).equals(STATS_TYPE_1V1)) {
                    values.put(LadderEntry.COLUMN_WINS, statsObject.getInt(STATS_WINS));
                    values.put(LadderEntry.COLUMN_LOSSES,
                            statsObject.getInt(STATS_GAMES) - statsObject.getInt(STATS_WINS));
                }
            }
        }

        return values;
    }
}
