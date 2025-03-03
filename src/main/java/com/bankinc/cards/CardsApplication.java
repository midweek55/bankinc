package com.bankinc.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

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
            Map<String, String> env = System.getenv();
            
            logger.info("Variables de entorno de PostgreSQL:");
            logger.info("PGHOST: {}", env.getOrDefault("PGHOST", "no definido"));
            logger.info("PGPORT: {}", env.getOrDefault("PGPORT", "no definido"));
            logger.info("PGDATABASE: {}", env.getOrDefault("PGDATABASE", "no definido"));
            logger.info("PGUSER está definida: {}", env.containsKey("PGUSER"));
            logger.info("PGPASSWORD está definida: {}", env.containsKey("PGPASSWORD"));
            
            // Construir URL de conexión para debug
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", 
                env.getOrDefault("PGHOST", "localhost"), 
                env.getOrDefault("PGPORT", "5432"), 
                env.getOrDefault("PGDATABASE", "postgres"));
            
            logger.info("URL de conexión construida: {}", jdbcUrl);
            logger.info("Configuración de PostgreSQL inicializada");
        };
    }
}
