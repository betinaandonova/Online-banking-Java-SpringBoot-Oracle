package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.model.Account;
import main.model.OnlineBankingUser;
import main.service.AccountService;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientTransactionController {

    private final OnlineBankingUserService onlineBankingUserService;
    private final AccountService accountService;

    public ClientTransactionController(
            OnlineBankingUserService onlineBankingUserService,
            AccountService accountService
    ) {
        this.onlineBankingUserService = onlineBankingUserService;
        this.accountService = accountService;
    }

    @GetMapping("/client/transactions")
    public String showLastTransactions(
            @RequestParam(required = false) Long accountId,
            HttpSession session,
            Model model
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        Long clientId = user.getClient().getId();

        model.addAttribute("accounts", accountService.findByClientId(clientId));

        if (accountId != null) {
            Account selectedAccount = accountService.findById(accountId).orElse(null);

            if (selectedAccount == null ||
                    selectedAccount.getClient() == null ||
                    !selectedAccount.getClient().getId().equals(clientId)) {
                model.addAttribute("errorMessage", "Нямате право да преглеждате тази сметка.");
                return "client-transactions";
            }

            model.addAttribute("selectedAccountId", accountId);
            model.addAttribute("movements", accountService.findLast30MovementsByAccountId(accountId));
        }

        return "client-transactions";
    }
}