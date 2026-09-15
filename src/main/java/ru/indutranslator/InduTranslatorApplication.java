package ru.indutranslator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = {"ru.indutranslator.domain.entity", "ru.indutranslator.domain.entity.enterprise"})
@EnableJpaRepositories(basePackages = "ru.indutranslator.domain.repository")
@EnableAsync
@EnableScheduling
public class InduTranslatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InduTranslatorApplication.class, args);
    }
}
