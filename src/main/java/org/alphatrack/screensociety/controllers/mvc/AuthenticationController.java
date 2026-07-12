package org.alphatrack.screensociety.controllers.mvc;


import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginView (){
        return "LoginView";
    }

    @GetMapping("/register")
    public String registerView(Model model){

        model.addAttribute("register",new UserRegistrationDto());

        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegistration (@Valid @ModelAttribute("register") UserRegistrationDto registrationDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "RegisterView";
        }

        userService.registerUser(registrationDto);

        return "redirect:/";
    }
}
