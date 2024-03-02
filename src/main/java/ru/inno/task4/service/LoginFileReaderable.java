package ru.inno.task4.service;

import java.io.IOException;

public interface LoginFileReaderable {
    StoredData readData() throws IOException;
}
