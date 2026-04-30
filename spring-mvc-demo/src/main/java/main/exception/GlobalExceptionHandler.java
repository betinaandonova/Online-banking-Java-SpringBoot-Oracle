package main.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public String handleInvalidDataException(InvalidDataException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-popup";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {

        ex.printStackTrace();

        model.addAttribute(
                "errorMessage",
                "Тип грешка: " + ex.getClass().getSimpleName()
                        + "\nСъобщение: " + ex.getMessage()
        );

        return "error-popup";
    }
}