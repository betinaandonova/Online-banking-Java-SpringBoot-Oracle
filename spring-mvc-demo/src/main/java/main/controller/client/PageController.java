package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.dto.UserProfileResponse;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.service.TransferService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final OnlineBankingUserService onlineBankingUserService;
    private final TransferService transferService;

    public PageController(
            OnlineBankingUserService onlineBankingUserService,
            TransferService transferService
    ) {
        this.onlineBankingUserService = onlineBankingUserService;
        this.transferService = transferService;
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

        Long clientId = user.getClient().getId();

        model.addAttribute("accounts", transferService.findAccountsByClientId(clientId));

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