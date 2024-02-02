/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;

@Service
public class PlayerH2Service implements PlayerRepository {
    @Autowired
    private JdbcTemplate TEAM;

    @Override
    public ArrayList<Player> getPlayers() {
        List<Player> playerList = TEAM.query("select * from player", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = TEAM.queryForObject("select * from player where id = ?", new PlayerRowMapper(), playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player addPlayer(Player player) {
        TEAM.update("insert into player(playerId, playerName, jerseyNumber, role) values (?, ?, ?, ?)",
                player.getPlayerId(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        Player savedPlayer = TEAM.queryForObject(
                "select * from player where playerid=?, playerName=?, jerseyNumber=? and role=?", new PlayerRowMapper(),
                player.getPlayerId(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savedPlayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            TEAM.update("update player set name =? where id =?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            TEAM.update("update player set jerseyNumber =? where id =?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            TEAM.update("update player set role =? where id =?", player.getRole(), playerId);
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        TEAM.update("delete from player where id=?", playerId);
    }
}
