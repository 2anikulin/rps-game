package com.anikulin.rps;

import com.anikulin.rps.core.*;
import com.anikulin.rps.shell.UserShell;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created by anikulin on 29.09.17.
 */
public class Application {

    private static final int ERROR_CODE = 1;
    private static final int SUCCESS_CODE = 0;

    private final StrategyService strategyService;
    private final Datasource dataSource;

    private int episodeCounter = 0;
    private int winCounter = 0;
    private int loseCounter = 0;

    public Application(final StrategyService strategyService) {
        this.strategyService = strategyService;
        this.dataSource = resourcePath -> FileUtils.readLines(new File(resourcePath), Charset.defaultCharset());
    }

    public Application(final StrategyService strategyService, final Datasource dataSource) {
        this.dataSource = dataSource;
        this.strategyService = strategyService;
    }


    public int run(final String[] args) {

        Options options = new Options();

        Option lsOpt = new Option("ls", "list-strategies", false, "Show available strategies");
        lsOpt.setRequired(false);
        options.addOption(lsOpt);

        Option testFileOpt = new Option("t", "test-file", true, "Test file path");
        testFileOpt.setRequired(false);
        options.addOption(testFileOpt);

        Option strategyOpt = new Option("s", "strategy", true, "Strategy id");
        strategyOpt.setRequired(false);
        options.addOption(strategyOpt);

        Option helpOpt = new Option("h", "help", false, "Help");
        helpOpt.setRequired(false);
        options.addOption(helpOpt);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("rps-game", options);

            return ERROR_CODE;
        }

        if (cmd.hasOption(helpOpt.getLongOpt())) {

            formatter.printHelp("rps-game", options);

        } else if (cmd.hasOption(lsOpt.getLongOpt())) {

            System.out.println("---- Strategies: ----");

            this.strategyService.getStrategies().forEach(
                    s -> System.out.println(s.getId())
            );

        } else if (cmd.hasOption(strategyOpt.getLongOpt())){

            String strategyId = cmd.getOptionValue(strategyOpt.getLongOpt());

            final Strategy strategy = this.strategyService.getStrategy(strategyId);
            if (strategy == null) {
                System.out.println("Unknown strategy id: " + strategyId);
                return ERROR_CODE;
            }

            String resourcePath = cmd.getOptionValue(testFileOpt.getLongOpt());
            if (resourcePath != null) {
                try {
                    playDatasource(strategy, resourcePath);
                    printStatistic();

                } catch (IOException e) {
                    System.out.println("Wrong resource path: " + resourcePath);
                    return ERROR_CODE;
                }
            } else {
                executeShell();
            }
        }
        else {
            executeShell();
        }

        return SUCCESS_CODE;
    }

    private void executeShell() {
        new UserShell().execute();
    }

    private void playDatasource(final Strategy strategy, final String resourcePath) throws IOException {
        dataSource.getValues(resourcePath).forEach(
                (String e) -> {
                    try {

                        RPSType opponent = RPSType.parse(e);
                        RPSType decision = strategy.getDecision();
                        strategy.onEpisodeFinish(opponent, decision);

                        collectStatistics(opponent, decision);

                    } catch (RPSTypeException ex) {
                        System.out.println("Skip unknown opponent decision: " + e);
                    } catch (IOException ex) {
                        System.out.println("Can't write output file");
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        System.out.println("Unknown strategy error");
                        ex.printStackTrace();
                    }
                }
        );
    }

    private void collectStatistics(RPSType opponentBid, RPSType strategyBid) throws IOException {

        episodeCounter++;

        int res = strategyBid.isWin(opponentBid);

        if (res == 1) {
            winCounter++;
        } else if (res == -1) {
            loseCounter++;
        }

        FileUtils.writeLines(
                new File("game_episode_results.csv"),
                Collections.singletonList(
                        String.format(
                                "%s,%s,%s",
                                opponentBid.getDescription(),
                                strategyBid.getDescription(),
                                res == 0 ? "None" : res == 1 ? "Win" : "Lose"
                        )
                ),
                true
        );
    }

    private void printStatistic() {
        System.out.println("-------------Statistics----------");
        System.out.println(String.format("Game episodes count: %d", episodeCounter));
        System.out.println(String.format("Win count: %d", winCounter));

        int deadHeat = episodeCounter - loseCounter - winCounter;

        System.out.println(String.format("Win percent %s %%", winCounter * 1.0 / (episodeCounter - deadHeat) * 100.0));
        System.out.println(String.format("Lose count: %d", loseCounter));
        System.out.println(String.format("Dead heat count: %d", deadHeat));
    }

    public int getEpisodeCounter() {
        return episodeCounter;
    }

    public int getWinCounter() {
        return winCounter;
    }

    public int getLoseCounter() {
        return loseCounter;
    }


}