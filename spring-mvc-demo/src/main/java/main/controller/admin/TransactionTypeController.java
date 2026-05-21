package main.controller.admin;

import main.service.TransactionTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/transaction-types")
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;

    public TransactionTypeController(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @GetMapping
    public String showTransactionTypes(Model model) {
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        return "admin-transaction-types";
    }

    @PostMapping("/add")
    public String addTransactionType(
            @RequestParam String transactionTypeName,
            RedirectAttributes redirectAttributes
    )
    {
        try
        {
            transactionTypeService.insertTransactionType(transactionTypeName);
            redirectAttributes.addFlashAttribute("successMessage", "Типът транзакция е добавен успешно.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", getUserFriendlyErrorMessage(e, "add"));
        }

        return "redirect:/admin/transaction-types";
    }

    @PostMapping("/update/{id}")
    public String updateTransactionType(
            @PathVariable Long id,
            @RequestParam String transactionTypeName,
            RedirectAttributes redirectAttributes
    )
    {
        try
        {
            transactionTypeService.updateTransactionType(id, transactionTypeName);
            redirectAttributes.addFlashAttribute("successMessage", "Типът транзакция е редактиран успешно.");
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", getUserFriendlyErrorMessage(e, "update"));
        }

        return "redirect:/admin/transaction-types";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransactionType(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            transactionTypeService.deleteTransactionType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Типът транзакция е изтрит успешно.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", getUserFriendlyErrorMessage(e, "delete"));
        }

        return "redirect:/admin/transaction-types";
    }

    private String getUserFriendlyErrorMessage(Exception e, String operation) {
        String message = e.getMessage();

        if (message == null) {
            return "Възникна грешка при операцията.";
        }

        if (message.contains("вече съществува")) {
            return "Не може да добавите такъв тип транзакция, защото вече съществува.";
        }

        if (message.contains("същото име")) {
            return "Не може да редактирате типа транзакция, защото вече съществува друг тип със същото име.";
        }

        if (message.contains("задължително")) {
            return "Името на типа транзакция е задължително.";
        }

        if (message.contains("не може да бъде над 20 символа")) {
            return "Името на типа транзакция не може да бъде над 20 символа.";
        }

        if (message.contains("Не съществува")) {
            return "Не съществува тип транзакция с това ID.";
        }

        if (message.contains("използва")) {
            return "Не може да изтриете този тип транзакция, защото се използва в банкови транзакции.";
        }

        if ("add".equals(operation)) {
            return "Не може да добавите типа транзакция. Моля, проверете въведените данни.";
        }

        if ("update".equals(operation)) {
            return "Не може да редактирате типа транзакция. Моля, проверете въведените данни.";
        }

        if ("delete".equals(operation)) {
            return "Не може да изтриете типа транзакция.";
        }

        return "Възникна грешка при операцията.";
    }
}