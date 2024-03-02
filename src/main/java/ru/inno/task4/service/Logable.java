package ru.inno.task4.service;

public interface Logable extends AutoCloseable{
    boolean putLog(String log);
}
