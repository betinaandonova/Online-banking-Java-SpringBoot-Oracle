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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/admin/users/edit/{id}")
    public String showEditUserPage(@PathVariable Long id,
                                   HttpSession session,
                                   Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser loggedUser = userService.findById(userId).orElse(null);

        if (loggedUser == null || !AuthUtil.isAdmin(loggedUser)) {
            return "redirect:/login";
        }

        OnlineBankingUser userToEdit = userService.findById(id).orElse(null);

        if (userToEdit == null) {
            return "redirect:/admin/users";
        }

        model.addAttribute("userToEdit", userToEdit);

        return "admin-edit-user";
    }

    @PostMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @RequestParam String username,
                           @RequestParam(required = false) String password,
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
            OnlineBankingUser userToEdit = userService.findById(id).orElse(null);

            if (userToEdit == null) {
                return "redirect:/admin/users";
            }

            Long clientId = null;
            Long employeeId = null;

            if (userToEdit.getClient() != null) {
                clientId = userToEdit.getClient().getId();
            }

            if (userToEdit.getEmployee() != null) {
                employeeId = userToEdit.getEmployee().getId();
            }

            String passwordToSave;

            if (password == null || password.trim().isEmpty()) {
                passwordToSave = userToEdit.getPasswordHash();
            } else {
                passwordToSave = password.trim();
            }

            userService.updateOnlineUser(id, username.trim(), passwordToSave);

            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
            return "redirect:/admin/users";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while updating user.");
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
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
            userService.deleteOnlineUser(id);

            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while deleting user.");
        }

        return "redirect:/admin/users";
    }
}