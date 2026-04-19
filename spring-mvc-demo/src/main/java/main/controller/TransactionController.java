package main.controller;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.AccountService;
import main.service.BankTransactionService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for admin transaction management pages.
 *
 * Responsibilities:
 * - Display transactions for a specific account
 * - Provide transaction data to admin UI
 *
 * Access:
 * - Only available to admin users
 *
 * Notes:
 * - Transactions are filtered by accountId
 * - Used for auditing and monitoring account activity
 */

/**
 * Loads transactions for a specific account.
 *
 * @parameter accountId the ID of the account
 */

@Controller
public class TransactionController {

    private final BankTransactionService bankTransactionService;
    private final AccountService accountService;

    public TransactionController(BankTransactionService bankTransactionService,
                                 AccountService accountService) {
        this.bankTransactionService = bankTransactionService;
        this.accountService = accountService;
    }

    @GetMapping("/transactions")
    public String clientTransactions(@RequestParam Long accountId,
                                     HttpSession session,
                                     Model model) {

        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        // проверка дали сметката е на този клиент
        boolean ownsAccount = accountService
                .findByClientId(user.getClient().getId())
                .stream()
                .anyMatch(acc -> acc.getId().equals(accountId));

        if (!ownsAccount) {
            return "redirect:/home";
        }

        model.addAttribute("transactions",
                bankTransactionService.findByAccountId(accountId));

        return "transactions";
    }
}