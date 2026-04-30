package main.controller.auth;

import jakarta.servlet.http.HttpSession;
import main.model.City;
import main.model.Client;
import main.model.OnlineBankingUser;
import main.repository.CityRepository;
import main.repository.ClientRepository;
import main.repository.OnlineBankingUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RegisterController {

    private final OnlineBankingUserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CityRepository cityRepository;

    public RegisterController(OnlineBankingUserRepository userRepository,
                              ClientRepository clientRepository,
                              CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/register")
    public String showClientForm(Model model) {
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);

        return "register-client";
    }

    @PostMapping("/register-step1")
    public String handleClientData(@RequestParam String name,
                                   @RequestParam String lastName,
                                   @RequestParam String egn,
                                   @RequestParam String phoneNumber,
                                   @RequestParam String address,
                                   @RequestParam Long cityId,
                                   HttpSession session) {

        // 1. ВАЛИДАЦИИ
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/register?error=name";
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            return "redirect:/register?error=lastname";
        }

        if (egn == null || !egn.matches("\\d{10}")) {
            return "redirect:/register?error=egn";
        }

        // 🔴 2. ПРОВЕРКА ЗА СЪЩЕСТВУВАЩ CLIENT (ТУК ГО СЛАГАШ)
        Client existingClient = clientRepository.findByEgn(egn).orElse(null);

        if (existingClient != null) {
            session.setAttribute("tempClient", existingClient);
            return "redirect:/register-userpass";
        }

        // 3. ВЗИМАШ CITY
        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            return "redirect:/register?error=city";
        }

        // 4. СЪЗДАВАШ НОВ CLIENT
        Client client = new Client();
        client.setName(name.trim());
        client.setLastName(lastName.trim());
        client.setEgn(egn.trim());
        client.setPhoneNumber(phoneNumber.trim());
        client.setAddress(address.trim());
        client.setCity(city);

        // 5. СЛАГАШ В SESSION
        session.setAttribute("tempClient", client);

        // 6. ОТИВАШ НА ВТОРА СТЪПКА
        return "redirect:/register-userpass";
    }

    @GetMapping("/register-userpass")
    public String showUserPassForm(HttpSession session) {
        if (session.getAttribute("tempClient") == null) {
            return "redirect:/register";
        }

        return "register-userpass";
    }

    @PostMapping("/register-userpass")
    public String completeRegister(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String repeatPassword,
                                   HttpSession session) {

        Client client = (Client) session.getAttribute("tempClient");

        if (client == null) {
            return "redirect:/register";
        }

        if (username == null || username.trim().isEmpty()) {
            return "redirect:/register-userpass?error=usernameEmpty";
        }

        if (password == null || password.trim().isEmpty()) {
            return "redirect:/register-userpass?error=passwordEmpty";
        }

        if (!password.equals(repeatPassword)) {
            return "redirect:/register-userpass?error=passwordMismatch";
        }

        OnlineBankingUser existingUser = userRepository.findByUsername(username.trim()).orElse(null);
        if (existingUser != null) {
            return "redirect:/register-userpass?error=usernameTaken";
        }


        Client savedClient = clientRepository.save(client);

        OnlineBankingUser user = new OnlineBankingUser();
        user.setClient(savedClient);
        user.setUsername(username.trim());
        user.setPasswordHash(password);

        userRepository.save(user);

        session.removeAttribute("tempClient");

        return "redirect:/login?success=registered";
    }
}