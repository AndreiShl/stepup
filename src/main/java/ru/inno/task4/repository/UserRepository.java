package ru.inno.task4.repository;

import org.springframework.data.repository.CrudRepository;
import ru.inno.task4.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUserName(String userName);
}
