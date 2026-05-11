package ru.yandex.practicum;

public class DictionaryLoadException extends Exception {
    public DictionaryLoadException(final String message) {
        super(message);
    }

    public DictionaryLoadException(final String message, Throwable e) {
        super(message, e);
    }
}
