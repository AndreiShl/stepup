package ru.inno.task4.service;

import ru.inno.task4.model.Login;
import ru.inno.task4.model.User;

import java.util.List;

public record StoredData(List<Login> logins, List<User> users) {
    public StoredData fix(DataFixable dataFixable){
        return dataFixable.fix(this);
    }
}