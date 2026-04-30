package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for admin user management pages.
 * Responsibilities:
 * - Display all online banking users
 * - Provide user data to admin UI
 * Access:
 * - Only available to admin users
 */

@Controller
public class UserController extends BaseAdminController {

    public UserController(OnlineBankingUserService userService) {
        super(userService);
    }

    @GetMapping("/admin/users")
    public String getAllUsers(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("users", userService.findAll());
        return "admin-users";
    }

    @GetMapping("/admin/users/add")
    public String showAddUserPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
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
                          Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            userService.createClientUser(
                    clientId,
                    username.trim(),
                    password.trim()
            );

            return "redirect:/admin/users";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при създаване на потребител.");
            model.addAttribute("clients", userService.getClientsWithoutUser());

            return "admin-add-user";
        }
    }


    @PostMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @RequestParam String username,
                           @RequestParam(required = false) String password,
                           HttpSession session,
                           Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        OnlineBankingUser userToEdit = userService.findById(id).orElse(null);

        if (userToEdit == null) {
            model.addAttribute("errorMessage", "User not found.");
            return "admin-users";
        }

        try {
            String passwordToSave = userToEdit.getPasswordHash();

            if (password != null && !password.trim().isEmpty()) {
                passwordToSave = password.trim();
            }

            userService.updateOnlineUser(
                    id,
                    username.trim(),
                    passwordToSave
            );

            return "redirect:/admin/users";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при редакция на потребителя.");
            model.addAttribute("userToEdit", userToEdit);

            return "admin-users";
        }
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             HttpSession session,
                             Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            userService.deleteOnlineUser(id);
            return "redirect:/admin/users";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Грешка при изтриване.");
            model.addAttribute("users", userService.findAll());

            return "admin-users";
        }
    }
}
