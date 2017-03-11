package com.flaiker.sc2profiler.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;
import com.flaiker.sc2profiler.persistence.LadderContract;
import com.flaiker.sc2profiler.sync.LadderSyncTask;

public class ProfileDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_PROFILE_ID = "id";
    public static final String EXTRA_PROFILE_NAME = "name";
    public static final String EXTRA_REALM = "realm";
    private static final int ID_PROFILE_LOADER = 1;
    private static final int ID_LADDER_LOADER = 2;
    private static final int ID_HISTORY_LOADER = 3;

    private TextView mNameTextView;
    private ImageView mRaceImageView;
    private ImageView mLeagueImageView;
    private TextView mRankingTextView;
    private TextView mRaceTextView;
    private TextView mWinsTextView;
    private TextView mLossesTextView;

    private int mProfileId;
    private String mProfileName;
    private int mRealm;
    private Profile mProfile;

    public ProfileDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        mNameTextView = (TextView) view.findViewById(R.id.profile_name);
        mRaceImageView = (ImageView) view.findViewById(R.id.profile_race_icon);
        mLeagueImageView = (ImageView) view.findViewById(R.id.profile_league_icon);
        mRankingTextView = (TextView) view.findViewById(R.id.profile_league_rank_text);
        mRaceTextView = (TextView) view.findViewById(R.id.race);
        mWinsTextView = (TextView) view.findViewById(R.id.wins);
        mLossesTextView = (TextView) view.findViewById(R.id.losses);

        if (savedInstanceState == null) {
            mProfileId = getArguments().getInt(EXTRA_PROFILE_ID);
            mProfileName = getArguments().getString(EXTRA_PROFILE_NAME);
            mRealm = getArguments().getInt(EXTRA_REALM);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(ID_PROFILE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_PROFILE_LOADER:
                Uri ladderUri = LadderContract.ProfileEntry.CONTENT_URI;
                return new CursorLoader(
                        getContext(),
                        ladderUri,
                        null,
                        LadderContract.ProfileEntry.COLUMN_CHARACTER_ID + " = ? AND " +
                                LadderContract.ProfileEntry.COLUMN_REALM + " = ? AND " +
                                LadderContract.ProfileEntry.COLUMN_DISPLAY_NAME + " = ?",
                        new String[]{
                                String.valueOf(mProfileId),
                                String.valueOf(mRealm),
                                mProfileName
                        },
                        null);
            case ID_LADDER_LOADER:

                break;
            case ID_HISTORY_LOADER:

                break;
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_PROFILE_LOADER:
                if (data.getCount() != 1) {
                    // Data does not exist in the database or is too old -> fetch it
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LadderSyncTask.fetchNewProfile(getContext(), mProfileId, mRealm,
                                    mProfileName);
                            getLoaderManager().initLoader(ID_PROFILE_LOADER, null,
                                    ProfileDetailFragment.this);
                        }
                    });
                    thread.start();
                } else {
                    data.moveToFirst();
                    mProfile = Profile.ofCursor(data);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });

                break;
            case ID_LADDER_LOADER:

                break;
            case ID_HISTORY_LOADER:

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProfile = null;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUi();
            }
        });
    }

    private void updateUi() {
        if (mProfile == null) {
            mNameTextView.setText("");
            mRaceTextView.setText("");
            mLeagueImageView.setImageBitmap(null);
            mRaceImageView.setImageBitmap(null);
            mLossesTextView.setText("");
            mWinsTextView.setText("");
            mRankingTextView.setText("");
        } else {
            mNameTextView.setText(mProfile.name);
            mRaceTextView.setText(mProfile.race.toString());
            mRaceImageView.setImageResource(mProfile.race.iconId);
            mLeagueImageView.setImageResource(mProfile.league.iconId);
            mLossesTextView.setText(String.valueOf(mProfile.losses));
            mWinsTextView.setText(String.valueOf(mProfile.wins));
            mRankingTextView.setText(mProfile.getFormattedRankingText());
        }
    }
}
