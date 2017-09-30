package com.anikulin.rps;

import com.anikulin.rps.core.StrategyServiceFactory;

/**
 * Entry point.
 *
 */
public class RpsMain {

    /**
     * Application main function.
     * @param args input arguments
     *
     * -t [test file path] Set file with test scenario
     * -s [strategy] Set current strategy
     * -ls Shows all available strategies
     * -h Shows help
     */
    public static void main(final String[] args ) {

        new Application(
                StrategyServiceFactory.getStrategyService()
        ).run(args);
    }
}
