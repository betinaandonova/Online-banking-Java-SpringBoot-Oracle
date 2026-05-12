package main.controller.admin;

import main.model.TransactionType;
import main.repository.TransactionTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/transaction-types")
public class TransactionTypeController {

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeController(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @GetMapping
    public String showTransactionTypes(Model model) {
        model.addAttribute("transactionTypes", transactionTypeRepository.findAll());
        return "admin-transaction-types";
    }

    @PostMapping("/add")
    public String addTransactionType(
            @RequestParam String transactionTypeName,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (transactionTypeName == null || transactionTypeName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Transaction type name is required.");
                return "redirect:/admin/transaction-types";
            }

            TransactionType transactionType = new TransactionType();
            transactionType.setTransactionTypeName(transactionTypeName.trim());

            transactionTypeRepository.save(transactionType);

            redirectAttributes.addFlashAttribute("successMessage", "Transaction type added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not add transaction type: " + e.getMessage()
            );
        }

        return "redirect:/admin/transaction-types";
    }

    @PostMapping("/update/{id}")
    public String updateTransactionType(
            @PathVariable Long id,
            @RequestParam String transactionTypeName,
            RedirectAttributes redirectAttributes
    ) {
        try {
            TransactionType transactionType = transactionTypeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type."));

            if (transactionTypeName == null || transactionTypeName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Transaction type name is required.");
                return "redirect:/admin/transaction-types";
            }

            transactionType.setTransactionTypeName(transactionTypeName.trim());
            transactionTypeRepository.save(transactionType);

            redirectAttributes.addFlashAttribute("successMessage", "Transaction type updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not update transaction type: " + e.getMessage()
            );
        }

        return "redirect:/admin/transaction-types";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransactionType(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            transactionTypeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction type deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not delete transaction type. It may be used in existing transactions."
            );
        }

        return "redirect:/admin/transaction-types";
    }
}