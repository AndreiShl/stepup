package ru.inno.task4.service;

@FunctionalInterface
public interface DataFixable {
    StoredData fix(StoredData storedData);
}
