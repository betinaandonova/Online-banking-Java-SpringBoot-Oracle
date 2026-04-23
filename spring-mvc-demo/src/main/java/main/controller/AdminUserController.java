package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.repository.ClientRepository;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import main.model.Client;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for admin user management pages.
 *
 * Responsibilities:
 * - Display all online banking users
 * - Provide user data to admin UI
 *
 * Access:
 * - Only available to admin users
 */

@Controller
public class AdminUserController {

    private final OnlineBankingUserService userService;
    private final ClientRepository clientRepository;

    public AdminUserController(OnlineBankingUserService userService,
                               ClientRepository clientRepository) {
        this.userService = userService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = userService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("users", userService.findAll());
        return "admin-users";
    }

    @GetMapping("/admin/users/add")
    public String showAddUserPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = userService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isAdmin(user)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", userService.getClientsWithoutUser());

        return "admin-add-user";
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam Long clientId,
                          @RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser loggedUser = userService.findById(userId).orElse(null);

        if (loggedUser == null || !AuthUtil.isAdmin(loggedUser)) {
            return "redirect:/login";
        }

        try {
            userService.createClientUser(clientId, username, password);

            redirectAttributes.addFlashAttribute("successMessage", "User added successfully.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while adding user.");
            return "redirect:/admin/users/add";
        }
    }
}