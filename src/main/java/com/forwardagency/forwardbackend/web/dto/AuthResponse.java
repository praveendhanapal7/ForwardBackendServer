package com.forwardagency.forwardbackend.web.dto;

public record AuthResponse(String token, UserView user) {}
