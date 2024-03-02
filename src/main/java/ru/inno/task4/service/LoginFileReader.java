package ru.inno.task4.service;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.inno.task4.exception.ReadFileException;
import ru.inno.task4.model.Login;
import ru.inno.task4.model.User;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginFileReader implements LoginFileReaderable {
    private final Map<String, User> usersMap = new HashMap<>();
    private final List<Login> logins = new ArrayList<>();
    @Value("${config.folder}")
    private String folderPath;
    @Value("${config.separator}")
    private char separator;

    @Override
    public StoredData readData() throws IOException {
        usersMap.clear();
        Files.walkFileTree(Paths.get(folderPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try (CSVReader reader
                             = new CSVReader(new FileReader(file.toFile(), StandardCharsets.UTF_8), separator)) {
                    String[] nextLine;
                    User user;
                    while ((nextLine = reader.readNext()) != null) {
                        String userName = nextLine[0];
                        if (!usersMap.containsKey(userName)) {
                            user = new User();
                            user.setUserName(userName);
                            user.setFio(nextLine[1]);
                            usersMap.put(userName, user);
                        } else {
                            user = usersMap.get(userName);
                        }
                        Login login = new Login();
                        login.setUser(user);
                        try {
                            login.setAccess_date(Instant.parse(nextLine[2]));
                        }
                        catch (DateTimeParseException e){
                            login.setAccess_date(null);
                        }

                        login.setApplication(nextLine[3]);
                        logins.add(login);
                    }
                } catch (IOException e) {
                    throw new ReadFileException();
                }
                return super.visitFile(file, attrs);
            }
        });
        return new StoredData(logins, new ArrayList<>(usersMap.values()));
    }
}
