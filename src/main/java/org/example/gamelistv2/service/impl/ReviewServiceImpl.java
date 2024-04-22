package org.example.gamelistv2.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.entity.Review;
import org.example.gamelistv2.exception.UserHasNoAccessException;
import org.example.gamelistv2.repository.ReviewRepository;
import org.example.gamelistv2.security.AuthenticationFacade;
import org.example.gamelistv2.service.ReviewService;
import org.example.gamelistv2.service.UserService;
import org.example.gamelistv2.view.ReviewView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public List<Review> getReviews(int gameId) {
        return reviewRepository.findAllByGameId(gameId);
    }

    @Override
    public void save(ReviewView reviewView) {
        Review review = Review.builder()
                .gameId(reviewView.getGameId())
                .content(reviewView.getContent())
                .user(userService.find(authenticationFacade.getUsername()))
                .build();

        reviewRepository.save(review);
    }

    @Override
    public void remove(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);

        if (!review.getUser().getUsername().equals(authenticationFacade.getUsername())) {
            throw new UserHasNoAccessException("User " + authenticationFacade.getUsername() + " can't remove " + review + " belonging to " + review.getUser().getUsername());
        }

        reviewRepository.deleteById(reviewId);
    }
}
