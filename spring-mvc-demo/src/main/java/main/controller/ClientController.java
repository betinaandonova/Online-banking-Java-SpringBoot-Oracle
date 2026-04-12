package main.controller;

import main.model.City;
import main.model.Client;
import main.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/by-city")
    public ResponseEntity<List<Client>> findByCityId(@RequestParam Long cityId) {
        return ResponseEntity.ok(clientService.findByCityId(cityId));
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<List<Client>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(clientService.findByNameContaining(name));
    }

    @GetMapping("/search/by-last-name")
    public ResponseEntity<List<Client>> findByLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(clientService.findByLastNameContaining(lastName));
    }

    @GetMapping("/search/by-egn")
    public ResponseEntity<Client> findByEgn(@RequestParam String egn) {
        return ResponseEntity.ok(clientService.findByEgn(egn));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestParam String name,
                                         @RequestParam String lastName,
                                         @RequestParam String egn,
                                         @RequestParam String phoneNumber,
                                         @RequestParam String address,
                                         @RequestParam Long cityId) {
        clientService.insertClient(name, lastName, egn, phoneNumber, address, cityId);
        return ResponseEntity.ok("Client created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestParam String name,
                                         @RequestParam String lastName,
                                         @RequestParam String phoneNumber,
                                         @RequestParam String address,
                                         @RequestParam Long cityId) {
        clientService.updateClient(id, name, lastName, phoneNumber, address, cityId);
        return ResponseEntity.ok("Client updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully.");
    }
}