package com.heycar.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = { JdbcRepositoriesAutoConfiguration.class })
class TestApplication {

    @BeforeAll
    public static void init() {
    }

    @Test
    void testContextLoads() {}

}
