package org.alphatrack.screensociety.controllers.mvc;

import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.services.contracts.UserService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public String userView (@PathVariable Long id, Model model){

        model.addAttribute("register", modelMapper.userToUserDto(userService.getUserById(id)));

        return "UserView";
    }

    @GetMapping("/{id}/update")
    public String getUpdateView(@PathVariable Long id,Model model){

        model.addAttribute("user", modelMapper.userToUserUpdateDto(userService.getUserById(id)));

        return "UserUpdateView";
    }

    @PostMapping("/{id}/update")
    public String handleUpdate(@PathVariable Long id, @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                               BindingResult bindingResult, @AuthenticationPrincipal User currentUser){

        if (bindingResult.hasErrors()){
            return "UserUpdateView";
        }

        userService.updateProfile(userUpdateDto, currentUser,id);

        return "redirect:/users/" + id;
    }




}
