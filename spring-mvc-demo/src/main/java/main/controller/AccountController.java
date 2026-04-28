package main.controller;
import jakarta.servlet.http.HttpSession;
import main.dto.AdminAccountResponse;
import main.enums.AccountSearchType;
import main.model.Account;
import main.model.OnlineBankingUser;
import main.repository.ClientRepository;
import main.repository.CurrencyTypeRepository;
import main.service.AccountService;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.math.BigDecimal;
import java.util.List;


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
    private final ClientRepository clientRepository;
    private final CurrencyTypeRepository currencyTypeRepository;

    public AccountController(AccountService accountService, OnlineBankingUserService onlineBankingUserService, ClientRepository clientRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.accountService = accountService;
        this.onlineBankingUserService = onlineBankingUserService;
        this.clientRepository = clientRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @GetMapping("/admin/accounts")
    public String adminAccounts(@RequestParam(required = false) AccountSearchType searchType,
                                @RequestParam(required = false) String searchValue,
                                HttpSession session,
                                Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        List<AdminAccountResponse> accounts =
                accountService.searchAccounts(searchType, searchValue);

        model.addAttribute("accounts", accounts);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("searchTypes", AccountSearchType.values());

        return "admin-accounts";
    }

    @GetMapping("/admin/accounts/add")
    public String showAddAccountForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("currencies", currencyTypeRepository.findAll());

        return "admin-account-add";
    }

    @PostMapping("/admin/accounts/add")
    public String addAccount(@RequestParam Long clientId,
                             @RequestParam Long currencyId,
                             @RequestParam BigDecimal availability,
                             @RequestParam String iban,
                             HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        accountService.insertAccount(clientId, currencyId, availability, iban);

        return "redirect:/admin/accounts";
    }

    @PostMapping("/admin/accounts/edit/{id}")
    public String updateAvailability(@PathVariable Long id,
                                     @RequestParam BigDecimal availability,
                                     HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        Account account = accountService.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.updateAccountAvailability(id, availability);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        try {
            accountService.deleteAccount(id);
            return "redirect:/admin/accounts?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/accounts?error=deleteFailed";
        }
    }
}
