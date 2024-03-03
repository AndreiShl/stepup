package ru.inno.task4.service;

import lombok.AllArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
public class Log implements Logable,AutoCloseable {
    private FileWriter log;
    public boolean putLog(String log) {
        try {
            this.log.append(log);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Log(String logPath) throws IOException {
        this.log = new FileWriter(logPath,true);
    }

    @Override
    public void close() throws Exception {
        log.close();
    }
}
