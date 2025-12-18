package com.example.userservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!local")
public class FlywayRunner {

    @Bean
    public CommandLineRunner runFlyway(DataSource dataSource) {
        return args -> {
            System.out.println("========================================");
            System.out.println("MANUALLY RUNNING FLYWAY MIGRATIONS");
            System.out.println("========================================");

            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .baselineVersion("0")
                    .load();

            int migrationsApplied = flyway.migrate().migrationsExecuted;

            System.out.println("========================================");
            System.out.println("FLYWAY: Applied " + migrationsApplied + " migration(s)");
            System.out.println("========================================");
        };
    }
}