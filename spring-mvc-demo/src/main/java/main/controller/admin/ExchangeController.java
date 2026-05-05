package main.controller.admin;

import main.model.CurrencyType;
import main.model.ExchangeRate;
import main.repository.CurrencyTypeRepository;
import main.repository.ExchangeRateRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/exchange-rates")
public class ExchangeController {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyTypeRepository currencyTypeRepository;

    public ExchangeController(
            ExchangeRateRepository exchangeRateRepository,
            CurrencyTypeRepository currencyTypeRepository
    ) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @GetMapping
    public String showExchangeRates(Model model) {
        model.addAttribute("exchangeRates", exchangeRateRepository.findAll());
        model.addAttribute("currencies", currencyTypeRepository.findAll());

        return "admin-exchange-rate";
    }

    @PostMapping("/add")
    public String addExchangeRate(
            @RequestParam Long currencyId,
            @RequestParam BigDecimal rateToEur,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rateDate,
            RedirectAttributes redirectAttributes
    ) {
        try {
            CurrencyType currency = currencyTypeRepository.findById(currencyId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid currency."));

            if ("EUR".equalsIgnoreCase(currency.getCurrencyShort())) {
                redirectAttributes.addFlashAttribute("errorMessage", "EUR cannot be selected.");
                return "redirect:/admin/exchange-rates";
            }

            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(currency);
            exchangeRate.setRateToEur(rateToEur);
            exchangeRate.setRateDate(rateDate != null ? rateDate : LocalDate.now());

            exchangeRateRepository.save(exchangeRate);

            redirectAttributes.addFlashAttribute("successMessage", "Exchange rate added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not add exchange rate: " + e.getMessage()
            );
        }

        return "redirect:/admin/exchange-rates";
    }

    @PostMapping("/delete/{id}")
    public String deleteExchangeRate(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            exchangeRateRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Exchange rate deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete exchange rate.");
        }

        return "redirect:/admin/exchange-rates";
    }
}