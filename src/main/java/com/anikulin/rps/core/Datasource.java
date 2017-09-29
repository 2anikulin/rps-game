package com.anikulin.rps.core;

import java.io.IOException;
import java.util.List;

/**
 * Created by anikulin on 29.09.17.
 */
public interface Datasource {

    List<String> getValues(String resourcePath) throws IOException;

}
