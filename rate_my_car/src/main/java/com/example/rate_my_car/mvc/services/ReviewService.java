package com.example.rate_my_car.mvc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.models.Review;
import com.example.rate_my_car.mvc.repositories.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> allReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> findByCar(Car car) {
        return reviewRepository.findByCar(car);
    }

    public Review createReview(Review b) {
        return reviewRepository.save(b);
    }

    public Review findReview(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if(optionalReview.isPresent()) {
            return optionalReview.get();
        } else {
            return null;
        }
    }

    public Review updateReview(Review b) {
        return reviewRepository.save(b);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

}
