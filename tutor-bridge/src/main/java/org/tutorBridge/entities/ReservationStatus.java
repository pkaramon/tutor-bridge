package org.tutorBridge.entities;

public enum ReservationStatus {
    ACCEPTED('A'),
    CANCELLED('C'),
    NEW('N');

    private final char code;

    ReservationStatus(char code) {
        this.code = code;
    }

    public static ReservationStatus fromCode(char code) {
        return switch (code) {
            case 'A' -> ACCEPTED;
            case 'C' -> CANCELLED;
            case 'N' -> NEW;
            default -> throw new IllegalArgumentException("Unknown code: " + code);
        };
    }

    char getCode() {
        return code;
    }
}
