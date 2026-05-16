package main.controller.client;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.repository.CurrencyTypeRepository;
import main.service.OnlineBankingUserService;
import main.service.TransferService;
import main.util.AuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class ClientTransferController {

    private final OnlineBankingUserService onlineBankingUserService;
    private final CurrencyTypeRepository currencyTypeRepository;
    private final TransferService transferService;

    public ClientTransferController(
            OnlineBankingUserService onlineBankingUserService,
            CurrencyTypeRepository currencyTypeRepository,
            TransferService transferService
    ) {
        this.onlineBankingUserService = onlineBankingUserService;
        this.currencyTypeRepository = currencyTypeRepository;
        this.transferService = transferService;
    }

    @GetMapping("/transfers")
    public String showTransferPage(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        Long clientId = user.getClient().getId();

        model.addAttribute("accounts", transferService.findAccountsByClientId(clientId));
        model.addAttribute("currencies", currencyTypeRepository.findAll());

        return "send-money";
    }

    @PostMapping("/transfers")
    public String makeTransfer(
            @RequestParam String transferMethod,
            @RequestParam Long senderAccountId,
            @RequestParam(required = false) String receiverPhoneNumber,
            @RequestParam(required = false) String receiverIban,
            @RequestParam BigDecimal amount,
            @RequestParam Long currencyId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        OnlineBankingUser user = onlineBankingUserService.findById(userId).orElse(null);

        if (user == null || !AuthUtil.isClient(user)) {
            return "redirect:/login";
        }

        Long clientId = user.getClient().getId();

        try {
            transferService.transferMoney(
                    clientId,
                    transferMethod,
                    senderAccountId,
                    receiverPhoneNumber,
                    receiverIban,
                    amount,
                    currencyId
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Преводът е извършен успешно."
            );

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/transfers";
    }
}