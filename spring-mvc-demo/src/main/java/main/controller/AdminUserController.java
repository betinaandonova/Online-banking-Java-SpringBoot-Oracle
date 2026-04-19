package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUserController {

    private final OnlineBankingUserService userService;

    public AdminUserController(OnlineBankingUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(HttpSession session, Model model) {
        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("users", userService.findAll());
        return "admin-users";
    }
}