package ru.inno.task4;

import org.springframework.stereotype.Component;
import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.service.DataFixable;
import ru.inno.task4.service.StoredData;

@Component
public class TestLogTransformation implements DataFixable {
    @Override
    @LogTransformation(logPath = "C:\\Temp\\LogTransformationTest.txt")
    public StoredData fix(StoredData storedData) {
        return storedData;
    }
}
