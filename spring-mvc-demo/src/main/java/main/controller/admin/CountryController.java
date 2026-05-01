package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
                             RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.insertCountry(countryName);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Държавата беше добавена успешно."
            );

            return "redirect:/admin/countries";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "redirect:/admin/countries";
        }
    }

    @PostMapping("/admin/countries/edit/{id}")
    public String editCountry(@PathVariable Long id,
                              @RequestParam String countryName,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.updateCountry(id, countryName);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Държавата беше редактирана успешно."
            );

            return "redirect:/admin/countries";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Грешка при редакция на държава."
            );

            return "redirect:/admin/countries";
        }
    }

    @PostMapping("/admin/countries/delete/{id}")
    public String deleteCountry(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            countryService.deleteCountry(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Държавата беше изтрита успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Държавата не може да бъде изтрита, защото се използва от град."
            );
        }

        return "redirect:/admin/countries";
    }
}