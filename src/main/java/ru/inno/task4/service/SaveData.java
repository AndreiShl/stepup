package ru.inno.task4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.inno.task4.model.User;
import ru.inno.task4.repository.LoginRepository;
import ru.inno.task4.repository.UserRepository;

import java.io.IOException;

@Component
public class SaveData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;

    public void saveData(StoredData storedData) throws IOException {
        for (User user : storedData.users()) {
            User storedUserbyName = userRepository.getUserByUserName(user.getUserName());
            if (storedUserbyName != null) {
                user.setId(storedUserbyName.getId());
                userRepository.save(user);
            } else {
                user.setId(userRepository.save(user).getId());
            }
        }
        loginRepository.saveAll(storedData.logins());
    }
}
