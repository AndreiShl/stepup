package ru.inno.task4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.inno.task4.service.FixDates;

@Configuration
@ComponentScan("ru.inno.task4")
public class AppConfig {
    @Value("${config.logFIxData}")
    String logPath;
    @Bean
    public FixDates getFixDates(){
        return new FixDates(logPath);
    }
}
