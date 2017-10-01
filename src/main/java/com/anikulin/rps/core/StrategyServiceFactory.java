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
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final StrategyService INSTANCE = new DefaultStrategyService();
    }
}
