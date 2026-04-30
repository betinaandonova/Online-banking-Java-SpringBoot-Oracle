package main.controller.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.servlet.http.HttpSession;
import main.repository.CurrencyTypeRepository;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/currencies")
public class CurrencyController extends BaseAdminController {

    private final CurrencyTypeRepository currencyTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CurrencyController(CurrencyTypeRepository currencyTypeRepository,
                              OnlineBankingUserService userService) {
        super(userService);
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @GetMapping
    public String showCurrencies(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        loadCurrenciesPage(model, null);
        return "admin-currencies";
    }

    @PostMapping("/add")
    public String addCurrency(@RequestParam String currencyShort,
                              @RequestParam String currency,
                              HttpSession session,
                              Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            executeCurrencyInsert(currencyShort, currency);
            return "redirect:/admin/currencies";

        } catch (Exception e) {
            loadCurrenciesPage(model, "Грешка при добавяне на валута.");
            return "admin-currencies";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCurrency(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            executeCurrencyDelete(id);
            return "redirect:/admin/currencies";

        } catch (Exception e) {
            loadCurrenciesPage(model, "Грешка при изтриване на валута.");
            return "admin-currencies";
        }
    }

    private void loadCurrenciesPage(Model model, String errorMessage) {
        model.addAttribute("currencies", currencyTypeRepository.findAll());

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }

    private void executeCurrencyInsert(String currencyShort, String currency) {
        StoredProcedureQuery query = createProcedure("CURRENCY_INS");

        query.registerStoredProcedureParameter("p_currency_short", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_currency", String.class, ParameterMode.IN);

        query.setParameter("p_currency_short", currencyShort);
        query.setParameter("p_currency", currency);

        query.execute();
    }

    private void executeCurrencyDelete(Long id) {
        StoredProcedureQuery query = createProcedure("CURRENCY_DEL");

        query.registerStoredProcedureParameter("p_currency_id", Long.class, ParameterMode.IN);
        query.setParameter("p_currency_id", id);

        query.execute();
    }


    private StoredProcedureQuery createProcedure(String procedureName) {
        return entityManager.createStoredProcedureQuery(procedureName);
    }
}