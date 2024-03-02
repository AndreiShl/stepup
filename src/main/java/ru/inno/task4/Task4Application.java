package ru.inno.task4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.inno.task4.service.*;
import ru.inno.task4.util.LogUtils;

import java.io.IOException;

@SpringBootApplication
public class Task4Application {
    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(Task4Application.class, args);
        LoginFileReaderable fileReaderable = context.getBean(LoginFileReader.class);
        StoredData storedData = fileReaderable
                .readData()
                .fix(LogUtils.logTransform(new FixApplication()))
                .fix(LogUtils.logTransform(new FixCapitalized()))
                .fix(new FixDates().setLogPath("C:\\Temp\\log1.txt"))
                ;
        System.out.println(storedData);
        SaveData saveData = context.getBean(SaveData.class);
        saveData.saveData(storedData);
    }
}
