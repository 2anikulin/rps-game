package com.anikulin.rps.core;

import com.anikulin.rps.strategies.ChineseStrategy;
import com.anikulin.rps.strategies.ProbabilityStrategy;
import com.anikulin.rps.strategies.RandomStrategy;
import java.util.*;


/**
 * Created by anikulin on 28.09.17.
 */
public class DefaultStrategyService implements StrategyService {

    private final Map<String, Strategy> strategies = new HashMap<>();


    public DefaultStrategyService() {

        addStrategy(new ChineseStrategy());

        addStrategy(new RandomStrategy());

        addStrategy(new ProbabilityStrategy());
    }


    public void addStrategy(final Strategy strategy) {
        strategies.put(strategy.getId(), strategy);
    }

    @Override
    public Strategy getStrategy(String id) {
        return strategies.get(id);
    }

    @Override
    public List<Strategy> getStrategies() {
        return new ArrayList<>(strategies.values());
    }
}
