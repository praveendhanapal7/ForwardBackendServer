package com.forwardagency.forwardbackend.web;

import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.exception.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;

public final class CurrentUser {

    private CurrentUser() {}

    public static Users require(HttpServletRequest request) {
        Users user = find(request);
        if (user == null) {
            throw new InvalidCredentialsException("Authentication required");
        }
        return user;
    }

    public static Users find(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object attr = request.getAttribute(BearerAuthFilter.CURRENT_USER_ATTR);
        return attr instanceof Users u ? u : null;
    }

    public static String token(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object attr = request.getAttribute(BearerAuthFilter.CURRENT_TOKEN_ATTR);
        return attr instanceof String s ? s : null;
    }

    public static boolean isAgency(Users user) {
        return user != null && "agency".equalsIgnoreCase(user.getAccountType());
    }
}
