package com.example.rate_my_car.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.models.Review;
import com.example.rate_my_car.mvc.models.User;
import com.example.rate_my_car.mvc.services.CarService;
import com.example.rate_my_car.mvc.services.ReviewService;
import com.example.rate_my_car.mvc.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews/new")
    public String newReviewForm(@RequestParam("carId") Long carId, Model model, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUser");
        if (loggedInUserId == null) { 
            return "redirect:/logout";
        }
        
        Car car = carService.findCar(carId);
        if (car == null) {
            return "redirect:/cars";
        }

        Review review = new Review();
        review.setRating(1);

        model.addAttribute("review", review);
        model.addAttribute("car", car);
        return "reviewForm";
    }

    @PostMapping("/reviews/create")
    public String createReview(@Valid @ModelAttribute("review") Review review, BindingResult result, @RequestParam("carId") Long carId, HttpSession session, Model model) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUser");

        if (result.hasErrors()) {
            model.addAttribute("submitted", true);
            Car car = carService.findCar(carId);
            model.addAttribute("car", car);
            model.addAttribute("review", review);
            System.out.println(result.getAllErrors());
            return "reviewForm";
        }

        User user = userService.findUser(loggedInUserId);
        Car car = carService.findCar(carId);

        review.setUser(user);
        review.setCar(car);
        reviewService.createReview(review);

        return "redirect:/cars/" + carId;
    }

    @GetMapping("/review/{id}")
    public String showReview(@PathVariable("id") Long id, Model model, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUser");
        
        if (loggedInUserId == null) { 
            return "redirect:/logout";
        }
        
        Review review = reviewService.findReview(id);
        if (review == null) {
            return "redirect:/carsindex";
        }
        model.addAttribute("review", review);
        return "showReview";
    }
}
