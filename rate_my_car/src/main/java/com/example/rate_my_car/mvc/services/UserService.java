package com.example.rate_my_car.mvc.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.rate_my_car.mvc.models.LoginUser;
import com.example.rate_my_car.mvc.models.User;
import com.example.rate_my_car.mvc.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User newUser, BindingResult result) {
        Optional<User> potentialUser = userRepository.findByEmail(newUser.getEmail());
        if(potentialUser.isPresent()){
            result.rejectValue("email", "AlreadyExists", "This email is already connected to another user!");
        }
        if(result.hasErrors()){
            return null;
        }

        String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashed);
        return newUser;
    }

    public User login(LoginUser newLoginObject, BindingResult result) {
        User user = null;
        Optional<User> potentialUser = userRepository.findByEmail(newLoginObject.getLoginEmail());
        if(!potentialUser.isEmpty()){
            user = potentialUser.get();
            if(!BCrypt.checkpw(newLoginObject.getLoginPassword(), user.getPassword())) {
                result.rejectValue("loginPassword", "Matches", "Invalid Password!");
            }
        }
        else{
            result.rejectValue("loginEmail", "NonExistent", "This email is not registered!");
        }

        if(result.hasErrors()){
            return null;
        }
        return user;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User createUser(User b) {
        return userRepository.save(b);
    }

    public User findUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }
    }

    public User updateUser(User b) {
        return userRepository.save(b);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
