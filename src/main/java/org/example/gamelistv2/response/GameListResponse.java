package org.example.gamelistv2.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameListResponse {

    @JsonProperty("data")
    private List<GameResponse.Game> gameList;

    private Pagination pagination;

    @Data
    @NoArgsConstructor
    public static class Pagination {

        @JsonProperty("last_visible_page")
        private int lastVisiblePage;

        @JsonProperty("has_next_page")
        private boolean hasNextPage;

        @JsonProperty("current_page")
        private int currentPage;
    }
}
