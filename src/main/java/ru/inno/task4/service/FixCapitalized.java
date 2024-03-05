package ru.inno.task4.service;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;
import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.model.User;

@Component
public class FixCapitalized implements DataFixable {

    @Override
    @LogTransformation(logPath = "C:\\Temp\\logFixCapitalized.txt")
    public StoredData fix(StoredData storedData) {
        for (User user: storedData.users()) {
            user.setFio(WordUtils.capitalizeFully(user.getFio()));
        }
        return storedData;
    }
}
