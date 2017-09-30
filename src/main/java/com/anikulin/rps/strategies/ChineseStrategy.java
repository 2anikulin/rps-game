package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

import java.util.Random;

/**
 * This strategy implements a chinese investigation provided in this document
 * https://arxiv.org/pdf/1404.5199v1.pdf
 *
 * Plus some modification from my side:
 * If strategy lose last N times - it change strategy vise versa.
 *
 */
public class ChineseStrategy implements Strategy {

    private static final String ID = "chinese";
    private static final int OPPONENT = 0;
    private static final int STRATEGY = 1;
    private static final int MAX_LOST_BARRIER = 10;
    private static final int DEFAULT_LOST_BARRIER = 5;

    private int LOST_BARRIER = DEFAULT_LOST_BARRIER;

    private RPSType[] lastEpisodeValues;
    private boolean inverse;
    private int lostCount;

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
        return lastEpisodeValues == null ? getRandomDecision() : getHistoryBasedDecision();
    }

    /**
     * On episode finished.
     * @param opponent Opponent bid.
     * @param strategy Strategy bid.
     */
    @Override
    public void onEpisodeFinish(final RPSType opponent, final RPSType strategy) {
        lastEpisodeValues = new RPSType[]{opponent, strategy};

        if (opponent.isWin(strategy) == 1) {
            lostCount++;
        }

        if (lostCount == LOST_BARRIER) {
            lostCount = 0;
            inverse = !inverse;
            LOST_BARRIER = new Random().nextInt(MAX_LOST_BARRIER);
        }
    }

    private RPSType getRandomDecision() {
        return RPSType.values()[(int) (RPSType.values().length *  Math.random())];
    }

    private RPSType getHistoryBasedDecision() {
        return inverse ? lastEpisodeValues[OPPONENT ].getBeat() : lastEpisodeValues[STRATEGY].getBeat();
    }
}
