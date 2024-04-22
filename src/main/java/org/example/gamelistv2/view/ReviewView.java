package org.example.gamelistv2.view;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ReviewView {

    @NonNull
    private Integer gameId;

    @NotNull(message = "{review.content.required}")
    @Size(min = 5, message = "{review.content.minLength}")
    private String content;
}
