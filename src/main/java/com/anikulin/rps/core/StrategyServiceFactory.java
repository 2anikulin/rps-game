package com.anikulin.rps.core;

/**
 * Created by anikulin on 28.09.17.
 */
public class StrategyServiceFactory {

    public static StrategyService getStrategyService() {
        return new CustomStrategyService();
    }
}
