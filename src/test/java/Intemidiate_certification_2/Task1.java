package Intemidiate_certification_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task1 {
    private PlayerService service;
    final String NAME = "Nikita";
    final int POINTS = 11;
    Path filePath = Path.of("./data.json");
    final String TESTFILE = "[{\"id\":1,\"nick\":\"Nikita\",\"points\":0,\"online\":true}]";


    @BeforeEach
    public void start() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
        service = new PlayerServiceImpl();
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
        service.createPlayer("newPlayer");
        Player testPlayer = service.getPlayerById(6);
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

}
