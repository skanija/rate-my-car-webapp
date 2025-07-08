package com.example.rate_my_car.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.services.CarService;

import jakarta.validation.Valid;

public class CarApi {

    @Autowired
    CarService carService;

    @PostMapping("/api/cars/new")
    public Car create(@Valid @ModelAttribute("car") Car car, BindingResult result) {
        return carService.createCar(car);
    }

    @RequestMapping("/api/cars/{id}")
    public Car show(@PathVariable("id") Long id) {
        Car expense = carService.findCar(id);
        return expense;
    }

}
