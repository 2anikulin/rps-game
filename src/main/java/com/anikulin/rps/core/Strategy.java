package com.anikulin.rps.core;

/**
 * Strategy interface.
 * Decision algorithms must support this contract.
 *
 * Don't forget to add implementation to the StrategyService.
 */
public interface Strategy {

    /**
     * Get strategy id.
     * It must be unique identifier.
     * @return id.
     */
    String getId();

    /**
     * Get strategy decision.
     *
     * @return RPSType Rock, Paper or Scissors.
     */
    RPSType getDecision();

    /**
     * Handler. Fired by the system then bids are made and game episode finished.
     * @param opponent Opponent bid.
     * @param strategy Strategy bid.
     */
    void onEpisodeFinish(RPSType opponent, RPSType strategy);
}
