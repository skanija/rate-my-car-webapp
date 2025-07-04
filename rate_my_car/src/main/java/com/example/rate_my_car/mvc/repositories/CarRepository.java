package com.example.rate_my_car.mvc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.models.User;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {

    List<Car> findAll();

    List<Car> findById(long id);

    void deleteById(long id);

    List<Car> findByUser(User user);

}
