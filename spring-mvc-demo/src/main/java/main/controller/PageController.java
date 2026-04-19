package main.controller;

import jakarta.servlet.http.HttpSession;
import main.dto.UserProfileResponse;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles navigation to main application pages.
 *
 * Responsibilities:
 * - Client home page
 * - Admin home page
 *
 * Performs access control based on user role.
 */

@Controller
public class PageController {

    private final OnlineBankingUserService onlineBankingUserService;

    public PageController(OnlineBankingUserService onlineBankingUserService) {
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/home")
    public String clientHome(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        UserProfileResponse profile = onlineBankingUserService.getUserProfile(userId);
        model.addAttribute("profile", profile);

        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        UserProfileResponse profile = onlineBankingUserService.getUserProfile(userId);
        model.addAttribute("profile", profile);

        return "admin-home";
    }
}