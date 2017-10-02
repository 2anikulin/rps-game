package com.anikulin.rps;

import com.anikulin.rps.core.*;
import com.anikulin.rps.shell.UserShell;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * Application Root class.
 *
 * Implements processing of input arguments.
 * Runs strategies on the test datasets and collect statistics.
 * Executes user shell.
 */
public class Application {

    private static final int ERROR_CODE = 1;
    private static final int SUCCESS_CODE = 0;

    private final StrategyService strategyService;
    private final DataSource dataSource;

    private int episodeCounter;
    private int winCounter;
    private int loseCounter;
    private String gameOutputLog;

    /**
     * Constructor.
     * @param strategyService Strategy service instance
     */
    public Application(final StrategyService strategyService) {
        this.strategyService = strategyService;
        this.dataSource = resourcePath -> FileUtils.readLines(new File(resourcePath), Charset.defaultCharset());
    }

    /**
     * Constructor.
     * @param strategyService Strategy service instance
     * @param dataSource DataSource instance. Provide an access to the test datasets
     */
    public Application(final StrategyService strategyService, final DataSource dataSource) {
        this.dataSource = dataSource;
        this.strategyService = strategyService;
    }

    /**
     * Input argument processor.
     *
     * @param args Command line arguments
     * @return Error code
     */
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
                    printStatistics();

                } catch (IOException e) {
                    System.out.println("Wrong resource path: " + resourcePath);
                    return ERROR_CODE;
                }
            } else {
                executeShell();
            }
        } else {
            executeShell();
        }

        return SUCCESS_CODE;
    }

    private void executeShell() {
        new UserShell(this).execute();
    }

    private void playDatasource(final Strategy strategy, final String resourcePath) throws IOException {
        clearStatistics();
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
                        //CHECKSTYLE.OFF: We have to catch all strategy exceptions
                    } catch (Exception ex) {
                        //CHECKSTYLE.ON:
                        System.out.println("Unknown strategy error");
                        ex.printStackTrace();
                    }
                }
        );
    }

    /**
     * Collect game statistics.
     *
     * @param opponentBid Bid of opponent.
     * @param strategyBid Bid of strategy.
     * @throws IOException .
     */
    public void collectStatistics(final RPSType opponentBid, final RPSType strategyBid) throws IOException {

        episodeCounter++;

        int res = strategyBid.isWin(opponentBid);

        if (res == 1) {
            winCounter++;
        } else if (res == -1) {
            loseCounter++;
        }

        if (gameOutputLog == null) {
            gameOutputLog = String.format(
                    "game_log_%s.csv", new SimpleDateFormat("yyyy-MM-dd-HH.mm.SS").format(new Date())
            );
        }

        FileUtils.writeLines(
                new File(gameOutputLog),
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

    /**
     * Print to console statistics of current game.
     */
    public void printStatistics() {
        System.out.println("-------------Statistics----------");
        System.out.println(String.format("Game episodes count: %d", episodeCounter));
        System.out.println(String.format("Win count: %d", winCounter));
        System.out.println(String.format("Lose count: %d", loseCounter));
        int deadHeat = episodeCounter - loseCounter - winCounter;
        System.out.println(String.format("Dead heat count: %d", deadHeat));

        //CHECKSTYLE.OFF: Magic number 100 is obvious
        System.out.println(String.format("Strategy Win percent %s %%", winCounter * 1.0 / (episodeCounter) * 100.0));
        System.out.println(String.format("Strategy lose percent %s %%", loseCounter * 1.0 / (episodeCounter) * 100.0));
        System.out.println(String.format("Strategy deadheat percent %s %%", loseCounter * 1.0 / (episodeCounter) * 100.0));
        //CHECKSTYLE.ON
    }

    /**
     * Clear statistics counters.
     */
    public void clearStatistics() {
        episodeCounter = 0;
        winCounter = 0;
        loseCounter = 0;
        gameOutputLog = null;
    }

    /**
     * Get count of game episodes.
     * @return count
     */
    public int getEpisodeCounter() {
        return episodeCounter;
    }

    /**
     * Get count of strategy win.
     * @return count.
     */
    public int getWinCounter() {
        return winCounter;
    }

    /**
     * Get count of strategy lost.
     * @return count.
     */
    public int getLoseCounter() {
        return loseCounter;
    }

}
