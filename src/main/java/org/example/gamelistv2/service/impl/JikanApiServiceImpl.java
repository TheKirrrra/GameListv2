package org.example.gamelistv2.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.gamelistv2.config.JikanApiProperties;
import org.example.gamelistv2.response.GameListResponse;
import org.example.gamelistv2.response.GameResponse;
import org.example.gamelistv2.service.JikanApiService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class JikanApiServiceImpl implements JikanApiService {

    private final WebClient webClient;
    private final JikanApiProperties properties;

    public JikanApiServiceImpl(WebClient.Builder webClientBuilder, JikanApiProperties properties) {
        this.webClient = webClientBuilder.baseUrl(properties.getBaseUrl()).build();
        this.properties = properties;
    }

    @Override
    public Mono<GameListResponse> search(String gameTitle, String gameGenres, int pageNumber) {
        StringBuilder urlBuilder = new StringBuilder(properties.getPaths().get("game"));
        urlBuilder.append(properties.getParams().get("page")).append(pageNumber)
                .append(properties.getParams().get("limit"))
                .append(properties.getParams().get("orderBy.score"))
                .append(properties.getParams().get("sort.desc"));

        if (gameTitle != null && !gameTitle.isBlank()) {
            urlBuilder.append(properties.getParams().get("title")).append(gameTitle);
        }

        if (gameGenres != null && !gameGenres.isBlank()) {
            urlBuilder.append(properties.getParams().get("genres")).append(gameGenres);
        }

        String url = urlBuilder.toString();

        log.info(url);

        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(GameListResponse.class)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Game list not found")));
    }

    public Mono<GameListResponse> searchByRating(int pageNumber) {
        String url = properties.getPaths().get("top") +
                properties.getParams().get("page") + pageNumber +
                properties.getParams().get("limit");

        log.info(url);

        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(GameListResponse.class)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Top game list not found")));
    }

    public GameResponse.Game searchById(int gameId) {
        String url = properties.getPaths().get("game.id") + gameId;

        log.info(url);

        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(GameResponse.class)
                .map(GameResponse::getGame)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Game with id " + gameId + " not found")))
                .block(Duration.of(10, ChronoUnit.SECONDS));
    }

    @Override
    public Mono<GameResponse.Game> searchRandom() {
        String url = properties.getPaths().get("random");

        log.info(url);

        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(GameResponse.class)
                .map(GameResponse::getGame)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Random game not found")));
    }
}
