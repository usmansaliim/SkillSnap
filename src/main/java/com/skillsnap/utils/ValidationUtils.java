package com.skillsnap.utils;

public class ValidationUtils {

    // ── Username ──────────────────────────────────────────────
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty())
            return false;
        if (username.length() < 3 || username.length() > 20)
            return false;
        // Only letters, numbers, underscores
        return username.matches("[a-zA-Z0-9_]+");
    }

    public static String getUsernameError(String username) {
        if (username == null || username.isEmpty())
            return "Username cannot be empty.";
        if (username.length() < 3)
            return "Username must be at least 3 characters.";
        if (username.length() > 20)
            return "Username cannot exceed 20 characters.";
        if (!username.matches("[a-zA-Z0-9_]+"))
            return "Username can only contain letters, numbers, underscore.";
        return null;
    }

    // ── Password ──────────────────────────────────────────────
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty())
            return "Password cannot be empty.";
        if (password.length() < 6)
            return "Password must be at least 6 characters.";
        return null;
    }

    // ── Email ─────────────────────────────────────────────────
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static String getEmailError(String email) {
        if (email == null || email.isEmpty())
            return "Email cannot be empty.";
        if (!isValidEmail(email))
            return "Please enter a valid email address.";
        return null;
    }

    // ── Full Name ─────────────────────────────────────────────
    public static boolean isValidFullName(String name) {
        return name != null &&
                name.trim().length() >= 2 &&
                name.trim().length() <= 60;
    }

    public static String getFullNameError(String name) {
        if (name == null || name.trim().isEmpty())
            return "Full name cannot be empty.";
        if (name.trim().length() < 2)
            return "Name must be at least 2 characters.";
        return null;
    }

    // ── Passwords match ───────────────────────────────────────
    public static boolean passwordsMatch(String p1,
                                         String p2) {
        return p1 != null && p1.equals(p2);
    }
}
