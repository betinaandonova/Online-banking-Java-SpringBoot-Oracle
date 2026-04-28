package main.enums;

public enum AccountSearchType {
    CLIENT_ID("Client ID"),
    FIRST_NAME("First name"),
    LAST_NAME("Last name"),
    EGN("EGN"),
    PHONE("Phone number");

    private final String label;

    AccountSearchType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}