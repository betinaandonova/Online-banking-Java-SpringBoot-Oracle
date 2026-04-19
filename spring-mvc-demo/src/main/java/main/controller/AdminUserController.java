package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for admin user management pages.
 *
 * Responsibilities:
 * - Display all online banking users
 * - Provide user data to admin UI
 *
 * Access:
 * - Only available to admin users
 */

@Controller
public class AdminUserController {

    private final OnlineBankingUserService userService;

    public AdminUserController(OnlineBankingUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = userService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("users", userService.findAll());
        return "admin-users";
    }
}