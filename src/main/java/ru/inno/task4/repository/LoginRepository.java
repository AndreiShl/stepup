package ru.inno.task4.repository;

import org.springframework.data.repository.CrudRepository;
import ru.inno.task4.model.Login;

public interface LoginRepository extends CrudRepository<Login, Integer> {
}
