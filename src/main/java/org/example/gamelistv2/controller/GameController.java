package org.example.gamelistv2.controller;


import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.response.GameResponse.Game;
import org.example.gamelistv2.model.GameStatus;
import org.example.gamelistv2.response.GameListResponse;
import org.example.gamelistv2.response.GameResponse;
import org.example.gamelistv2.service.GameService;
import org.example.gamelistv2.service.JikanApiService;
import org.example.gamelistv2.service.ReviewService;
import org.example.gamelistv2.view.GameView;
import org.example.gamelistv2.view.ReviewView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final JikanApiService jikanApiService;

    @GetMapping("/{gameId}")
    public String searchById(
            @PathVariable(name = "gameId") @Min(1) int gameId,
            Model model) {

        GameResponse.Game game;
        try {
            game = jikanApiService.searchById(gameId);
        } catch (WebClientResponseException.NotFound e) {
            throw new EntityNotFoundException(e.getMessage());
        }

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviewService.getReviews(gameId));
        model.addAttribute("userGame", gameService.getUserGame(game.getMalId()));

        if (!model.containsAttribute("reviewView")) {
            model.addAttribute("reviewView", new ReviewView(game.getMalId()));
        }

        return "game/game-details";
    }

    @GetMapping("/random")
    public Mono<String> getRandomGame(
            Model model) {

        Mono<Game> gameMono = jikanApiService.searchRandom();

        return gameMono.flatMap(game -> {
            model.addAttribute("game", game);
            model.addAttribute("reviewView", new ReviewView(game.getMalId()));
            model.addAttribute("reviews", reviewService.getReviews(game.getMalId()));
            model.addAttribute("userGame", gameService.getUserGame(game.getMalId()));

            return Mono.just("game/game-details");
        }).switchIfEmpty(Mono.error(new EntityNotFoundException("Random game not found")));
    }

    @PostMapping("/search/{pageId}")
    public Mono<String> searchGame(
            @ModelAttribute("searchGame") GameView gameView,
            @PathVariable(name = "pageId") @Min(1) int pageId,
            Model model) {

        Mono<GameListResponse> gameListMono = jikanApiService.search(gameView.getTitle(), gameView.getGenres(), pageId);

        return gameListMono.map(gameListResponse -> {
            model.addAttribute("gameView", gameView);
            model.addAttribute("gameList", gameListResponse.getGameList());
            model.addAttribute("pagination", gameListResponse.getPagination());

            return "game/game-search";
        }).switchIfEmpty(Mono.error(new EntityNotFoundException("Game list not found")));
    }

    @GetMapping("/top/{pageId}")
    public Mono<String> getTopRatedGame(
            @PathVariable(name = "pageId") @Min(1) int pageId,
            Model model) {

        Mono<GameListResponse> gameListResponseMono = jikanApiService.searchByRating(pageId);

        return gameListResponseMono.flatMap(gameListResponse -> {
            model.addAttribute("gameList", gameListResponse.getGameList());
            model.addAttribute("pagination", gameListResponse.getPagination());

            return Mono.just("game/game-top");
        }).switchIfEmpty(Mono.error(new EntityNotFoundException("Top game list not found")));
    }

    @GetMapping("/set/{gameId}")
    public String setGameStatus(
            @PathVariable(name = "gameId") @Min(1) int gameId,
            @RequestParam(name = "status") GameStatus status) {

        gameService.updateUserGame(gameId, (x) -> x.setStatus(status));

        return "redirect:/game/{gameId}";
    }

    @GetMapping("/set/{gameId}/favourite")
    public String setGameStatusAsFavourite(
            @PathVariable(name = "gameId") @Min(1) int gameId) {

        gameService.updateUserGame(gameId, x -> x.setFavourite(!x.isFavourite()));

        return "redirect:/game/{gameId}";
    }

    @GetMapping("/score/{gameId}/{score}")
    public String setUserGameScore(
            @PathVariable(name = "gameId") @Min(1) int gameId,
            @PathVariable(name = "score") @Min(0) @Max(10) int score) {

        gameService.updateUserGame(gameId, x -> x.setScore(score));

        return "redirect:/game/{gameId}";
    }

    @GetMapping("/reset/{gameId}")
    public String resetUserGame(
            @PathVariable(name = "gameId") @Min(1) int gameId) {

        gameService.reset(gameId);

        return "redirect:/game/{gameId}";
    }
}
