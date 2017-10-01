package com.anikulin.rps.shell;

import com.anikulin.rps.Application;
import com.anikulin.rps.core.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Implementation of User Command Line Interface.
 * It provides an interface to allow user to play the game.
 */
public class UserShell {

    private final Application application;

    /**
     * Constructor.
     * @param application Application instance.
     */
    public UserShell(final Application application) {
        this.application = application;
    }

    /**
     * Runs shell session.
     */
    public void execute() {

        System.out.println("**************************************");
        System.out.println("Welcome to the RPS terminal.");
        System.out.println("Type '\\q' to exit");
        System.out.println("Type 'ls' to show all available strategies");
        System.out.println("Type 'stat' to show statistics of current game");
        System.out.println("Type 'use [strategy]' to chose strategy and start the game");
        System.out.println("**************************************");
        Scanner sc = new Scanner(System.in);

        StrategyService strategyService = StrategyServiceFactory.getStrategyService();
        Strategy currentStrategy = null;
        boolean continueAnyWay = false;

        while(true) {

            System.out.print(">");
            String cmd = sc.nextLine();

            if ("\\q".equals(cmd)) {
                System.out.println("Goodbye!");
                break;
            } else if ("ls".equals(cmd)) {
                strategyService.getStrategies().forEach(
                      s -> System.out.println(s.getId())
                );
            } else if (cmd.startsWith("use ")) {
                String[] args = cmd.split(" ");
                if (args.length > 1) {
                    String strategyId = args[1];
                    currentStrategy = strategyService.getStrategy(strategyId);

                    if (currentStrategy == null) {
                        System.out.println("Unknown strategy: " + strategyId);
                        continue;
                    }

                    application.clearStatistics();

                    System.out.println("**************************************");
                    System.out.println(String.format("%s - strategy chosen", strategyId));
                    System.out.println("The Game have started!");
                    System.out.println("Type 'P' - Paper, 'R' - Rock, 'S' - Scissors");

                } else {

                    System.out.println("Command 'use' must have an argument (strategy)");
                }
            } else if ("stat".equals(cmd)) {

                application.printStatistics();
            } else if (currentStrategy != null) {
                try {
                    RPSType opponentBid = RPSType.parse(cmd);
                    RPSType strategyBid = currentStrategy.getDecision();

                    currentStrategy.onEpisodeFinish(opponentBid, strategyBid);
                    int winCode = strategyBid.isWin(opponentBid);

                    System.out.println(
                            String.format(
                                    "%s + %s = %s",
                                    opponentBid.getDescription(),
                                    strategyBid.getDescription(),
                                    winCode == 1 ? "You Lose" : winCode == -1 ? "You Win":"Dead heat"
                            )
                    );

                    try {
                        application.collectStatistics(opponentBid, strategyBid);
                    } catch (IOException e) {
                        if (continueAnyWay) {
                            continue;
                        }

                        System.out.println("Output file is not accessible. Do you want to continue anyway (Y)?");
                        System.out.print(":");
                        cmd = sc.nextLine();
                        if ("Y".compareToIgnoreCase(cmd) ==0) {
                            continueAnyWay = true;
                        } else {
                            break;
                        }
                    }
                } catch (RPSTypeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Unknown command: " + cmd);
            }
        }
    }
}
