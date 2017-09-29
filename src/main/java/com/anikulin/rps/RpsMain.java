package com.anikulin.rps;

import com.anikulin.rps.core.StrategyServiceFactory;

/**
 * Hello world!
 *
 */
public class RpsMain {

    public static void main( String[] args ) {

        new Application(
                StrategyServiceFactory.getStrategyService()
        ).run(args);

    }
}
