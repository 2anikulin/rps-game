package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

/**
 * Created by anikulin on 28.09.17.
 *
 */
public class ProbabilityStrategy implements Strategy {

    private static final String ID = "probability"; //multi-arm bandit

    /**
     * Get strategy id.
     * @return id
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * Get decision.
     * @return decision.
     */
    @Override
    public RPSType getDecision() {
        return null;
    }

    /**
     * On episode finished.
     * @param opponent Opponent bid.
     * @param strategy Strategy bid.
     */
    @Override
    public void onEpisodeFinish(final RPSType opponent, final RPSType strategy) {

    }
}
