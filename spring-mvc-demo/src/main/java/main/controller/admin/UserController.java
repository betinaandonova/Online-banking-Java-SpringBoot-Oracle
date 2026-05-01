package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.service.OnlineBankingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController extends BaseAdminController {

    private final OnlineBankingUserService onlineBankingUserService;

    public UserController(OnlineBankingUserService onlineBankingUserService) {
        super(onlineBankingUserService);
        this.onlineBankingUserService = onlineBankingUserService;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("users", onlineBankingUserService.findAll());
        return "admin-users";
    }

    @GetMapping("/admin/users/add")
    public String showAddUserPage(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("clients", onlineBankingUserService.getClientsWithoutUser());
        return "admin-add-user";
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam(required = false) Long clientId,
                          @RequestParam(required = false) Long employeeId,
                          @RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            onlineBankingUserService.insertOnlineUser(clientId, employeeId, username, password);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Потребителят беше добавен успешно."
            );

            return "redirect:/admin/users";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "redirect:/admin/users/add";
        }
    }

    @PostMapping("/admin/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            onlineBankingUserService.updateOnlineUser(id, username, password);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Потребителят беше редактиран успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/login";
        }

        try {
            onlineBankingUserService.deleteOnlineUser(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Потребителят беше изтрит успешно."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/admin/users";
    }
}
