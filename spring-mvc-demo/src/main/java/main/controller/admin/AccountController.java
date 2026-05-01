package main.controller.admin;
import jakarta.servlet.http.HttpSession;
import main.dto.AdminAccountResponse;
import main.enums.AccountSearchType;
import main.repository.ClientRepository;
import main.repository.CurrencyTypeRepository;
import main.service.AccountService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for admin account management pages.
 * Responsibilities:
 * - Display all bank accounts
 * - Provide account data to admin UI
 * Access:
 * - Only available to admin users
 */

@Controller
public class AccountController extends BaseAdminController {

    private final AccountService accountService;
    private final ClientRepository clientRepository;
    private final CurrencyTypeRepository currencyTypeRepository;

    public AccountController(AccountService accountService,
                             OnlineBankingUserService userService,
                             ClientRepository clientRepository,
                             CurrencyTypeRepository currencyTypeRepository) {
        super(userService);
        this.accountService = accountService;
        this.clientRepository = clientRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @GetMapping("/admin/accounts")
    public String adminAccounts(@RequestParam(required = false) AccountSearchType searchType,
                                @RequestParam(required = false) String searchValue,
                                HttpSession session,
                                Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            loadAccountsPage(model, searchType, searchValue, null);

        } catch (Exception e) {
            e.printStackTrace();

            List<AdminAccountResponse> accounts = accountService.searchAccounts(null, null);

            model.addAttribute("accounts", accounts);
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
            model.addAttribute("searchTypes", AccountSearchType.values());
            model.addAttribute(
                    "errorMessage",
                    "Грешка при търсене на сметки. Проверете избрания филтър или въведената стойност."
            );
        }

        return "admin-accounts";
    }

    @GetMapping("/admin/accounts/add")
    public String showAddAccountForm(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        loadAccountForm(model, null);

        return "admin-account-add";
    }

    @PostMapping("/admin/accounts/add")
    public String addAccount(@RequestParam Long clientId,
                             @RequestParam Long currencyId,
                             @RequestParam BigDecimal availability,
                             @RequestParam String iban,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            accountService.insertAccount(clientId, currencyId, availability, iban);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Сметката беше добавена успешно."
            );

            return "redirect:/admin/accounts";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "redirect:/admin/accounts/add";
        }
    }
    @PostMapping("/admin/accounts/edit/{id}")
    public String updateAvailability(@PathVariable Long id,
                                     @RequestParam BigDecimal availability,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            accountService.updateAccountAvailability(id, availability);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Сметката беше редактирана успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/accounts";
    }

    @PostMapping("/admin/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            accountService.deleteAccount(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Сметката беше изтрита успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Сметката не може да бъде изтрита, защото има свързани транзакции."
            );
        }

        return "redirect:/admin/accounts";
    }

    private void loadAccountsPage(Model model,
                                  AccountSearchType searchType,
                                  String searchValue,
                                  String errorMessage) {

        List<AdminAccountResponse> accounts =
                accountService.searchAccounts(searchType, searchValue);

        model.addAttribute("accounts", accounts);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("searchTypes", AccountSearchType.values());

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }

    private void loadAccountForm(Model model, String errorMessage) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("currencies", currencyTypeRepository.findAll());

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }
}