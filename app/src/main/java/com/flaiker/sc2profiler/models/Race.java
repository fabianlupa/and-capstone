package com.flaiker.sc2profiler.models;

import android.support.annotation.DrawableRes;

import com.flaiker.sc2profiler.R;

public enum Race {
    PROTOSS(R.drawable.race_protoss),
    ZERG(R.drawable.race_zerg),
    TERRAN(R.drawable.race_terran);

    public final int iconId;

    Race(@DrawableRes int iconId) {
        this.iconId = iconId;
    }
}
