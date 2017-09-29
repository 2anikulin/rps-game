package com.anikulin.rps.core;

import java.util.List;

/**
 * Created by anikulin on 28.09.17.
 */
public interface StrategyService {

    Strategy getStrategy(String id);

    List<Strategy> getStrategies();

}
