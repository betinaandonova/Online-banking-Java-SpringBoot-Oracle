package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.ClientService;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller for admin client management pages.
 *
 * Responsibilities:
 * - Display all clients
 * - Provide client data to admin UI
 *
 * Access:
 * - Only available to admin users
 */


@Controller
public class AdminClientController {

    private final ClientService clientService;
    private final OnlineBankingUserService userService;

    public AdminClientController(ClientService clientService,
                                 OnlineBankingUserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @GetMapping("/admin/clients")
    public String clientsPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = userService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", clientService.findAll());
        return "admin-clients";
    }
}