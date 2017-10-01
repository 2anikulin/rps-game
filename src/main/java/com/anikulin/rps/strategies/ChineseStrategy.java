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

    private RPSType[] lastEpisodeBids;
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
        return lastEpisodeBids == null ? getRandomDecision() : getHistoryBasedDecision();
    }

    /**
     * On episode finished.
     * @param opponent Opponent bid.
     * @param strategy Strategy bid.
     */
    @Override
    public void onEpisodeFinish(final RPSType opponent, final RPSType strategy) {
        lastEpisodeBids = new RPSType[]{opponent, strategy};

        int winCode = strategy.isWin(opponent);
        if (winCode == -1) {
            lostCount++;
        } else if (winCode == 1) {
            lostCount = 0;
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
        int winCode = lastEpisodeBids[STRATEGY].isWin(lastEpisodeBids[OPPONENT]);
        RPSType decision;

        if (winCode == 1) { //if win -> set last opponent bid
            decision = lastEpisodeBids[OPPONENT];
        } else  { //if lose -> set bid which beat last opponent bid
            decision = RPSType.stream().filter(
                    t -> t.getBeat() == (inverse ? lastEpisodeBids[OPPONENT].getBeat() : lastEpisodeBids[OPPONENT])
            ).findAny().get();
        }

        return decision;
    }
}
