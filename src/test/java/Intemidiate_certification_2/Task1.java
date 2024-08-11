package Intemidiate_certification_2;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class Task1 {
    private PlayerService service;
    final String NAME = "Nikita";
    final int POINTS = 11;
    Path filePath = Path.of("./data.json");
    final String TESTFILE = "[{\"id\":1,\"nick\":\"Nikita\",\"points\":0,\"online\":true}]";
    Helper helper;

    @BeforeEach
    public void start() {
        service = new PlayerServiceImpl();
        helper = new Helper(service);
    }

    @AfterEach
    public void end() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
    }

    @Test
    @DisplayName("Добавить игрока и проверить наличие в списке")
    public void test1() {

        assertEquals(0, service.getPlayers().size());

        int testId = service.createPlayer(NAME);
        Player testPlayer = service.getPlayerById(testId);

        assertEquals(NAME, testPlayer.getNick());
        assertEquals(testId, testPlayer.getId());
        assertEquals(0, testPlayer.getPoints());
        assertTrue(testPlayer.isOnline());
    }

    @Test
    @DisplayName("Удалить игрока и проверить отсутствие в списке")
    public void test2() {

        int testId = service.createPlayer(NAME);
        assertEquals(1, service.getPlayers().size());
        service.deletePlayer(testId);
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @DisplayName("Добавить игрока с отсутствующим JSON-файлом")
    public void test3() {

        assertTrue(Files.notExists(Path.of("./data.json")));
        service.createPlayer(NAME);
        assertEquals(1, service.getPlayers().size());
    }

    @Test
    @DisplayName("Добавить игрока в присутствующий JSON-файлом")
    public void test4() throws IOException {
        Files.writeString(filePath, "[]");
        service.createPlayer(NAME);
        assertEquals(1, service.getPlayers().size());
    }

    @Test
    @DisplayName("Начислить баллы существующему игроку")
    public void test5() {

        int testId = service.createPlayer(NAME);
        Player testPlayer = service.getPlayerById(testId);
        service.addPoints(testId, POINTS);
        assertEquals(POINTS, testPlayer.getPoints());
    }

    @Test
    @DisplayName("Добавить очков поверх существующих")
    public void test6() {

        int testId = service.createPlayer(NAME);
        Player testPlayer = service.getPlayerById(testId);
        service.addPoints(testId, POINTS);
        service.addPoints(testId, POINTS);
        assertEquals(POINTS * 2, testPlayer.getPoints());
    }

    @Test
    @DisplayName("Получить игрока по Id")
    public void test7() {
        int testId = service.createPlayer(NAME);
        Player testPlayer = service.getPlayerById(testId);
        assertEquals(testPlayer, service.getPlayerById(testId));
    }

    @Test
    @DisplayName("Проверить корректность сохранения в файл")
    public void test8() throws IOException {
        service.createPlayer(NAME);
        String file = Files.readString(Path.of("./data.json"));
        assertEquals(TESTFILE, file);
    }

    @Test
    @DisplayName("Проверить корректность чтения из файла")
    public void test9() throws IOException {
        Files.writeString(filePath, TESTFILE);
        PlayerService serviceTest = new PlayerServiceImpl();
        Player testPlayer = serviceTest.getPlayerById(1);

        assertEquals(NAME, testPlayer.getNick());
        assertEquals(1, testPlayer.getId());
        assertEquals(0, testPlayer.getPoints());
        assertTrue(testPlayer.isOnline());
    }

    @Test
    @DisplayName("Проверить, что id всегда уникальный")
    public void test10() {
        for (int i = 1; i <= 5; i++) {
            service.createPlayer(NAME + i);
        }
        service.deletePlayer(3);
        int testId = service.createPlayer("newPlayer");
        Player testPlayer = service.getPlayerById(testId);
        assertEquals(6, testPlayer.getId());
    }

    @Test
    @DisplayName("запросить список игроков без JSON-файла")
    public void test11() {
        assertEquals("[]", service.getPlayers().toString());
    }

    @Test
    @DisplayName("Проверить создание игрока с 15 символами")
    public void test12() {
        int testId = service.createPlayer("123456789012345");
        Player testPlayer = service.getPlayerById(testId);
        assertEquals(15, testPlayer.getNick().length());
    }

    @Test
    @DisplayName("Удалить игрока, которого нет")
    public void test1N() {
        for (int i = 1; i <= 8; i++) {
            service.createPlayer(NAME + i);
        }
        assertThrows(NoSuchElementException.class, () -> service.deletePlayer(10));
    }

    @Test
    @DisplayName("Создать дубликат игрока")
    public void test2N() {
        service.createPlayer(NAME);
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(NAME));
    }

    @Test
    @DisplayName("получить игрока по id, которого нет")
    public void test3N() {
        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(5));
    }

    @Test
    @DisplayName("сохранить игрока с пустым ником")
    public void test4N() {
        String name = null;
        int testId = service.createPlayer(name);
        Player testPlayer = service.getPlayerById(testId);
        assertEquals(null, testPlayer.getNick());
    }

    @Test
    @DisplayName("начислить отрицательное число очков")
    public void test5N() {
        int testId = service.createPlayer(NAME);
        Player testPlayer = service.getPlayerById(testId);
        service.addPoints(testId, -5);
        assertEquals(-5, testPlayer.getPoints());
    }

    @Test
    @DisplayName("Накинуть очков игроку, которого нет")
    public void test6N() {
        assertThrows(NoSuchElementException.class, () -> service.addPoints(4, 5));
    }


    @Test
    @DisplayName("проверить загрузку системы с другим json-файлом")
    public void test7N() throws IOException {
        String testFile = Files.readString(Path.of("E:\\Courses\\ConsoleAppPlayers\\src\\test\\resources\\TestFile.json"));
        assertEquals(helper.getPlayersFromFile(testFile), service.getPlayers().stream().toList());
    }

    @Test
    @DisplayName("проверить корректность загрузки json-файла с дубликатом")
    public void test8N() throws IOException {
        String testFile = Files.readString(Path.of("E:\\Courses\\ConsoleAppPlayers\\src\\test\\resources\\duplicate.json"));
        assertThrows(IllegalArgumentException.class, () -> helper.getPlayersFromFile(testFile));
        assertEquals(4, service.getPlayers().size());
    }

    @Test
    @DisplayName("Проверить создание игрока с 16 символами")
    public void test9N() {
        String name16 = "1234567890123456";
        int testId = service.createPlayer(name16);
        Player testPlayer = service.getPlayerById(testId);
        assertEquals(16, testPlayer.getNick().length());
    }

}
