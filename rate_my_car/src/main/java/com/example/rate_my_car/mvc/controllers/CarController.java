package com.example.rate_my_car.mvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.rate_my_car.mvc.models.Car;
import com.example.rate_my_car.mvc.models.User;
import com.example.rate_my_car.mvc.services.CarService;
import com.example.rate_my_car.mvc.services.UserService;
import com.example.rate_my_car.mvc.storage.StorageService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CarController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/cars")
    public String displayAllCars(@RequestParam(required = false) String engineType, Model model, HttpSession session) {
        if(session.getAttribute("loggedInUser") != null){
            User user = userService.findUser((long) session.getAttribute("loggedInUser"));
            List<Car> all = carService.allCars();
            model.addAttribute("loggedInUser", user);
            model.addAttribute("allUsers", userService.allUsers());
            model.addAttribute("allCars", all);
            session.removeAttribute("currentCar");
            return "carIndex";
        }
        return "redirect:/logout";
    }

    @GetMapping("/cars/new")
    public String newCar(@Valid @ModelAttribute("car") Car car, BindingResult result, Model model, HttpSession session) {
        if(session.getAttribute("loggedInUser") != null){
            User user = userService.findUser((long) session.getAttribute("loggedInUser"));
            model.addAttribute("loggedInUser", user);
            return "addCar";
        }
        return "redirect:/logout";
    }

    @PostMapping("/cars/add")
    public String createCar(@Valid @ModelAttribute("car") Car car, BindingResult result, @RequestParam("image") MultipartFile image, Model model, HttpSession session) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("loggedInUser", userService.findUser((Long) session.getAttribute("loggedInUser")));
            return "addCar";
        }
        User user = userService.findUser((long) session.getAttribute("loggedInUser"));
        car.setUser(user);

        car.setImagePath(image.getOriginalFilename());
        car.setImageContentType(image.getContentType());
        car.setImageSize(image.getSize());

        carService.createCar(car);
        storageService.store(image);
        return "redirect:/cars";
    }

    @GetMapping("/cars/{car_id}")
    public String displayOneCar(@PathVariable("car_id") long carId, Model model, HttpSession session) {
        if(session.getAttribute("loggedInUser") != null){
            User user = userService.findUser((long) session.getAttribute("loggedInUser"));
            model.addAttribute("loggedInUser", user);
            model.addAttribute("poster", carService.findCar(carId).getUser());
            model.addAttribute("car", carService.findCar(carId));
            session.setAttribute("currentcar", carService.findCar(carId));
            return "oneCar";
        }
        return "redirect:/logout";
    }

    @RequestMapping("/cars/edit/{id}")
    public String editCar(@PathVariable("id") long carId, @Valid @ModelAttribute("car") Car car, BindingResult result, Model model, HttpSession session) {
        User loggedInUser = userService.findUser((long) session.getAttribute("loggedInUser"));
        if(session.getAttribute("loggedInUser") != null && carService.findCar(carId).getUser().getId() == loggedInUser.getId()){
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("car", carService.findCar(carId));
            return "editCar";
        }
        return "redirect:/logout";
    }

    @RequestMapping("/cars/edit")
    public String update(@Valid @ModelAttribute("car") Car car, BindingResult result, @RequestParam("image") MultipartFile image, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUser");

        if (userId == null || !car.getUser().getId().equals(userId)) {
            return "redirect:/logout";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("car", car);
            return "editCar";
        } else {
            Car newCar = car;
            newCar.setUser(carService.findCar(car.getId()).getUser());
            newCar.setImagePath(image.getOriginalFilename());
            newCar.setImageContentType(image.getContentType());
            newCar.setImageSize(image.getSize());
            carService.updateCar(newCar);
            storageService.store(image);
            return "redirect:/cars";
        }
    }

    @RequestMapping(value="/cars/delete/{id}")
    public String destroy(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUser");

        if (userId == null) {
            return "redirect:/logout";
        }   

        User loggedInUser = userService.findUser(userId);
        Car car = carService.findCar(id);

        if(car != null && car.getUser().getId().equals(loggedInUser.getId())) {
            carService.deleteCar(id);
        }
        return "redirect:/cars";
    }

}
