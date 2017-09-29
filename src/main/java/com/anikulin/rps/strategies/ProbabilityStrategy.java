package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

/**
 * Created by anikulin on 28.09.17.
 */
public class ProbabilityStrategy implements Strategy {

    private static final String ID = "probability"; //multi-arm bandit

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public RPSType getDecision() {
        return null;
    }

    @Override
    public void onEpisodeFinish(RPSType opponent, RPSType strategy) {

    }
}
