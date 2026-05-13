package main.enums;

public enum AccountSearchType {
    NAME("Name"),
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