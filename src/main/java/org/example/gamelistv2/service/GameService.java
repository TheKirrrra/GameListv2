package org.example.gamelistv2.service;


import org.example.gamelistv2.entity.UserGame;
import org.example.gamelistv2.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.function.Consumer;

public interface GameService {

    UserGame getUserGame(int gameId);

    Page<UserGame> getUserGameListByStatus(GameStatus status, PageRequest score);

    Page<UserGame> getFavouriteUserGameList(boolean isFavourite, PageRequest score);

    void updateUserGame(int gameId, Consumer<UserGame> consumer);

    void reset(int gameId);
}
