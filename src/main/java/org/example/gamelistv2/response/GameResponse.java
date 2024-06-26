package org.example.gamelistv2.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameResponse {

    @JsonProperty("data")
    private Game game;

    @Data
    @NoArgsConstructor
    public static class Game {

        @JsonProperty("mal_id")
        private int malId;

        private int episodes;
        private int rank;
        private int year;

        private double score;

        private String title;
        private String type;
        private String status;
        private String rating;

        private String synopsis;
        private String background;

        private Images images;

        private Trailer trailer;

        @Data
        @NoArgsConstructor
        public static class Images {

            private Jpg jpg;

            @Data
            @NoArgsConstructor
            public static class Jpg {

                @JsonProperty("image_url")
                private String imageUrl;

                @JsonProperty("small_image_url")
                private String smallImageUrl;

                @JsonProperty("large_image_url")
                private String largeImageUrl;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Trailer {

            @JsonProperty("embed_url")
            private String embedUrl;
        }
    }
}
