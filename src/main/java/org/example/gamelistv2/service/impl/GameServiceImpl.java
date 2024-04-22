package org.example.gamelistv2.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.entity.Game;
import org.example.gamelistv2.entity.UserGame;
import org.example.gamelistv2.model.GameStatus;
import org.example.gamelistv2.repository.GameRepository;
import org.example.gamelistv2.repository.UserGameRepository;
import org.example.gamelistv2.response.GameResponse;
import org.example.gamelistv2.security.AuthenticationFacade;
import org.example.gamelistv2.service.GameService;
import org.example.gamelistv2.service.JikanApiService;
import org.example.gamelistv2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;
    private final JikanApiService jikanApiService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserGame getUserGame(int gameId) {
        return userGameRepository.findByGame_IdAndUser_Username(gameId, authenticationFacade.getUsername())
                .orElse(new UserGame());
    }

    @Override
    public Page<UserGame> getUserGameListByStatus(GameStatus status, PageRequest pageRequest) {
        return userGameRepository.findAllByStatusAndUser_Username(
                status,
                authenticationFacade.getUsername(),
                pageRequest
        );
    }

    @Override
    public Page<UserGame> getFavouriteUserGameList(boolean isFavourite, PageRequest pageRequest) {
        return userGameRepository.findAllByFavouriteAndUser_Username(
                isFavourite,
                authenticationFacade.getUsername(),
                pageRequest
        );
    }

    @Override
    public void updateUserGame(int gameId, Consumer<UserGame> consumer) {
        Game game = gameRepository.findById(gameId)
                .orElseGet(() -> {
                    GameResponse.Game retrievedGame = jikanApiService.searchById(gameId);
                    return Game.builder()
                            .id(gameId)
                            .title(retrievedGame.getTitle())
                            .image(retrievedGame.getImages().getJpg().getImageUrl())
                            .build();
                });

        UserGame userGame = getUserGame(gameId);

        userGame.setUser(userService.find(authenticationFacade.getUsername()));
        userGame.setGame(game);

        consumer.accept(userGame);

        userGameRepository.save(userGame);
    }

    @Override
    public void reset(int gameId) {
        userGameRepository.findAllByGame_Id(gameId)
                .stream()
                .filter(x -> x.getUser().getUsername().equals(authenticationFacade.getUsername()))
                .findFirst()
                .ifPresent(userGameRepository::delete);
    }
}
