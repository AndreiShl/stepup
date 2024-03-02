package ru.inno.task4.service;

import ru.inno.task4.model.Login;

import java.util.List;

public class FixApplication implements DataFixable {
    List<String> acceptedApplications = List.of("web", "mobile");

    @Override
    public StoredData fix(StoredData storedData) {
        for (Login login : storedData.logins()) {
            if (!acceptedApplications.contains(login.getApplication())) {
                login.setApplication("other " + login.getApplication());
            }
        }
        return storedData;
    }
}
