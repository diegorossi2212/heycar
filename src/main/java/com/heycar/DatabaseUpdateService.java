package com.heycar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.heycar.database.DatabaseScriptComparator;
import com.heycar.database.DatabaseUtil;

@Service
public class DatabaseUpdateService {

    private static final Logger log = LogManager.getLogger(DatabaseUpdateService.class);

    private static final int QUERY_INSERT_VERSION_COLUMN_INDEX_VERSION = 1;
    private static final int QUERY_INSERT_VERSION_COLUMN_INDEX_APPLIED_DATE = 2;
    private static final int QUERY_INSERT_VERSION_COLUMN_INDEX_CONTEXT = 3;

    private static final int QUERY_SELECT_VERSION_COLUMN_INDEX_VERSION = 1;
    private static final int QUERY_SELECT_VERSION_COLUMN_INDEX_CONTEXT = 2;
    private static final int QUERY_SELECT_VERSION_COLUMN_INDEX_APPLIED_DATE = 3;

    private final DataSource dataSource;

    public DatabaseUpdateService(DataSource dataSource, String instanceContext, String instanceVersion) {
        this.dataSource = dataSource;
        this.instanceContext = instanceContext;
        this.instanceVersion = instanceVersion;
    }
    
    private final String instanceContext;
    private final String instanceVersion;

    public void updateDatabase() {
        log.info("START");
        log.info("CONTEXT = {} - VERSION = {}", instanceContext, instanceVersion);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] scripts = resolver.getResources("database/**");
            Arrays.sort(scripts, new DatabaseScriptComparator());

            executeScripts(scripts);

            setCurrentVersionAsApplied();

            checkIfVersionHasBeenApplied(instanceVersion);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("END");
    }

    private void executeScripts(Resource[] scripts) throws Exception {
        for (Resource script : scripts) {
            String version = script.getFilename();

            Date applicationDate = checkIfVersionHasBeenApplied(version);

            if (applicationDate == null) {
                applyVersionAndSetAsApplied(script, version);
            } else {
                log.info("APPLIED VERSION {} ON {}", version, applicationDate);
            }
        }
    }

    private void setCurrentVersionAsApplied() throws SQLException {
        if (checkIfVersionHasBeenApplied(instanceVersion) == null) {
            setVersionAsApplied(instanceVersion);
            log.info("APPLIED VERSION {}", instanceVersion);
        }
    }

    private void applyVersionAndSetAsApplied(Resource resource, String versionName) throws Exception {
        try {
            log.info("APPLYING VERSION {}", resource.getFilename());

            applyVersion(resource);
            setVersionAsApplied(versionName);

            log.info("APPLIED VERSION {}", versionName);
        } catch (Exception exception) {
            log.error("UNEXPECTED ERRROR APPLYING VERSION {}", versionName, exception);
            throw exception;
        }
    }

    private void applyVersion(Resource resource) throws Exception {

        Connection connection = null;
        String lastExecutedCommand = "";

        try (BufferedReader inputFile = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = inputFile.readLine()) != null) {
            	line.trim();
                if (line.isEmpty()) {
                    String command = removeLastSemicolon(stringBuilder.toString().trim());

                    lastExecutedCommand = command;
                    log.info("applyVersion - START SQL {}", command);
                    executeCommand(connection, command);

                    stringBuilder.setLength(0);
                } else {
                    stringBuilder.append(line).append(" ");
                }
            }

            if (stringBuilder.length() > 0) {
                String command = removeLastSemicolon(stringBuilder.toString().trim());

                lastExecutedCommand = command;
                executeCommand(connection, command);
            }

            connection.commit();
        } catch (Exception exception) {
            com.heycar.database.DatabaseUtil.rollbackConnectionQuietly(connection);

            log.error("LAST EXECUTED COMMAND = {}", lastExecutedCommand, exception);

            throw exception;
        } finally {
            DatabaseUtil.closeConnectionQuietly(connection);
        }
    }

    private Date checkIfVersionHasBeenApplied(String version) throws SQLException {
        String query = "SELECT ID, VERSION, APPLIED_DATE FROM APPLICATION_HISTORY WHERE APPLIED_DATE IS NOT NULL AND VERSION = ? AND CONTEXT = ?";

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(QUERY_SELECT_VERSION_COLUMN_INDEX_VERSION, version);
            preparedStatement.setString(QUERY_SELECT_VERSION_COLUMN_INDEX_CONTEXT, instanceContext);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp(QUERY_SELECT_VERSION_COLUMN_INDEX_APPLIED_DATE);
                }
            }
        } catch (SQLException sqlException) {
            if (versionTableNotExists()) {
                log.info("INITIAL CONFIGURATION OF DATABASE");
            } else {
                throw sqlException;
            }
        }
        return null;
    }

    private boolean versionTableNotExists() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, "APPLICATION_HISTORY", null);
            return !tables.next();
        }
    }

    private void setVersionAsApplied(String version) throws SQLException {
        String query = "INSERT INTO APPLICATION_HISTORY (ID, VERSION, APPLIED_DATE, CONTEXT) SELECT COALESCE(MAX(ID), 0) + 1, ?, ?, ? FROM "
                + "APPLICATION_HISTORY";

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(QUERY_INSERT_VERSION_COLUMN_INDEX_VERSION, version);
            preparedStatement.setTimestamp(QUERY_INSERT_VERSION_COLUMN_INDEX_APPLIED_DATE, new Timestamp(new Date().getTime()));
            preparedStatement.setString(QUERY_INSERT_VERSION_COLUMN_INDEX_CONTEXT, instanceContext);
            preparedStatement.executeUpdate();
        }
    }
    private static String removeLastSemicolon(String string) {
        int i = string.length() - 1;
        while (i > -1 && string.charAt(i) == ';') {
            i--;
        }
        return string.substring(0, i + 1);
    }

    private static void executeCommand(Connection connection, String command) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(command);
        }
    }
    
}
