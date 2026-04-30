package main.controller.admin;

import jakarta.servlet.http.HttpSession;

import main.service.CountryService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CountryController extends BaseAdminController {

    private final CountryService countryService;

    public CountryController(CountryService countryService,
                             OnlineBankingUserService userService) {
        super(userService);
        this.countryService = countryService;
    }

    @GetMapping("/admin/countries")
    public String getCountries(@RequestParam(required = false) String searchValue,
                               HttpSession session,
                               Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("countries", countryService.getCountries(searchValue));
        model.addAttribute("searchValue", searchValue);

        return "admin-countries";
    }

    @PostMapping("/admin/countries/add")
    public String addCountry(@RequestParam String countryName,
                             HttpSession session,
                             Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.insertCountry(countryName);
            return "redirect:/admin/countries";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при добавяне на държава.");
            model.addAttribute("countries", countryService.getCountries(null));
            model.addAttribute("searchValue", null);

            return "admin-countries";
        }
    }

    @PostMapping("/admin/countries/edit/{id}")
    public String editCountry(@PathVariable Long id,
                              @RequestParam String countryName,
                              HttpSession session,
                              Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.updateCountry(id, countryName);
            return "redirect:/admin/countries";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при редакция на държава.");
            model.addAttribute("countries", countryService.getCountries(null));
            model.addAttribute("searchValue", null);

            return "admin-countries";
        }
    }

    @GetMapping("/admin/countries/delete/{id}")
    public String deleteCountry(@PathVariable Long id,
                                HttpSession session,
                                Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.deleteCountry(id);
            return "redirect:/admin/countries";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при изтриване на държава.");
            model.addAttribute("countries", countryService.getCountries(null));
            model.addAttribute("searchValue", null);

            return "admin-countries";
        }
    }
}