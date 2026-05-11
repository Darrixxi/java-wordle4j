package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    private final Random random = new Random();

    public WordleDictionary(List<String> rawWords) {
        ArrayList<String> cleanWords = new ArrayList<>();
        for (String word : rawWords) {
            String normalizedWord = word.toLowerCase().replace("ё", "е").trim();
            if (normalizedWord.length() == 5) {
                cleanWords.add(normalizedWord);
            }
        }
        this.words = cleanWords;
    }

    public String getRandomWord() {
        int randomWord = random.nextInt(words.size());
        return words.get(randomWord);
    }

    public boolean contains(String word) {
        String normalized = word.toLowerCase().replace("ё", "e").trim();
        return words.contains(normalized);
    }

    public List<String> getWords() {
        return words;
    }
}
