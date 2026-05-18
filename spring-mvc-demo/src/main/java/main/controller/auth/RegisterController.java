package main.controller.auth;

import main.model.City;
import main.model.Client;
import main.repository.CityRepository;
import main.repository.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegisterController {

    private final ClientRepository clientRepository;
    private final CityRepository cityRepository;

    public RegisterController(ClientRepository clientRepository,
                              CityRepository cityRepository) {
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
                                   @RequestParam Long cityId) {

        if (name == null || name.trim().isEmpty()) {
            return "redirect:/register?error=name";
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            return "redirect:/register?error=lastname";
        }

        if (egn == null || !egn.trim().matches("\\d{10}")) {
            return "redirect:/register?error=egn";
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "redirect:/register?error=phone";
        }

        if (address == null || address.trim().isEmpty()) {
            return "redirect:/register?error=address";
        }

        if (clientRepository.findByEgn(egn.trim()).isPresent()) {
            return "redirect:/register?error=egnExists";
        }

        if (clientRepository.findByPhoneNumber(phoneNumber.trim()).isPresent()) {
            return "redirect:/register?error=phoneExists";
        }

        City city = cityRepository.findById(cityId).orElse(null);

        if (city == null) {
            return "redirect:/register?error=city";
        }

        Client client = new Client();
        client.setName(name.trim());
        client.setLastName(lastName.trim());
        client.setEgn(egn.trim());
        client.setPhoneNumber(phoneNumber.trim());
        client.setAddress(address.trim());
        client.setCity(city);

        clientRepository.save(client);

        return "redirect:/login?success=requestSent";
    }
}