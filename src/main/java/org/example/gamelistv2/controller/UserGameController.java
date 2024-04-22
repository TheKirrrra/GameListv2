package org.example.gamelistv2.controller;


import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.entity.UserGame;
import org.example.gamelistv2.model.GameStatus;
import org.example.gamelistv2.service.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/list")
public class UserGameController {

    private final GameService gameService;

    @GetMapping
    public String getGameList(
            @RequestParam(name = "status", required = false) GameStatus status,
            @RequestParam(name = "favourite", required = false, defaultValue = "false") Boolean isFavourite,
            @RequestParam(name = "page", required = false, defaultValue = "1") @Min(1) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") @Min(1) Integer size,
            Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("score").descending().and(Sort.by("game.title")));

        Page<UserGame> userGamePage;
        if (isFavourite) {
            userGamePage = gameService.getFavouriteUserGameList(true, pageRequest);
        } else {
            userGamePage = gameService.getUserGameListByStatus(status, pageRequest);
        }

        model.addAttribute("content", userGamePage.getContent());
        model.addAttribute("page", userGamePage);

        List<Integer> pageNumbers = IntStream.rangeClosed(1, userGamePage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("pageNumbers", pageNumbers);

        return "game/game-list";
    }
}
