package com.frontend.user.controller.web;

import com.frontend.user.dto.UserDTO;
import com.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;

@Controller
public class WebController {
    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {

        LocalTime now = LocalTime.now();
        String greeting;
        if(now.isBefore(LocalTime.NOON)){
            greeting="greeting.morning";
        }else if(now.isBefore(LocalTime.of(18,0))){
            greeting="greeting.afternoon";
        }else{
            greeting="greeting.evening";
        }
        model.addAttribute("timeOfDayGreeting", greeting);
        return "usersFolder/index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "usersFolder/users";
    }

    @GetMapping("/users/{id}")
    public String getUserByID(Model model, @PathVariable Long id) {
        UserDTO user = userService.findById(id);
        if(user == null){
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        return "usersFolder/user";
    }
}
