package com.forwardagency.forwardbackend.web.dto;

import com.forwardagency.forwardbackend.Model.Users;

public record UserView(
        String email,
        String name,
        String accountType,
        String brandName,
        String phoneNumber,
        String location) {

    public static UserView from(Users user) {
        if (user == null) {
            return null;
        }
        return new UserView(
                user.getEmail(),
                user.getName(),
                user.getAccountType(),
                user.getBrandName(),
                user.getPhoneNumber(),
                user.getLocation());
    }
}
