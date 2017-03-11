package com.flaiker.sc2profiler.models;

import android.support.annotation.DrawableRes;

import com.flaiker.sc2profiler.R;

public enum League {
    GRANDMASTER(R.drawable.league_grandmaster),
    MASTER(R.drawable.league_master),
    DIAMOND(R.drawable.league_diamond),
    PLATINUM(R.drawable.league_platinum),
    GOLD(R.drawable.league_gold),
    SILVER(R.drawable.league_silver),
    BRONZE(R.drawable.league_bronze);

    public final int iconId;

    League(@DrawableRes int iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
