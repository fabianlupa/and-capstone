package com.flaiker.sc2profiler.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Ranking;
import com.flaiker.sc2profiler.ui.LadderFragment.OnListFragmentInteractionListener;

public class LadderRecyclerViewAdapter
        extends RecyclerView.Adapter<LadderRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private Cursor mCursor;

    public LadderRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ladder_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Ranking item = Ranking.ofCursor(mCursor);
        holder.mItem = item;
        holder.mRankingTextView.setText(String.valueOf(position + 1)); // TODO: Check order
        holder.mPlayerTextView.setText((!item.clanTag.equals("") ? "[" + item.clanTag + "] " : "") +
                item.displayName);
        holder.mRaceImageView.setImageResource(holder.mItem.race.iconId);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPlayerTextView;
        public final TextView mRankingTextView;
        public final ImageView mRaceImageView;
        public Ranking mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPlayerTextView = (TextView) view.findViewById(R.id.ladder_player);
            mRankingTextView = (TextView) view.findViewById(R.id.ladder_rank);
            mRaceImageView = (ImageView) view.findViewById(R.id.ladder_race_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlayerTextView.getText() + "'";
        }
    }
}
