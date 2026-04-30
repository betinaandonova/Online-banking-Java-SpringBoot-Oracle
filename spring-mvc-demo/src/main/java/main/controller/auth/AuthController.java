package main.controller.auth;
import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.repository.OnlineBankingUserRepository;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Handles authentication logic.
 * Responsibilities:
 * - Login (GET/POST)
 * - Logout
 * - Session management
 * Determines user role (admin or client) and redirects accordingly.
 */


@Controller
public class AuthController {
    private final OnlineBankingUserRepository userRepository;

    public AuthController(OnlineBankingUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {

        OnlineBankingUser user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return "redirect:/login?error=user";
        }

        if (!user.getPasswordHash().equals(password)) {
            return "redirect:/login?error=pass";
        }

        session.setAttribute("userId", user.getId());

        if (AuthUtil.isAdmin(user)) {
            return "redirect:/admin/home";
        }

        if (AuthUtil.isClient(user)) {
            return "redirect:/home";
        }

        return "redirect:/login?error=role";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
