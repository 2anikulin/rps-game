package com.anikulin.rps.core;

import java.util.List;

/**
 * Strategy Service Interface.
 * Contract for strategies management.
 */
public interface StrategyService {

    /**
     * Get strategy by id
     * @param id Strategy id.
     * @return Strategy or null if doesn't exists.
     */
    Strategy getStrategy(String id);

    /**
     * Get list of strategies.
     * @return Strategies list.
     */
    List<Strategy> getStrategies();

}
