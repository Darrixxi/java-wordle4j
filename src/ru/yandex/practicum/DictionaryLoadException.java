package ru.yandex.practicum;

public class DictionaryLoadException extends RuntimeException {
    public DictionaryLoadException(final String message) {
        super(message);
    }

    public DictionaryLoadException(final String message, Throwable e) {
        super(message, e);
    }
}
