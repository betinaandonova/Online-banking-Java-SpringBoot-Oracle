package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.dto.UserProfileResponse;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientProfileController {

    private final OnlineBankingUserService onlineBankingUserService;

    public ClientProfileController(OnlineBankingUserService onlineBankingUserService) {
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/profile")
    public String showUserProfile(HttpSession session, Model model) {

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

        return "user-profile";
    }

    @GetMapping("/profile/change-password")
    public String showChangePasswordPage(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        return "change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            HttpSession session,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        try {
            onlineBankingUserService.changePassword(userId, oldPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Паролата е сменена успешно.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/change-password";
    }
}