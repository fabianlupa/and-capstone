package com.flaiker.sc2profiler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaiker.sc2profiler.LadderFragment.OnListFragmentInteractionListener;
import com.flaiker.sc2profiler.dummy.DummyContent.DummyItem;

import java.util.List;

public class LadderRecyclerViewAdapter
        extends RecyclerView.Adapter<LadderRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public LadderRecyclerViewAdapter(List<DummyItem> items,
                                     OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ladder_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mRankingTextView.setText(mValues.get(position).id);
        holder.mPlayerTextView.setText(mValues.get(position).content);
        holder.mRaceImageView.setImageResource(R.drawable.race_zerg);

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPlayerTextView;
        public final TextView mRankingTextView;
        public final ImageView mRaceImageView;
        public DummyItem mItem;

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
