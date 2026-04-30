package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.service.CurrencyTypeService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                              HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        currencyService.insertCurrency(currencyShort, currency);

        return "redirect:/admin/currencies";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurrency(@PathVariable Long id,
                                 HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        currencyService.deleteCurrency(id);

        return "redirect:/admin/currencies";
    }
}