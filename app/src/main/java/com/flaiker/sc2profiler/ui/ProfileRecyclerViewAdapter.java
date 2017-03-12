package com.flaiker.sc2profiler.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;

public class ProfileRecyclerViewAdapter extends
        RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    private Cursor mCursor;
    private ProfileFragment.OnListFragmentInteractionListener mListener;

    public ProfileRecyclerViewAdapter(ProfileFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ProfileRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_entry, parent, false);
        return new ProfileRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfileRecyclerViewAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Profile item = Profile.ofCursor(mCursor);
        holder.mItem = item;
        holder.mPlayerTextView.setText(item.name);
        holder.mRaceImageView.setImageResource(holder.mItem.race.iconId);
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mPlayerTextView;
        public final ImageView mRaceImageView;
        public Profile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPlayerTextView = (TextView) view.findViewById(R.id.profile_name);
            mRaceImageView = (ImageView) view.findViewById(R.id.profile_race_icon);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlayerTextView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.onListFragmentInteraction(mItem);
        }
    }
}
