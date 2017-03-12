package com.flaiker.sc2profiler.models;

import android.support.annotation.DrawableRes;

import com.flaiker.sc2profiler.R;

/**
 * Enumeration for all races in StarCraft II, including {@link #RANDOM} and {@link #UNKNOWN}
 */
public enum Race {
    PROTOSS(R.drawable.race_protoss),
    ZERG(R.drawable.race_zerg),
    TERRAN(R.drawable.race_terran),
    RANDOM(R.drawable.race_random),
    UNKNOWN(R.drawable.race_unknown);

    public final int iconId;

    Race(@DrawableRes int iconId) {
        this.iconId = iconId;
    }
}
