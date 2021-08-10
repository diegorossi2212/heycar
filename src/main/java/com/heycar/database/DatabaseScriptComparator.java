package com.heycar.database;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;

public class DatabaseScriptComparator implements Comparator<Resource>, Serializable {

    @Serial
    private static final long serialVersionUID = 1362468060314135105L;

    private static final Logger log = LogManager.getLogger(DatabaseScriptComparator.class);

    private static final String LABEL_SCHEMA = "schema";

    private static final String SCRIPT_SEPARATOR_CHARS = "._";

    private static final int SCRIPT_PARSED_INDEX_MAJOR_VERSION = 0;
    private static final int SCRIPT_PARSED_INDEX_MINOR_VERSION = 1;
    private static final int SCRIPT_PARSED_INDEX_PATCH_VERSION = 2;
    private static final int SCRIPT_PARSED_INDEX_BUILD_VERSION = 3;
    private static final int SCRIPT_PARSED_INDEX_LABEL = 4;

    private static DatabaseScript generateDatabaseScriptFromFilename(String fileName) {
        String[] parsedFilename = StringUtils.split(fileName, SCRIPT_SEPARATOR_CHARS);

        return new DatabaseScript(Integer.parseInt(parsedFilename[SCRIPT_PARSED_INDEX_MAJOR_VERSION]),
                Integer.parseInt(parsedFilename[SCRIPT_PARSED_INDEX_MINOR_VERSION]),
                Integer.parseInt(parsedFilename[SCRIPT_PARSED_INDEX_PATCH_VERSION]),
                Integer.parseInt(parsedFilename[SCRIPT_PARSED_INDEX_BUILD_VERSION]), parsedFilename[SCRIPT_PARSED_INDEX_LABEL]);
    }

    private static int compareDatabaseScripts(DatabaseScript firstDatabaseScript, DatabaseScript secondDatabaseScript) {
        int result;

        if (firstDatabaseScript.getMajorVersion() < secondDatabaseScript.getMajorVersion()) {
            result = -1;
        } else if (firstDatabaseScript.getMajorVersion() > secondDatabaseScript.getMajorVersion()) {
            result = 1;
        } else if (firstDatabaseScript.getMinorVersion() < secondDatabaseScript.getMinorVersion()) {
            result = -1;
        } else if (firstDatabaseScript.getMinorVersion() > secondDatabaseScript.getMinorVersion()) {
            result = 1;
        } else if (firstDatabaseScript.getPatchVersion() < secondDatabaseScript.getPatchVersion()) {
            result = -1;
        } else if (firstDatabaseScript.getPatchVersion() > secondDatabaseScript.getPatchVersion()) {
            result = 1;
        } else if (firstDatabaseScript.getBuildVersion() < secondDatabaseScript.getBuildVersion()) {
            result = -1;
        } else if (firstDatabaseScript.getBuildVersion() > secondDatabaseScript.getBuildVersion()) {
            result = 1;
        } else if (firstDatabaseScript.getLabel().equalsIgnoreCase(LABEL_SCHEMA) && !secondDatabaseScript.getLabel().equalsIgnoreCase(LABEL_SCHEMA)) {
            result = -1;
        } else {
            result = 1;
        }

        return result;
    }

    @Override
    public int compare(Resource firstScript, Resource secondScript) {
        String firstScriptFilename = firstScript.getFilename();
        String secondScriptFilename = secondScript.getFilename();

        int result = 0;

        if (StringUtils.isNoneBlank(firstScriptFilename, secondScriptFilename)) {
            DatabaseScript firstDatabaseScript = generateDatabaseScriptFromFilename(firstScriptFilename);
            DatabaseScript secondDatabaseScript = generateDatabaseScriptFromFilename(secondScriptFilename);
            result = compareDatabaseScripts(firstDatabaseScript, secondDatabaseScript);
        } else {
            log.warn("AT LEAST ONE RESOURCE IS NOT RELATED TO A FILE NAME");
        }
        return result;
    }
}
