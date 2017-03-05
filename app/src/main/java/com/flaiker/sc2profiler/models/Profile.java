package com.flaiker.sc2profiler.models;

public class Profile {
    public final String name;
    public final String rankWithinLeague;
    public final League league;
    public final Race race;

    public Profile(String name, String rankWithinLeague, League league, Race race) {
        this.name = name;
        this.rankWithinLeague = rankWithinLeague;
        this.league = league;
        this.race = race;
    }
}
