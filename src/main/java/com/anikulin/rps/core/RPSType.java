package com.anikulin.rps.core;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Enum of game entities.
 */
public enum RPSType {

    ROCK ("R", "Rock", "S"),
    PAPER ("P", "Paper", "R"),
    SCISSORS ("S", "Scissors", "P");

    private final String description;
    private final String id;
    private Object beat;

    /**
     * Constructor.
     * @param id Entity id.
     * @param description Entity description.
     * @param beatId Id of beaten (lose) entity for current one.
     */
    RPSType(final String id, final String description, final String beatId) {
        this.description = description;
        this.id = id;
        this.beat = beatId;
    }

    /**
     * Get Id of entity.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get entity description.
     * @return Description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get beaten (lose) entity for current one.
     * @return Beaten entity.
     */
    public RPSType getBeat() {
        if (beat instanceof String) {
            try {
                beat = parse((String) beat);
            } catch (RPSTypeException e) {
                //CHECKSTYLE.OFF: This is critical exception. Application must be terminated
                throw new RuntimeException("Incorrect RPSType of 'betId' in the constructor: " + beat);
                //CHECKSTYLE.ON
            }
        }

        return (RPSType) beat;
    }

    /**
     * Compares the opponent bid with current value.
     * Returns:
     *  -1 if current value lost (opponent win)
     *   0 if bids are equal (dead heat)
     *   1 if current value win (opponent lost)
     *
     * @param opponent Opponent bid.
     * @return win code.
     */
    public int isWin(final RPSType opponent) {
        return equals(opponent) ? 0 : opponent == getBeat() ? 1 : -1;
    }


    /**
     * Get enum value from given string.
     * of throw exception.
     * @param input String value. It can be short value 'P' and long value 'Paper'.
     * @return Enum entity.
     * @throws RPSTypeException .
     */
    public static RPSType parse(final String input) throws RPSTypeException {

        return RPSType.stream().filter(
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

    /**
     * Wrap enum values to stream.
     * @return Stream of RPSType
     */
    public static Stream<RPSType> stream() {
        return Arrays.stream(RPSType.values());
    }
}
