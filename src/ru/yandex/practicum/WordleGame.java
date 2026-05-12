package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private PrintWriter log;

    private String lastFeedback;

    private List<String> history = new ArrayList<>();
    private List<String> feedbacks = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        this.steps = 6;
        this.answer = dictionary.getRandomWord();

        log.println("Игра началась. Заданное слово: " + answer);
    }

    public String makeMove(String playerWord) throws InvalidWordException {
        String normalized = playerWord.toLowerCase().replace("ё", "е").trim();
        if (normalized.length() != 5) {
            throw new InvalidWordException("Введенное слово должно быть из 5 букв.");
        }

        if (!dictionary.contains(normalized)) {
            throw new InvalidWordException("в словаре нет такого слова.");
        }

        String feedback = generateFeedback(normalized, answer);

        steps--;
        log.println("Ход: " + normalized + " -> " + feedback);

        this.history.add(normalized);
        this.feedbacks.add(feedback);
        this.lastFeedback = feedback;
        return feedback;
    }

    public String generateFeedback(String guess, String answer) {
        char[] result = new char[5];
        for (int i = 0; i < result.length; i++) {
            result[i] = '-';
        }

        boolean[] usedInAnswer = new boolean[5];
        for (int i = 0; i < result.length; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                usedInAnswer[i] = true;
            }
        }

        for (int i = 0; i < result.length; i++) {
            if (result[i] == '+') continue;
            char currentChar = guess.charAt(i);
            for (int j = 0; j < result.length; j++) {
                if (!usedInAnswer[j] && answer.charAt(j) == currentChar) {
                    result[i] = '^';
                    usedInAnswer[j] = true;
                    break;
                }
            }
        }
        return new String(result);
    }

    public boolean isWin() {
        if (lastFeedback == null) return false;
        return lastFeedback.equals("+++++");
    }

    public String suggestWord() {
        List<String> candidates = new ArrayList<>(dictionary.getWords());
        for (int i = 0; i < history.size(); i++) {
            String guess = history.get(i);
            String feedback = feedbacks.get(i);

            candidates.removeIf(word -> !isCompatible(word, guess, feedback));
        }
        if (!candidates.isEmpty()) {
            Random random = new Random();
            return candidates.get(random.nextInt(candidates.size()));
        } else {
            return "Больше подсказку дать не могу, а то будет не интересно!";
        }
    }

    private boolean isCompatible(String candidate, String guess, String feedback) {
        String simulatedFeedback = generateFeedback(guess, candidate);
        return simulatedFeedback.equals(feedback);
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isGameOver() {
        return isWin() || steps <= 0;
    }
}
