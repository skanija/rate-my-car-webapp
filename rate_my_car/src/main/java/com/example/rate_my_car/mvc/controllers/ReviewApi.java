package com.example.rate_my_car.mvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rate_my_car.mvc.models.Review;
import com.example.rate_my_car.mvc.services.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewApi {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/new")
    public Review create(@Valid @RequestBody Review review, BindingResult result ) {
        if (result.hasErrors()) {
            return null;
        }
        return reviewService.createReview(review);
    }

    @GetMapping("")
    public List<Review> getAll() {
        return reviewService.allReviews();
    }

    @GetMapping("/{id}")
    public Review getOneReview(@PathVariable("id") Long id) {
        return reviewService.findReview(id);
    }
}
