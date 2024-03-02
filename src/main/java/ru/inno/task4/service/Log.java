package ru.inno.task4.service;

import lombok.AllArgsConstructor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
@AllArgsConstructor
public class Log implements Logable,AutoCloseable {
    private PrintWriter log;
    public boolean putLog(String log) {
        try {
            this.log.write(log);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Log(String logPath) throws FileNotFoundException {
        this.log = new PrintWriter(logPath);
    }

    @Override
    public void close() throws Exception {
        log.close();
    }
}
