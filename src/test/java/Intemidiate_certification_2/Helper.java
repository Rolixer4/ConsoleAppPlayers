package Intemidiate_certification_2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.security.Provider;
import java.util.List;

public class Helper {
    private PlayerService service;

    public Helper(PlayerService service) {
        this.service = service;
    }

    public List<Player> getPlayersFromFile(String testFile) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Player> listPlayers = mapper.readValue(testFile, new TypeReference<>() {
        });
        for (Player listPalyer : listPlayers) {
            service.createPlayer(listPalyer.getNick());
        }
        return listPlayers;
    }
}
