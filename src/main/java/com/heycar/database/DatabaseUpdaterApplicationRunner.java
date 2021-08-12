package com.heycar.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DatabaseUpdaterApplicationRunner implements ApplicationRunner {

    private static final Logger log = LogManager.getLogger(DatabaseUpdaterApplicationRunner.class);

    private final DatabaseUpdateService databaseUpdateService;

    public DatabaseUpdaterApplicationRunner(DatabaseUpdateService databaseUpdateService) {
        this.databaseUpdateService = databaseUpdateService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DATABASE UPDATER APPLICATION RUNNER - START");
        databaseUpdateService.updateDatabase();
        log.info("DATABASE UPDATER APPLICATION RUNNER - END");
    }

}
