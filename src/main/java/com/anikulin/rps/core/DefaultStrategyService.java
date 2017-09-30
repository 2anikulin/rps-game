package com.anikulin.rps.core;

import com.anikulin.rps.strategies.ChineseStrategy;
import com.anikulin.rps.strategies.ProbabilityStrategy;
import com.anikulin.rps.strategies.RandomStrategy;
import java.util.*;


/**
 * Default implementation of StrategyService.
 * It provides an access to internal strategies.
 */
public class DefaultStrategyService implements StrategyService {

    private final Map<String, Strategy> strategies = new HashMap<>();

    /**
     * Constructor.
     */
    public DefaultStrategyService() {

        addStrategy(new ChineseStrategy());

        addStrategy(new RandomStrategy());

        addStrategy(new ProbabilityStrategy());
    }


    /**
     * Adds new strategy.
     * @param strategy Strategy.
     */
    public void addStrategy(final Strategy strategy) {
        strategies.put(strategy.getId(), strategy);
    }

    /**
     * Get strategy by id.
     * @param id Strategy id.
     * @return Strategy or null if doesn't exists.
     */
    @Override
    public Strategy getStrategy(String id) {
        return strategies.get(id);
    }

    /**
     * Get list of strategies.
     * @return Strategies list.
     */
    @Override
    public List<Strategy> getStrategies() {
        return new ArrayList<>(strategies.values());
    }
}
