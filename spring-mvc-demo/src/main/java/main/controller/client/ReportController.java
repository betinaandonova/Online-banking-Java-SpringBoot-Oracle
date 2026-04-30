package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for admin reports pages.
 *
 * Responsibilities:
 * - Provide access to reporting functionalities
 * - Serve report views for admin users
 *
 * Access:
 * - Only available to admin users
 */

@Controller
public class ReportController {

    private final OnlineBankingUserService onlineBankingUserService;

    public ReportController(OnlineBankingUserService onlineBankingUserService) {
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/admin/reports")
    public String adminReports(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        return "admin-reports";
    }
}