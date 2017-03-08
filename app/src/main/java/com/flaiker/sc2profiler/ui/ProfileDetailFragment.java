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
import com.flaiker.sc2profiler.models.Race;
import com.flaiker.sc2profiler.persistence.LadderContract;

public class ProfileDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_PATH = "path";
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO: Remove sample data
        mNameTextView.setText("FlaSh");
        mRaceImageView.setImageResource(R.drawable.race_terran);
        mLeagueImageView.setImageResource(R.drawable.league_grandmaster);
        mRankingTextView.setText("Grandmaster League Rank 1");
        mRaceTextView.setText(Race.PROTOSS.toString());
        mWinsTextView.setText(String.valueOf(100));
        mLossesTextView.setText(String.valueOf(50));
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
                        null,
                        null,
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

                break;
            case ID_LADDER_LOADER:

                break;
            case ID_HISTORY_LOADER:

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
