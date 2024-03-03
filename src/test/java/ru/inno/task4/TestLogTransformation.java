package ru.inno.task4;

import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.service.DataFixable;
import ru.inno.task4.service.StoredData;

public class TestLogTransformation implements DataFixable {
    @Override
    @LogTransformation(logPath = "C:\\Temp\\LogTransformationTest.txt")
    public StoredData fix(StoredData storedData) {
        return storedData;
    }
}
