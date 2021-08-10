package com.heycar.test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.heycar.model.search.BaseSearch;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

class TestArchitecture {

    private static final Logger log = LogManager.getLogger(TestArchitecture.class);

    private static final String PACKAGE_ANY = ".(*)..";
    private static final String PACKAGE_BASE = "com.heycar";
    private static final String PACKAGE_CONFIG = PACKAGE_BASE + ".config";
    private static final String PACKAGE_DAO = PACKAGE_BASE + ".dao";
    private static final String PACKAGE_SEARCH = PACKAGE_BASE + ".model.search";
    private static final String PACKAGE_MAPPER = PACKAGE_BASE + ".model.mapper";
    private static final String PACKAGE_REST_CONTROLLER = PACKAGE_BASE + ".controller";
    private static final String PACKAGE_SERVICE = PACKAGE_BASE + ".service" + PACKAGE_ANY;

    private static final String SUFFIX_CONFIG = "Config";
    private static final String SUFFIX_DAO = "DAO";
    private static final String SUFFIX_SEARCH = "Search";
    private static final String SUFFIX_REST_CONTROLLER = "RestController";
    private static final String SUFFIX_SERVICE = "Service";

    @BeforeAll
    static void init() {
        log.info("init - START");
        log.info("init - PACKAGE BASE = {}", PACKAGE_BASE);
        log.info("init - PACKAGE CONFIG = {}", PACKAGE_CONFIG);
        log.info("init - PACKAGE DAO = {}", PACKAGE_DAO);
        log.info("init - PACKAGE SEARCH = {}", PACKAGE_SEARCH);
        log.info("init - PACKAGE MAPPER = {}", PACKAGE_MAPPER);
        log.info("init - PACKAGE REST CONTROLLER = {}", PACKAGE_REST_CONTROLLER);
        log.info("init - PACKAGE SERVICE = {}", PACKAGE_SERVICE);
        log.info("init - END");
    }

    @Test
    void testNoStandardStreamAccessOut() {
        log.info("testNoStandardStreamAccessOut - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = noClasses().that().resideInAPackage(PACKAGE_BASE + PACKAGE_ANY).should(GeneralCodingRules.ACCESS_STANDARD_STREAMS);

        archRule.check(javaClasses);

        log.info("testNoStandardStreamAccessOut - END");
    }

    @Test
    void testConfigSuffix() {
        log.info("testConfigSuffix - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(Configuration.class).should().haveSimpleNameEndingWith(SUFFIX_CONFIG);

        archRule.check(javaClasses);

        log.info("testConfigSuffix - END");
    }

    @Test
    void testConfigLocation() {
        log.info("testConfigLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(Configuration.class).should().resideInAPackage(PACKAGE_CONFIG);

        archRule.check(javaClasses);

        log.info("testConfigLocation - END");
    }

    @Test
    void testConfigAnnotation() {
        log.info("testConfigAnnotation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_CONFIG).should().beAnnotatedWith(Configuration.class);

        archRule.check(javaClasses);

        log.info("testConfigAnnotation - END");
    }

    @Test
    void testDaoSuffix() {
        log.info("testDaoSuffix - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_DAO).should().haveSimpleNameEndingWith(SUFFIX_DAO);

        archRule.check(javaClasses);

        log.info("testDaoSuffix - END");
    }

    @Test
    void testDaoLocation() {
        log.info("testDaoLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(Repository.class).should().resideInAPackage(PACKAGE_DAO);

        archRule.check(javaClasses);

        log.info("testDaoLocation - END");
    }

    @Test
    void testOnlyDaoInsideDaoLocation() {
        log.info("testOnlyDaoInsideDaoLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = noClasses().that().areNotAnnotatedWith(Repository.class).should().resideInAPackage(PACKAGE_DAO);

        archRule.check(javaClasses);

        log.info("testOnlyDaoInsideDaoLocation - END");
    }

    @Test
    void testDaoAnnotation() {
        log.info("testDaoAnnotation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_DAO).should().beAnnotatedWith(Repository.class);

        archRule.check(javaClasses);

        log.info("testDaoAnnotation - END");
    }

    @Test
    void testBaseSearchAssignableSuffix() {
        log.info("testBaseSearchAssignableSuffix - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAssignableTo(BaseSearch.class).should().haveSimpleNameEndingWith(SUFFIX_SEARCH);

        archRule.check(javaClasses);

        log.info("testBaseSearchAssignableSuffix - END");
    }

    @Test
    void testBaseSearchAssignableLocation() {
        log.info("testBaseSearchAssignableLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAssignableTo(BaseSearch.class).should().resideInAPackage(PACKAGE_SEARCH);

        archRule.check(javaClasses);

        log.info("testBaseSearchAssignableLocation - END");
    }

    @Test
    void testNoBaseSearchAssignableInsideBaseSearchAssignableLocation() {
        log.info("testNoBaseSearchAssignableInsideBaseSearchAssignableLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = noClasses().that().areNotAssignableTo(BaseSearch.class).should().resideInAPackage(PACKAGE_SEARCH);

        archRule.check(javaClasses);

        log.info("testNoBaseSearchAssignableInsideBaseSearchAssignableLocation - END");
    }

    @Test
    void testMapperDependencyFree() {
        log.info("testMapperDependencyFree - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = noClasses().that().resideInAPackage(PACKAGE_MAPPER + "..").should().dependOnClassesThat().resideInAPackage(PACKAGE_ANY);

        archRule.check(javaClasses);

        log.info("testMapperDependencyFree   - END");
    }

    @Test
    void testMapperOnlyAccessedByDao() {
        log.info("testMapperOnlyAccessedByDao - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_MAPPER).should().onlyBeAccessed().byClassesThat()
                .areAnnotatedWith(Repository.class);

        archRule.check(javaClasses);

        log.info("testMapperOnlyAccessedByDao - END");
    }

    @Test
    void testRestControllerSuffix() {
        log.info("testRestControllerSuffix - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(RestController.class).should().haveSimpleNameEndingWith(SUFFIX_REST_CONTROLLER);

        archRule.check(javaClasses);

        log.info("testRestControllerSuffix - END");
    }

    @Test
    void testRestControllerLocation() {
        log.info("testRestControllerLocation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(RestController.class).should().resideInAPackage(PACKAGE_REST_CONTROLLER);

        archRule.check(javaClasses);

        log.info("testRestControllerLocation - END");
    }

    @Test
    void testRestControllerAnnotation() {
        log.info("testRestControllerAnnotation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_REST_CONTROLLER).should().beAnnotatedWith(RestController.class);

        archRule.check(javaClasses);

        log.info("testRestControllerAnnotation - END");
    }

    @Test
    void testServiceSuffix() {
        log.info("testServiceSuffix - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().areAnnotatedWith(Service.class).should().haveSimpleNameEndingWith(SUFFIX_SERVICE);

        archRule.check(javaClasses);

        log.info("testServiceSuffix - END");
    }

    @Test
    void testServiceAnnotation() {
        log.info("testServiceAnnotation - START");

        JavaClasses javaClasses = new ClassFileImporter().importPackages(PACKAGE_BASE);

        ArchRule archRule = classes().that().resideInAPackage(PACKAGE_SERVICE).should().beAnnotatedWith(Service.class);

        archRule.check(javaClasses);

        log.info("testServiceAnnotation - END");
    }

}
