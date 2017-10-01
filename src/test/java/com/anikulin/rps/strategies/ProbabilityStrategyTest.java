package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.RPSTypeException;
import com.anikulin.rps.core.Strategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for Markov chain strategy.
 */
public class ProbabilityStrategyTest {

    @Test
    public void testPrediction() {

        Strategy strategy = new ProbabilityStrategy(5);

        initializeStrategy(strategy, Arrays.asList("P","S","R","P","S","R","P","S"));

        RPSType bid = strategy.getDecision();
        strategy.onEpisodeFinish(RPSType.PAPER, bid);
        assertTrue(bid == RPSType.PAPER);

        initializeStrategy(strategy, Arrays.asList("S","R","P","S"));

        bid = strategy.getDecision();
        strategy.onEpisodeFinish(RPSType.PAPER, bid);

        initializeStrategy(strategy, Arrays.asList("S","R","P","S"));
        bid = strategy.getDecision();
        assertTrue(bid == RPSType.SCISSORS);

    }

    private void initializeStrategy(final Strategy strategy, List<String> bids) {
        bids.forEach(
                b -> {
                    RPSType bid = strategy.getDecision();
                    try {
                        strategy.onEpisodeFinish(RPSType.parse(b), bid);
                    } catch (RPSTypeException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

}