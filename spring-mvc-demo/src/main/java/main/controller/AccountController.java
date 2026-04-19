package main.controller;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.AccountService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller for admin account management pages.
 *
 * Responsibilities:
 * - Display all bank accounts in the system
 * - Provide account data to admin UI
 *
 * Access:
 * - Only available to admin users
 *
 * Notes:
 * - Used by admin to inspect client accounts
 * - Acts as entry point to view account transactions
 */

/**
 * Loads all accounts for admin view.
 */

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/admin/accounts")
    public String adminAccounts(HttpSession session, Model model) {
        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("accounts", accountService.findAll());
        return "admin-accounts";
    }
}
