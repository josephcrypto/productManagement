package cn.coding.com.productmanagement.controller;

import cn.coding.com.productmanagement.dto.UserRegistrationDTO;
import cn.coding.com.productmanagement.model.User;
import cn.coding.com.productmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

    public @ModelAttribute("user") UserRegistrationDTO userRegistrationDTO() {
        return new UserRegistrationDTO();
    }

    @GetMapping(value = "/login")
    public String showRegistrationForm(Model model) {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDTO userDto,
                                      BindingResult result, Model model) {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with this email!");
        }
        if (result.hasErrors()) {
            return "login";
        }
        userService.save(userDto);
        return "redirect:/login?success";
    }
}
