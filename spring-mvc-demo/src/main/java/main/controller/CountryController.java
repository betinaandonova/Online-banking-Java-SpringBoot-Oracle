package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.Country;
import main.model.OnlineBankingUser;
import main.service.CountryService;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CountryController {

    private final CountryService countryService;
    private final OnlineBankingUserService onlineBankingUserService;

    public CountryController(CountryService countryService, OnlineBankingUserService onlineBankingUserService) {
        this.countryService = countryService;
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/admin/countries")
    public String getCountries(@RequestParam(required = false) String searchValue,
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

        model.addAttribute("countries", countryService.getCountries(searchValue));
        model.addAttribute("searchValue", searchValue);

        return "admin-countries";
    }

    @GetMapping("/admin/countries/add")
    public String showAddCountryPage(HttpSession session) {

        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        return "admin-country-add";
    }

    @PostMapping("/admin/countries/add")
    public String addCountry(@RequestParam String countryName,
                             HttpSession session,
                             Model model) {

        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        try {
            countryService.insertCountry(countryName);

            // ✅ SUCCESS MESSAGE
            return "redirect:/admin/countries?success=Country added successfully";

        } catch (IllegalArgumentException e) {

            // ❌ ERROR MESSAGE
            model.addAttribute("error", e.getMessage());

            return "admin-country-add"; // връща пак формата
        }
    }

    @PostMapping("/admin/countries/edit/{id}")
    public String editCountry(@PathVariable Long id,
                              @RequestParam String countryName,
                              HttpSession session) {

        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        countryService.updateCountry(id, countryName);

        return "redirect:/admin/countries";
    }

    @GetMapping("/admin/countries/delete/{id}")
    public String deleteCountry(@PathVariable Long id,
                                HttpSession session) {

        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        countryService.deleteCountry(id);

        return "redirect:/admin/countries";
    }
}