package com.flaiker.sc2profiler.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;
import com.flaiker.sc2profiler.persistence.LadderContract;
import com.flaiker.sc2profiler.sync.LadderSyncTask;

public class ProfileFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_PROFILE_LOADER = 1;
    private OnListFragmentInteractionListener mListener;
    private ProfileRecyclerViewAdapter mAdapter;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(ID_PROFILE_LOADER, null, this);
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

        Button addProfileOAuthButton = (Button) view.findViewById(R.id.add_profile_oauth_btn);
        Button addProfileIdButton = (Button) view.findViewById(R.id.add_profile_id_btn);
        addProfileOAuthButton.setOnClickListener(this);
        addProfileIdButton.setOnClickListener(this);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProfileRecyclerViewAdapter(mListener);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_profile_oauth_btn:
                //addProfileWithOAuth();
                break;
            case R.id.add_profile_id_btn:
                addProfileWithId();
        }
    }

    private void addProfileWithId() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_profile, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        final EditText userIdInput = (EditText) view.findViewById(R.id.text_profile_id);
        final EditText userNameInput = (EditText) view.findViewById(R.id.text_profile_name);

        AlertDialog dialog = builder
                .setCancelable(true)
                .setPositiveButton(R.string.add_profile, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int profileId = Integer.parseInt(userIdInput.getText().toString());
                                String profileName = userNameInput.getText().toString();
                                try {
                                    LadderSyncTask.fetchNewProfile(getContext(), profileId, 1,
                                            profileName);
                                    LadderSyncTask.setProfileFavorite(getContext(), true, profileId, 1,
                                            profileName);
                                } catch (final IllegalArgumentException e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }
                            }
                        });
                        thread.start();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_PROFILE_LOADER:
                Uri profileUri = LadderContract.ProfileEntry.CONTENT_URI;
                return new CursorLoader(
                        getContext(),
                        profileUri,
                        null,
                        LadderContract.ProfileEntry.COLUMN_FAVORITE + " = ?",
                        new String[]{"1"},
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Profile profile);
    }
}
