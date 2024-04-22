package org.example.gamelistv2.service;



import org.example.gamelistv2.entity.Review;
import org.example.gamelistv2.view.ReviewView;

import java.util.List;

public interface ReviewService {

    List<Review> getReviews(int animeId);

    void save(ReviewView reviewForm);

    void remove(Integer reviewId);
}
