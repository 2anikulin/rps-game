package com.anikulin.rps.core;

/**
 * Created by anikulin on 28.09.17.
 */
public interface Strategy {

    String getId();

    RPSType getDecision();

    void onEpisodeFinish(RPSType opponent, RPSType strategy);
}
