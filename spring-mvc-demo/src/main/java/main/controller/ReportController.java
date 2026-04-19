package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/admin/reports")
    public String adminReports(HttpSession session) {
        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        return "admin-reports";
    }
}