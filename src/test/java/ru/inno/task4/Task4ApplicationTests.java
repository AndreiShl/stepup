package ru.inno.task4;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inno.task4.model.Login;
import ru.inno.task4.model.User;
import ru.inno.task4.repository.LoginRepository;
import ru.inno.task4.repository.UserRepository;
import ru.inno.task4.service.*;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Task4ApplicationTests {

    @Autowired
    LoginFileReaderable fileReaderable;
    @Autowired
    SaveData saveData;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    UserRepository userRepository;

    private StoredData generateTestData() {
        var user1 = new User();
        user1.setFio("Иванов Иван Иванович");
        user1.setUserName("iivanov");
        var login1 = new Login();
        login1.setApplication("web");
        login1.setAccess_date(Instant.parse("2024-02-29T12:24:15Z"));
        login1.setUser(user1);
        var login2 = new Login();
        login2.setApplication("mobile");
        login2.setAccess_date(Instant.parse("2024-02-29T12:26:15Z"));
        login2.setUser(user1);
        var login3 = new Login();
        login3.setApplication("Docker");
        login3.setAccess_date(Instant.parse("2024-02-29T12:29:15Z"));
        login3.setUser(user1);
        return new StoredData(List.of(login1, login3, login2), List.of(user1));
    }

    @Test
    @DisplayName("Считывание данных")
    @SneakyThrows
    public void loginFileReaderTest() {
        var storedData = fileReaderable.readData();
        Assertions.assertTrue(CollectionUtils.isEqualCollection(storedData.logins(), generateTestData().logins()));
        Assertions.assertTrue(CollectionUtils.isEqualCollection(storedData.users(), generateTestData().users()));
    }

    @Test
    @DisplayName("Запись данных Logins")
    @SneakyThrows
    public void testSaveDataLogins() {
        saveData.saveData(generateTestData());
        List<Login> loginListWoId = new ArrayList<>();
        loginRepository.findAll().forEach(loginListWoId::add);
        loginListWoId.forEach(login -> {
            login.setId(null);
            login.getUser().setId(null);
        });
        Assertions.assertTrue(CollectionUtils.isEqualCollection(loginListWoId, generateTestData().logins()));
    }

    @Test
    @DisplayName("Запись данных Users")
    @SneakyThrows
    public void testSaveDataUsers() {
        saveData.saveData(generateTestData());
        List<User> userListWoId = new ArrayList<>();
        userRepository.findAll().forEach(userListWoId::add);
        userListWoId.forEach(user -> user.setId(null));
        Assertions.assertTrue(CollectionUtils.isEqualCollection(userListWoId, generateTestData().users()));
    }

    @Test
    @DisplayName("Компонента FixCapitalized")
    public void testFixCapitalized() {
        var storedData = generateTestData();
        storedData.users().forEach(user -> user.setFio(user.getFio().toLowerCase()));
        var fixedStoredData = storedData.fix(new FixCapitalized());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(fixedStoredData.users(), generateTestData().users()));
    }

    @Test
    @DisplayName("Компонента FixApplication")
    public void testFixApplication() {
        List<String> acceptedApplications = List.of("web", "mobile");
        var fixedStoredData = generateTestData().fix(new FixApplication());
        List<Login> loginList = generateTestData().logins();
        loginList.forEach(login -> {
            if (!acceptedApplications.contains(login.getApplication())) {
                login.setApplication("other " + login.getApplication());
            }
        });
        Assertions.assertTrue(CollectionUtils.isEqualCollection(fixedStoredData.logins(),loginList));
    }

    @Test
    @DisplayName("Компонента FixDates")
    public void testFixDates(){
        var storedData = generateTestData();
        storedData.logins().forEach(login -> login.setAccess_date(null));
        var fixedStoredData = storedData.fix(new FixDates("C:\\Temp\\testlog.txt"));
        Assertions.assertTrue(fixedStoredData.logins().isEmpty());
    }

    @Test
    @DisplayName("Создание log FixDates")
    @SneakyThrows
    public void testLogFixDates(){
        var logFileName = "C:\\Temp\\testlog.txt";
        var logFIleNamePath = Path.of(logFileName);
        String allLog;
        var storedData = generateTestData();
        Files.deleteIfExists(logFIleNamePath);
        storedData.logins().forEach(login -> login.setAccess_date(null));
        storedData.fix(new FixDates(logFileName));
        try (FileInputStream fileInputStream = new FileInputStream(logFileName)){
            allLog = IOUtils.toString(fileInputStream, "UTF-8");
        }
        finally {
            Files.deleteIfExists(logFIleNamePath);
        }
        Assertions.assertTrue(allLog.contains("Empty date Login(id=null, access_date=null, " +
                "user=User(id=null, userName=iivanov, fio=Иванов Иван Иванович), application=web)"));
    }

    @Test
    @DisplayName("Логирование с помощью аннотации @LogTransformation")
    @SneakyThrows
    public void testLogTransformation(){
        var logfileName = "C:\\Temp\\LogTransformationTest.txt";
        var logfileNamePath = Path.of(logfileName);
        String allLog;
        Files.deleteIfExists(logfileNamePath);
        generateTestData().fix(new TestLogTransformation());
        try (FileInputStream fileInputStream = new FileInputStream(logfileName)){
            allLog = IOUtils.toString(fileInputStream, "UTF-8");
        }
        finally {
            Files.deleteIfExists(logfileNamePath);
        }
        Assertions.assertTrue(allLog.contains(TestLogTransformation.class.getName()));
        Assertions.assertTrue(allLog.contains(generateTestData().toString()));
    }
}
