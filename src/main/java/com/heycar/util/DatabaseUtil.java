package com.heycar.util;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.heycar.exception.UtilityClassInstantiationException;

public final class DatabaseUtil {

    private static final Logger log = LogManager.getLogger(DatabaseUtil.class);

    private DatabaseUtil() {
        throw new UtilityClassInstantiationException();
    }

    public static void closeConnectionQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("IT WAS NOT POSSIBLE TO CLOSE THE CONNECTION", e);
            }
        }
    }

    public static void rollbackConnectionQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (Exception e) {
                log.error("IT WAS NOT POSSIBLE TO EXECUTE THE ROLLBACK", e);
            }
        }
    }
}
