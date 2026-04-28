package main.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.repository.CurrencyTypeRepository;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/currencies")
public class CurrencyController {

    private final CurrencyTypeRepository currencyTypeRepository;
    private final OnlineBankingUserService onlineBankingUserService;

    @PersistenceContext
    private EntityManager entityManager;

    public CurrencyController(CurrencyTypeRepository currencyTypeRepository,
                              OnlineBankingUserService onlineBankingUserService) {
        this.currencyTypeRepository = currencyTypeRepository;
        this.onlineBankingUserService = onlineBankingUserService;
    }

    // ======================
    // VIEW ALL
    // ======================
    @GetMapping
    public String showCurrencies(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("currencies", currencyTypeRepository.findAll());

        return "admin-currencies";
    }

    // ======================
    // ADD (FORM)
    // ======================
    @GetMapping("/add")
    public String showAddForm(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        return "admin-currency-add";
    }

    // ======================
    // ADD (POST)
    // ======================
    @PostMapping("/add")
    public String addCurrency(@RequestParam String currencyShort,
                              @RequestParam String currency,
                              HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CURRENCY_INS");

        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_short", currencyShort);
        query.setParameter("p_currency", currency);

        query.execute();

        return "redirect:/admin/currencies";
    }

    // ======================
    // DELETE
    // ======================
    @GetMapping("/delete/{id}")
    public String deleteCurrency(@PathVariable Long id, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CURRENCY_DEL");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.setParameter("p_currency_id", id);

        query.execute();

        return "redirect:/admin/currencies";
    }
}