package main.controller.admin;

import jakarta.servlet.http.HttpSession;
import main.model.OnlineBankingUser;
import main.service.OnlineBankingUserService;
import main.util.AuthUtil;

public abstract class BaseAdminController {

    protected final OnlineBankingUserService userService;

    protected BaseAdminController(OnlineBankingUserService userService) {
        this.userService = userService;
    }

    protected boolean isNotAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return true;
        }

        OnlineBankingUser user = userService.findById(userId).orElse(null);

        return !AuthUtil.isAdmin(user);
    }
}