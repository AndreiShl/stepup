package ru.inno.task4.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.inno.task4.model.Login;
import ru.inno.task4.model.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class FixDates implements DataFixable{
    private String logPath;

    @SneakyThrows
    @Override
    public StoredData fix(StoredData storedData) {
        List<Login> loginList = new ArrayList<>(storedData.logins());
        List<User> userList = new ArrayList<>();
        try (var log = new Log(logPath)) {
            loginList.removeIf(login -> login.getAccess_date() == null && log.putLog("Empty date " + login) );
            loginList.forEach(login -> {
                if (!userList.contains(login.getUser())) userList.add(login.getUser());
            });
            return new StoredData(loginList, userList);
        }
    }
}
