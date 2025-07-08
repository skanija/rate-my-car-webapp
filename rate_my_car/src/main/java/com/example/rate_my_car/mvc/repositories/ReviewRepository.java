package com.example.rate_my_car.mvc.repositories;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.models.Review;
import com.example.rate_my_car.mvc.models.User;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findAll();
    List<Review> findByUser(User user);
    List<Review> findByCar(Car car);
}
