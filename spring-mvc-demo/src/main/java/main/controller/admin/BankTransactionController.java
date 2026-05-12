package main.controller.admin;

import main.repository.BankTransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bank-transactions")
public class BankTransactionController {

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionController(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @GetMapping
    public String showBankTransactions(Model model) {
        model.addAttribute("bankTransactions", bankTransactionRepository.findAll());
        return "admin-bank-transactions";
    }

    @PostMapping("/delete/{id}")
    public String deleteBankTransaction(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            bankTransactionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bank transaction deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not delete bank transaction: " + e.getMessage()
            );
        }

        return "redirect:/admin/bank-transactions";
    }
}