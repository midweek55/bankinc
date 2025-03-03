package com.bankinc.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EntityScan("com.bankinc.cards.model")
@EnableJpaRepositories("com.bankinc.cards.repository")
public class CardsApplication {

    private static final Logger logger = LoggerFactory.getLogger(CardsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner logPostgresInfo() {
        return args -> {
            logger.info("Iniciando aplicación Bank Inc Cards con PostgreSQL");
            
            // Log variables de entorno relacionadas con PostgreSQL
            logger.info("DATABASE_URL está definida: {}", System.getenv("DATABASE_URL") != null);
            logger.info("PGUSER está definida: {}", System.getenv("PGUSER") != null);
            logger.info("PGHOST: {}", System.getenv("PGHOST"));
            logger.info("PGPORT: {}", System.getenv("PGPORT"));
            logger.info("PGDATABASE: {}", System.getenv("PGDATABASE"));
            
            // Log variables de entorno específicas de Spring
            logger.info("SPRING_DATASOURCE_URL está definida: {}", System.getenv("SPRING_DATASOURCE_URL") != null);
            
            logger.info("Configuración de PostgreSQL inicializada");
        };
    }
}
