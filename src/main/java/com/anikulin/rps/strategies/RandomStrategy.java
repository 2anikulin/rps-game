package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

import java.util.Random;

/**
 * Created by anikulin on 28.09.17
 */
public class RandomStrategy implements Strategy {

    private static final String ID = "random";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public RPSType getDecision() {
        return RPSType.values()[(int)(RPSType.values().length *  Math.random())];
    }

    @Override
    public void onEpisodeFinish(RPSType opponent, RPSType strategy) {

    }
}
