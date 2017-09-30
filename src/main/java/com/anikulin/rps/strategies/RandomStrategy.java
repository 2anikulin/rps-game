package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

/**
 * This is simple random strategy.
 * Nothing special.
 */
public class RandomStrategy implements Strategy {

    private static final String ID = "random";

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
        return RPSType.values()[(int) (RPSType.values().length *  Math.random())];
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
