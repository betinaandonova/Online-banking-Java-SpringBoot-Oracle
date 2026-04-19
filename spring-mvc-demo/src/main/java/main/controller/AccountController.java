package main.controller;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.AccountService;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller for admin account management pages.
 *
 * Responsibilities:
 * - Display all bank accounts
 * - Provide account data to admin UI
 *
 * Access:
 * - Only available to admin users
 */

@Controller
public class AccountController {

    private final AccountService accountService;
    private final OnlineBankingUserService onlineBankingUserService;

    public AccountController(AccountService accountService, OnlineBankingUserService onlineBankingUserService) {
        this.accountService = accountService;
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/admin/accounts")
    public String adminAccounts(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("accounts", accountService.findAll());
        return "admin-accounts";
    }
}
