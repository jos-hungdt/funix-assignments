package com.cry301x.asm3.enums;

public enum SecurityLabel {
    Unclassified(1),
    Confidential(2),
    Secret(3),
    TopSecret(4);

    private final int value;

    SecurityLabel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static String getLabel(int value) {
        return switch (value) {
            case 1 -> "Unclassified";
            case 2 -> "Confidential";
            case 3 -> "Secret";
            case 4 -> "TopSecret";
            default -> null;
        };
    }
}
