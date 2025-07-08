package com.example.rate_my_car.mvc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.repositories.CarRepository;

@Service
public class CarService {

    private final CarRepository carRepository;
    
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> allCars() {
        return carRepository.findAll();
    }

    public Car createCar(Car b) {
        return carRepository.save(b);
    }

    public Car findCar(Long id) {
        Optional<Car> optionalUser = carRepository.findById(id);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }
    }

    public Car updateCar(Car b) {
        return carRepository.save(b);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

}
