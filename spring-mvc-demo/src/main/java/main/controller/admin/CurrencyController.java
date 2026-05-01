package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.service.CurrencyTypeService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/currencies")
public class CurrencyController extends BaseAdminController {

    private final CurrencyTypeService currencyService;

    public CurrencyController(CurrencyTypeService currencyService,
                              OnlineBankingUserService userService) {
        super(userService);
        this.currencyService = currencyService;
    }

    @GetMapping
    public String showCurrencies(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("currencies", currencyService.findAll());

        return "admin-currencies";
    }

    @PostMapping("/add")
    public String addCurrency(@RequestParam String currencyShort,
                              @RequestParam String currency,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            currencyService.insertCurrency(currencyShort, currency);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Валутата беше добавена успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/currencies";
    }

    @PostMapping("/delete/{id}")
    public String deleteCurrency(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            currencyService.deleteCurrency(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Валутата беше изтрита успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Валутата не може да бъде изтрита, защото се използва."
            );
        }

        return "redirect:/admin/currencies";
    }
}