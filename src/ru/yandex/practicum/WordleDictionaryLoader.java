package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public ArrayList<String> load(String path) throws DictionaryLoadException {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line != null && !line.isBlank()) {
                    words.add(line);
                }
                if (words.isEmpty()) {
                    log.println("Словарь пуст после загрузки.");
                    throw new DictionaryLoadException("Словарь не содержит слов");
                }
            }
        } catch (IOException e) {
            log.println("Ошибка чтения файла: " + e.getMessage());
            throw new DictionaryLoadException("Не удалось загрузить словарь", e);
        }
        return words;
    }
}
