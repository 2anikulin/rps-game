package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

import java.util.Random;

/**
 * Created by anikulin on 28.09.17
 *
 * https://arxiv.org/pdf/1404.5199v1.pdf
 */
public class ChineseStrategy implements Strategy {

    private static final String ID = "chinese";
    private static final int OPPONENT = 0;
    private static final int STRATEGY = 1;

    private int LOST_BARIER = 5;

    private RPSType[] lastEpisodeValues;
    private boolean inverse = false;
    private int lostCount;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public RPSType getDecision() {
        return lastEpisodeValues == null ? getRandomDecision() : getHistoryBasedDecision();
    }

    @Override
    public void onEpisodeFinish(RPSType opponent, RPSType strategy) {
        lastEpisodeValues = new RPSType[]{opponent, strategy};

        if (opponent.isWin(strategy) == 1) {
            lostCount++;
        }

        if (lostCount == LOST_BARIER) {
            lostCount = 0;
            inverse = !inverse;
            LOST_BARIER = new Random().nextInt(10);
        }
    }

    private RPSType getRandomDecision() {
        return RPSType.values()[(int)(RPSType.values().length *  Math.random())];
    }

    private RPSType getHistoryBasedDecision() {
        return inverse ? lastEpisodeValues[OPPONENT ].getBeat() : lastEpisodeValues[STRATEGY].getBeat();
    }
}
