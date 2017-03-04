package com.flaiker.sc2profiler;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView mNameTextView;
    private ImageView mRaceImageView;
    private ImageView mLeagueImageView;
    private TextView mRankingTextView;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mNameTextView = (TextView) view.findViewById(R.id.profile_name);
        mRaceImageView = (ImageView) view.findViewById(R.id.profile_race_icon);
        mLeagueImageView = (ImageView) view.findViewById(R.id.profile_league_icon);
        mRankingTextView = (TextView) view.findViewById(R.id.profile_league_rank_text);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Remove sample data
        mNameTextView.setText("FlaSh");
        mRaceImageView.setImageResource(R.drawable.race_terran);
        mLeagueImageView.setImageResource(R.drawable.league_grandmaster);
        mRankingTextView.setText("Grandmaster League Rank 1");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
