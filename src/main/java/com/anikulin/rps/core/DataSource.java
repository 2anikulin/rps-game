package com.anikulin.rps.core;

import java.io.IOException;
import java.util.List;

/**
 * DataSource interface.
 * This is contract for test-dataset providers.
 */
public interface DataSource {

    /**
     * Get list of opponent bids ('R','P','S').
     *
     * @param resourcePath Path to resource.
     * @return List of bids.
     * @throws IOException .
     */
    List<String> getValues(String resourcePath) throws IOException;

}
