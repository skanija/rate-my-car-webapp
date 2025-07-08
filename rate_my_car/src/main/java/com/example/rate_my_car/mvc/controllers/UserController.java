package com.example.rate_my_car.mvc.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.rate_my_car.mvc.models.FormUser;
import com.example.rate_my_car.mvc.models.LoginUser;
import com.example.rate_my_car.mvc.models.User;
import com.example.rate_my_car.mvc.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newUser", new FormUser());
        model.addAttribute("newLogin", new LoginUser());
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") FormUser newUser, BindingResult result, Model model, HttpSession session) {

        newUser.isPasswordsMatching();
        User u = new User();
        u.setUserName(newUser.getUserName());
        u.setEmail(newUser.getEmail());
        u.setPassword(newUser.getPassword());

        User registeredUser = userService.register(u, result);

        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "login";
        }
        userService.createUser(u);
        session.setAttribute("loggedInUser", registeredUser.getId());

        return "redirect:/cars";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, Model model, HttpSession session) {

        if(result.hasErrors()) {
            model.addAttribute("newUser", new FormUser());
            return "login";
        }

        System.out.println(newLogin);

        User user = userService.login(newLogin, result);
        session.setAttribute("loggedInUser", user.getId());

        return "redirect:/cars";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/{user_id}")
    public String editUser(@PathVariable("user_id") long userId, Model model, HttpSession session) {
        User loggedInUser = userService.findUser((long) session.getAttribute("loggedInUser"));
        if(session.getAttribute("loggedInUser") != null && userService.findUser(userId).getId() == loggedInUser.getId()){
            FormUser editUser = new FormUser();
            editUser.setUserName(loggedInUser.getUserName());
            editUser.setEmail(loggedInUser.getEmail());
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("editUser", editUser);    
            return "userSettings";
        }
        return "redirect:/logout";
    }

    @RequestMapping("/users/edit")
    public String update(@Valid @ModelAttribute("editUser") FormUser editUser, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            User loggedInUser = userService.findUser((long) session.getAttribute("loggedInUser"));
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("editUser", editUser);
            return "userSettings";
        } else {
            User u = userService.findUser((Long)session.getAttribute("loggedInUser"));
            u.setUserName(editUser.getUserName());
            u.setEmail(editUser.getEmail());
            u.setPassword(BCrypt.hashpw(editUser.getPassword(), BCrypt.gensalt()));
            userService.updateUser(u);
            session.setAttribute("loggedInUser", u.getId());
            return "redirect:/cars";
        }
    }

}
