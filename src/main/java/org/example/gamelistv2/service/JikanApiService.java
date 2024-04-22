package org.example.gamelistv2.service;


import org.example.gamelistv2.response.GameListResponse;
import org.example.gamelistv2.response.GameResponse;
import reactor.core.publisher.Mono;

public interface JikanApiService {

    Mono<GameListResponse> search(String gameTitle, String gameGenres, int pageNumber);

    Mono<GameListResponse> searchByRating(int pageNumber);

    GameResponse.Game searchById(int gameId);

    Mono<GameResponse.Game> searchRandom();
}
