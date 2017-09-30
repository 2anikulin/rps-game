package com.anikulin.rps.core;

/**
 * Strategy Service Factory.
 */
public class StrategyServiceFactory {

    /**
     * Create a new strategy service.
     * @return Service instance.
     */
    public static StrategyService getStrategyService() {
        return new DefaultStrategyService();
    }
}
