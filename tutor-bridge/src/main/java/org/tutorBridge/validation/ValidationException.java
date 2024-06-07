package org.tutorBridge.validation;

import java.util.Collection;

public class ValidationException extends RuntimeException {
    private final Collection<String> messages;

    public ValidationException(Collection<String> messages) {
        super(String.join("\n", messages));
        this.messages = messages;
    }

    public Collection<String> getMessages() {
        return messages;
    }
}
