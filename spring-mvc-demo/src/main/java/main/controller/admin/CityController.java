package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.service.CityService;
import main.service.CountryService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CityController extends BaseAdminController {

    private final CityService cityService;
    private final CountryService countryService;

    public CityController(CityService cityService,
                               CountryService countryService,
                               OnlineBankingUserService userService) {
        super(userService);
        this.cityService = cityService;
        this.countryService = countryService;
    }

    /**
     * Controller for admin city management pages.
     * Responsibilities:
     * - Display all cities
     * - Provide country data for city management
     * - Handle add, edit and delete operations for cities
     * Access:
     * - Only available to admin users
     */

    @GetMapping("/admin/cities")
    public String citiesPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        loadCitiesPage(model);

        return "admin-cities";
    }

    @PostMapping("/admin/cities/add")
    public String addCity(@RequestParam String cityName,
                          @RequestParam Long countryId,
                          HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        cityService.insertCity(cityName, countryId);

        return "redirect:/admin/cities";
    }

    @PostMapping("/admin/cities/edit/{id}")
    public String editCity(@PathVariable Long id,
                           @RequestParam String cityName,
                           @RequestParam Long countryId,
                           HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        cityService.updateCity(id, cityName, countryId);

        return "redirect:/admin/cities";
    }

    @GetMapping("/admin/cities/delete/{id}")
    public String deleteCity(@PathVariable Long id,
                             HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        cityService.deleteCity(id);

        return "redirect:/admin/cities";
    }

    private void loadCitiesPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("countries", countryService.findAll());
    }

    @PostMapping("/admin/cities/delete/{id}")
    public String deleteCity(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            cityService.deleteCity(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Градът беше изтрит успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Градът не може да бъде изтрит, защото се използва."
            );
        }

        return "redirect:/admin/cities";
    }
}