package ru.inno.task4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.inno.task4.config.AppConfig;
import ru.inno.task4.service.*;

import java.io.IOException;

@SpringBootApplication
public class Task4Application {
    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(AppConfig.class, args);
        LoginFileReaderable fileReaderable = context.getBean(LoginFileReader.class);
        StoredData storedData = fileReaderable
                .readData()
                .fix(context.getBean(FixApplication.class))
                .fix(context.getBean(FixCapitalized.class))
                .fix(context.getBean(FixDates.class))
                ;
        System.out.println(storedData);
        SaveData saveData = context.getBean(SaveData.class);
        saveData.saveData(storedData);
    }
}
