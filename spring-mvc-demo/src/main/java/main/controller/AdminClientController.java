package main.controller;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.ClientService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller for admin client management pages.
 *
 * Responsibilities:
 * - Display all clients
 * - Provide data to admin UI
 *
 * Access:
 * - Only available to admin users
 */


@Controller
public class AdminClientController {

    private final ClientService clientService;

    public AdminClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/admin/clients")
    public String clientsPage(HttpSession session, Model model) {
        OnlineBankingUser user = (OnlineBankingUser) session.getAttribute("user");

        if (!AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", clientService.findAll());
        return "admin-clients";
    }
}