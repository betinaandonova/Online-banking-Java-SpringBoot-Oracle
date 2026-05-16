package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.model.Account;
import main.model.OnlineBankingUser;
import main.repository.AccountRepository;
import main.repository.OnlineBankingUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClientAccountController {

    private final AccountRepository accountRepository;
    private final OnlineBankingUserRepository onlineBankingUserRepository;

    public ClientAccountController(
            AccountRepository accountRepository,
            OnlineBankingUserRepository onlineBankingUserRepository
    ) {
        this.accountRepository = accountRepository;
        this.onlineBankingUserRepository = onlineBankingUserRepository;
    }

    @GetMapping("/accounts")
    public String showMyAccounts(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getClient() == null) {
            throw new RuntimeException("This user is not a client.");
        }

        List<Account> accounts = accountRepository.findByClientId(user.getClient().getId());

        model.addAttribute("accounts", accounts);
        model.addAttribute("activePage", "accounts");

        return "client-accounts";
    }
}