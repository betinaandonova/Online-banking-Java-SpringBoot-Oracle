package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.service.ClientService;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import main.service.CityService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for admin client management pages.
 * Responsibilities:
 * - Display all clients
 * - Provide client data to admin UI
 * Access:
 * - Only available to admin users
 */


@Controller
public class ClientController extends BaseAdminController {

    private final ClientService clientService;
    private final CityService cityService;

    public ClientController(ClientService clientService,
                            OnlineBankingUserService userService,
                            CityService cityService) {
        super(userService);
        this.clientService = clientService;
        this.cityService = cityService;
    }

    @GetMapping("/admin/clients")
    public String clientsPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", clientService.getAdminClients());
        model.addAttribute("cities", cityService.findAll());

        return "admin-clients";
    }

    @PostMapping("/admin/clients/edit/{id}")
    public String updateClient(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam String lastName,
                               @RequestParam String phoneNumber,
                               @RequestParam String address,
                               @RequestParam Long cityId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            clientService.updateClient(id, name, lastName, phoneNumber, address, cityId);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Клиентът беше редактиран успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/clients";
    }


    @GetMapping("/admin/clients/add")
    public String addClientPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("cities", cityService.findAll());

        return "admin-add-client";
    }

    @PostMapping("/admin/clients/add")
    public String addClient(@RequestParam String name,
                            @RequestParam String lastName,
                            @RequestParam String egn,
                            @RequestParam String phoneNumber,
                            @RequestParam String address,
                            @RequestParam Long cityId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            clientService.insertClient(name, lastName, egn, phoneNumber, address, cityId);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Клиентът беше добавен успешно."
            );

            return "redirect:/admin/clients";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "redirect:/admin/clients/add";
        }
    }

    @PostMapping("/admin/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            clientService.deleteClient(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Клиентът беше изтрит успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/clients";
    }
}
