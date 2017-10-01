package com.anikulin.rps.strategies;

import com.anikulin.rps.core.RPSType;
import com.anikulin.rps.core.Strategy;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This strategy based on Markov chain.
 * https://en.wikipedia.org/wiki/Markov_chain
 *
 * You can set length of Markov chain as you wish.
 * but be sensible, each step increase count of possible states at 3^(steps count)!
 */
public class ProbabilityStrategy implements Strategy {

    private static final String ID = "markov";
    private static final int DEFAULT_MARKOV_CHAIN_LEN = 5;

    private final Map<String, State> statesMap;
    private final int markovChainLength;

    private String currentChainKey = "";

    /**
     * Default Constructor.
     */
    public ProbabilityStrategy() {
        this(DEFAULT_MARKOV_CHAIN_LEN);
    }

    /**
     * Constructor.
     * @param markovChainLength -Length of Markov chain
     */
    public ProbabilityStrategy(final int markovChainLength) {
        this.markovChainLength = markovChainLength;
        statesMap = buildMarkovChain(markovChainLength);
    }

    /**
     * Get strategy id.
     * @return id
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * Get decision.
     * @return decision.
     */
    @Override
    public RPSType getDecision() {
        RPSType res;
        if (currentChainKey.length() < markovChainLength) {
            res = getRandomDecision();
        } else {

            RPSType predictionBid = statesMap.get(currentChainKey)
                    .getMax()
                    .orElseGet(this::getRandomDecision);

            res = RPSType.stream()
                    .filter(
                            t -> t.getBeat().equals(predictionBid)
                    ).findAny().get();
        }

        return res;
    }

    /**
     * On episode finished.
     * @param opponent Opponent bid.
     * @param strategy Strategy bid.
     */
    @Override
    public void onEpisodeFinish(final RPSType opponent, final RPSType strategy) {

        if (currentChainKey.length() == markovChainLength) {
            statesMap.get(currentChainKey).incrementStateCount(opponent);
        }

        currentChainKey+= opponent.getId();

        if (currentChainKey.length() > markovChainLength) {
            currentChainKey = currentChainKey.substring(1);
        }
    }

    private Map<String, State> buildMarkovChain(final int length) {
        Map<String, State> map = new HashMap<>();

        buildKeys(
                RPSType.stream().map(RPSType::getId).collect(Collectors.toList()),
                new AtomicInteger(markovChainLength)
        ).forEach(
                key -> map.put(key, new State())
        );

        return map;
    }

    private List<String> buildKeys(final List<String> keys, final AtomicInteger lengthCounter) {
        if (lengthCounter.decrementAndGet() > 0) {

            List<String> upKeys = keys.stream() .flatMap(
                    k -> RPSType.stream().map( v -> k + v.getId())
            ).collect(Collectors.toList());

            return buildKeys(upKeys, lengthCounter);
        }

        return keys;
    }

    private RPSType getRandomDecision() {
        return RPSType.values()[(int) (RPSType.values().length *  Math.random())];
    }



    /**
     * Helper class.
     * Keeps counters for states in Markov chain.
     */
    static class State {

        private final Map<RPSType, AtomicInteger> stateMap = new HashMap<>();

        /**
         * Get bid with max counter.
         * @return RPSType
         */
        public Optional<RPSType> getMax() {
            return stateMap.entrySet().stream()
                    .max(Comparator.comparingInt(o -> o.getValue().get()))
                    .map(Map.Entry::getKey);
        }

        /**
         * Increments counter of state.
         * @param state key.
         * @return new value of counter.
         */
        public int incrementStateCount(final RPSType state) {
            return stateMap.computeIfAbsent(
                    state,
                    s -> new AtomicInteger(0)
            ).incrementAndGet();
        }
    }
}
