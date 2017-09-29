package com.anikulin.rps.core;

import java.util.Arrays;

/**
 * Created by anikulin on 28.09.17.
 */
public enum RPSType {

    ROCK ("R", "Rock", "S"),
    PAPER ("P", "Paper", "R"),
    SCISSORS ("S", "Scissors", "P");

    private final String description;
    private final String id;
    private Object beat;

    private RPSType(final String id, final String description, final String beatId) {
        this.description = description;
        this.id = id;
        this.beat = beatId;
    }

    public String getDescription() {
        return description;
    }

    public RPSType getBeat() {
        if (beat instanceof String) {
            try {
                beat = parse((String) beat); //We can't do it in the constructor, because of enum
            } catch (RPSTypeException e) {
                throw new RuntimeException("Incorrect RPSType of 'betId' in the constructor: " + beat);
            }
        }

        return (RPSType) beat;
    }

    public int isWin(final RPSType opponent) {
        return equals(opponent) ? 0 : opponent == getBeat() ? 1 : -1;
    }


    /**
     *
     * @param input
     * @return
     * @throws RPSTypeException
     */
    public static RPSType parse(final String input) throws RPSTypeException {

        return Arrays.stream(RPSType.values()).filter(
                v -> input.compareToIgnoreCase(
                        input.length() == 1 ? v.id : v.description
                ) == 0
        ).findFirst()
         .orElseThrow(
                 () -> new RPSTypeException(
                         String.format(
                                 "Invalid RPSType value: '%s'. It must be one of: ['Rock', 'Paper', 'Scissiors'] or ['R', 'P', 'S']",
                                 input
                         )
                 )
         );
    }
}
