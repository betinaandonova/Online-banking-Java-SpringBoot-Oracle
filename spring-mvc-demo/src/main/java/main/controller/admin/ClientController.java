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
    public String editClient(@PathVariable Long id,
                             @RequestParam String name,
                             @RequestParam String lastName,
                             @RequestParam String phoneNumber,
                             @RequestParam String address,
                             @RequestParam Long cityId) {

        clientService.updateClient(id, name, lastName, phoneNumber, address, cityId);

        return "redirect:/admin/clients";
    }

    @GetMapping("/admin/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
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
                            Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        if (name.isBlank() || lastName.isBlank() || egn.isBlank()
                || phoneNumber.isBlank() || address.isBlank()) {

            model.addAttribute("errorMessage", "Всички полета са задължителни.");
            model.addAttribute("cities", cityService.findAll());

            return "admin-add-client";
        }

        try {
            clientService.insertClient(name, lastName, egn, phoneNumber, address, cityId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Вече съществува клиент с това ЕГН или телефонен номер.");
            model.addAttribute("cities", cityService.findAll());

            return "admin-add-client";
        }

        return "redirect:/admin/clients";
    }
}
