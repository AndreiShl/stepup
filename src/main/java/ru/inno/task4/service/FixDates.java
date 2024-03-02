package ru.inno.task4.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class FixDates implements DataFixable{

    private String logPath;

    public FixDates setLogPath(String logPath){
        this.logPath = logPath;
        return this;
    }

    @SneakyThrows
    @Override
    public StoredData fix(StoredData storedData) {
        System.out.println("logPath = " + logPath);
        try (var log = new Log(logPath)) {
            storedData.logins().removeIf(login -> login.getAccess_date() == null && log.putLog("Empty date " + login) );
            return storedData;

//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new WriteLogFileException();
        }
    }
}
