package com.anikulin.rps;

import com.anikulin.rps.core.DefaultStrategyService;
import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

/**
 * Created by anikulin on 29.09.17
 */
public class ApplicationTest {

    @Test
    public void mainFlowTest() {

        Strategy testStrategy = new Strategy() {
            @Override
            public String getId() {
                return "test_strategy";
            }

            @Override
            public RPSType getDecision() {
                return RPSType.SCISSORS;
            }

            @Override
            public void onEpisodeFinish(RPSType opponent, RPSType strategy) {

            }
        };

        DefaultStrategyService service = new DefaultStrategyService();
        service.addStrategy(testStrategy);

        Application app = new Application(
                service,
                resourcePath -> Arrays.asList("R","P","S","S","P","R","R","R","P","R")
        );

        assertTrue(app.run(new String[] {"-t", "test1", "-s", testStrategy.getId()}) == 0);
        assertTrue(app.getEpisodeCounter() == 10);
        assertTrue(app.getWinCounter() == 3);
        assertTrue(app.getLoseCounter() == 5);
    }

}