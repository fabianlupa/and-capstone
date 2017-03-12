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
    private static final class Ladder {
        static final String LADDER_MEMBERS_ARR = "ladderMembers";
        static final String CHARACTER_OBJ = "character";
        static final String ID = "id";
        static final String REALM = "realm";
        static final String DISPLAY_NAME = "displayName";
        static final String CLAN_NAME = "clanName";
        static final String CLAN_TAG = "clanTag";
        static final String PROFILE_PATH = "profilePath";
        static final String POINTS = "points";
        static final String WINS = "wins";
        static final String LOSSES = "losses";
        static final String FAVORITE_RACE = "favoriteRaceP1";

        private Ladder() {
        }
    }

    private static final class Profile {
        static final String PORTRAIT_OBJ = "portrait";
        static final String PORTRAIT_LINK = "url";
        static final String PORTRAIT_X = "x";
        static final String PORTRAIT_Y = "y";
        static final String PORTRAIT_W = "w";
        static final String PORTRAIT_H = "h";
        static final String PORTRAIT_OFFSET = "offset";
        static final String ID = "id";
        static final String REALM = "realm";
        static final String CLAN_NAME = "clanName";
        static final String CLAN_TAG = "clanTag";
        static final String PROFILE_PATH = "profilePath";
        static final String DISPLAY_NAME = "displayName";
        static final String CAREER_OBJ = "career";
        static final String PRIMARY_RACE = "primaryRace";

        private Profile() {
        }
    }

    private static final class ProfileLadders {
        static final String CURRENT_SEASON_ARR = "currentSeason";
        static final String LADDER_ARR = "ladder";
        static final String RANK = "rank";
        static final String LEAGUE = "league";
        static final String MATCH_MAKING_QUEUE = "matchMakingQueue";
        static final String QUEUE_LOTV_SOLO = "LOTV_SOLO";
        static final String WINS = "wins";
        static final String LOSSES = "losses";

        private ProfileLadders() {
        }
    }


    private BattlenetApiJsonParser() {
        // Static helper class non instantiatable
    }

    /**
     * Parse a {@link LadderSyncTask#ENDPOINT_LADDER} response
     *
     * @param json Response in JSON
     * @return Parsed ContentValues
     * @throws JSONException Parser error
     */
    public static ContentValues[] getContentValuesFromLadderJson(String json) throws JSONException {
        JSONObject ladderObject = new JSONObject(json);

        JSONArray ladderJsonArray = ladderObject.getJSONArray(Ladder.LADDER_MEMBERS_ARR);
        ContentValues[] contentValues = new ContentValues[ladderJsonArray.length()];

        for (int i = 0; i < ladderJsonArray.length(); i++) {
            JSONObject memberObject = ladderJsonArray.getJSONObject(i);
            JSONObject characterObject = memberObject.getJSONObject(Ladder.CHARACTER_OBJ);

            ContentValues values = new ContentValues();
            values.put(LadderEntry.COLUMN_CHARACTER_ID,
                    characterObject.getInt(Ladder.ID));
            values.put(LadderEntry.COLUMN_REALM,
                    characterObject.getInt(Ladder.REALM));
            values.put(LadderEntry.COLUMN_DISPLAY_NAME,
                    characterObject.getString(Ladder.DISPLAY_NAME));
            values.put(LadderEntry.COLUMN_CLAN_NAME,
                    characterObject.getString(Ladder.CLAN_NAME));
            values.put(LadderEntry.COLUMN_CLAN_TAG,
                    characterObject.getString(Ladder.CLAN_TAG));
            values.put(LadderEntry.COLUMN_PROFILE_PATH,
                    characterObject.getString(Ladder.PROFILE_PATH));
            values.put(LadderEntry.COLUMN_POINTS, memberObject.getInt(Ladder.POINTS));
            values.put(LadderEntry.COLUMN_WINS, memberObject.getInt(Ladder.WINS));
            values.put(LadderEntry.COLUMN_LOSSES, memberObject.getInt(Ladder.LOSSES));
            values.put(LadderEntry.COLUMN_RACE,
                    memberObject.optString(Ladder.FAVORITE_RACE, Race.UNKNOWN.toString()));
            contentValues[i] = values;
        }

        return contentValues;
    }

    /**
     * Parse a {@link LadderSyncTask#ENDPOINT_PROFILE} and
     * {@link LadderSyncTask#ENDPOINT_PROFILE_LADDERS} response
     *
     * @param jsonProfile Profile response in JSON
     * @param jsonLadders Ladders response in JSON
     * @return Parsed ContentValues
     * @throws JSONException Parser error
     */
    public static ContentValues getContentValuesFromProfileJson(String jsonProfile,
                                                                String jsonLadders)
            throws JSONException {
        JSONObject profileObject = new JSONObject(jsonProfile);
        JSONObject portraitObject = profileObject.optJSONObject(Profile.PORTRAIT_OBJ);
        JSONObject careerObject = profileObject.getJSONObject(Profile.CAREER_OBJ);

        ContentValues values = new ContentValues();

        values.put(ProfileEntry.COLUMN_CHARACTER_ID, profileObject.getInt(Profile.ID));
        values.put(ProfileEntry.COLUMN_REALM, profileObject.getInt(Profile.REALM));
        values.put(ProfileEntry.COLUMN_DISPLAY_NAME, profileObject.getString(Profile.DISPLAY_NAME));
        values.put(ProfileEntry.COLUMN_CLAN_NAME, profileObject.getString(Profile.CLAN_NAME));
        values.put(ProfileEntry.COLUMN_CLAN_TAG, profileObject.getString(Profile.CLAN_TAG));
        values.put(ProfileEntry.COLUMN_PROFILE_PATH, profileObject.getString(Profile.PROFILE_PATH));
        if (portraitObject != null) {
            values.put(ProfileEntry.COLUMN_PORTRAIT_LINK,
                    portraitObject.getString(Profile.PORTRAIT_LINK));
            values.put(ProfileEntry.COLUMN_PORTRAIT_X, portraitObject.getInt(Profile.PORTRAIT_X));
            values.put(ProfileEntry.COLUMN_PORTRAIT_Y, portraitObject.getInt(Profile.PORTRAIT_Y));
            values.put(ProfileEntry.COLUMN_PORTRAIT_W, portraitObject.getInt(Profile.PORTRAIT_W));
            values.put(ProfileEntry.COLUMN_PORTRAIT_H, portraitObject.getInt(Profile.PORTRAIT_H));
            values.put(ProfileEntry.COLUMN_PORTRAIT_OFFSET,
                    portraitObject.getInt(Profile.PORTRAIT_OFFSET));
        }
        values.put(ProfileEntry.COLUMN_RACE, careerObject.getString(Profile.PRIMARY_RACE));


        JSONObject laddersJson = new JSONObject(jsonLadders);
        JSONArray seasonArray = laddersJson.getJSONArray(ProfileLadders.CURRENT_SEASON_ARR);
        JSONObject foundRankedLadder = null;
        for (int i = 0; i < seasonArray.length(); i++) {
            JSONObject seasonEntry = seasonArray.getJSONObject(i);
            JSONArray seasonEntryLadderArray = seasonEntry.getJSONArray(ProfileLadders.LADDER_ARR);
            for (int ii = 0; ii < seasonEntryLadderArray.length(); ii++) {
                JSONObject seasonEntryLadderEntry = seasonEntryLadderArray.getJSONObject(ii);
                if (seasonEntryLadderEntry.getString(ProfileLadders.MATCH_MAKING_QUEUE)
                        .equals(ProfileLadders.QUEUE_LOTV_SOLO)) {
                    foundRankedLadder = seasonEntryLadderEntry;
                    break;
                }
            }
            if (foundRankedLadder != null) break;
        }

        if (foundRankedLadder != null) {
            values.put(ProfileEntry.COLUMN_WINS, foundRankedLadder.getInt(ProfileLadders.WINS));
            values.put(ProfileEntry.COLUMN_LOSSES, foundRankedLadder.getInt(ProfileLadders.LOSSES));
            values.put(ProfileEntry.COLUMN_LEAGUE,
                    foundRankedLadder.getString(ProfileLadders.LEAGUE));
            values.put(ProfileEntry.COLUMN_RANK, foundRankedLadder.getInt(ProfileLadders.RANK));
        }

        return values;
    }
}
