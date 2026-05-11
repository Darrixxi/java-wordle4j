package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleDictionary dictionary;
    private PrintWriter log;
    private StringWriter logWriter;

    @BeforeEach
    void setUp() {
        logWriter = new StringWriter();
        log = new PrintWriter(logWriter, true);

        List<String> testWords = List.of("аббат", "балет", "выбор", "слово", "гонец", "недуг", "герой", "медок");

        dictionary = new WordleDictionary(testWords);
    }

    private WordleGame createGameWithSecret(String secretWord) {
        List<String> singleWordList = List.of(secretWord);
        WordleDictionary specificDick = new WordleDictionary(singleWordList);
        return new WordleGame(specificDick, log);
    }
    private WordleGame createStableGame(WordleDictionary dict, String expectedSecret) {
        WordleGame game = new WordleGame(dict, log);

        // Пересоздаем игру, пока не выпадет нужное слово
        while (!game.getAnswer().equals(expectedSecret)) {
            game = new WordleGame(dict, log);
        }

        return game;
    }


    @Test
    void testExactMatch() {
        WordleGame game = createGameWithSecret("выбор");
        String feedback = game.makeMove("выбор");
        assertEquals("+++++", feedback);
        assertTrue(game.isWin());
    }

    @Test
    void testNoMatch() {
        WordleDictionary dict = new WordleDictionary(List.of("выбор", "шланг"));
        WordleGame game = new WordleGame(dict, log);

        String secret = game.getAnswer();
        String input = secret.equals("выбор") ? "шланг" : "выбор";

        String feedback = game.makeMove(input);
        assertEquals("-----", feedback);
    }

    @Test
    void testWrongPosition() {
        WordleDictionary dict = new WordleDictionary(List.of("слово", "волос"));
        WordleGame g = new WordleGame(dict, log);
        String feedback = g.makeMove("волос");

        assertNotNull(feedback);
        assertFalse(feedback.contains("-"));
    }

    @Test
    void testMixedFeedback() {
        WordleDictionary dict = new WordleDictionary(List.of("герой", "гонец"));
        WordleGame g = createStableGame(dict, "герой");

        String feedback = g.makeMove("гонец");
        assertEquals("+^-^-", feedback);
    }

    @Test
    void testDuplicateLettersOneInTarget() {
        WordleDictionary dict = new WordleDictionary(List.of("баран", "аббат"));
        WordleGame g = createStableGame(dict, "баран");

        String feedback = g.makeMove("аббат");

        assertTrue(feedback.contains("-"));
        assertEquals("^^-+-", feedback);
    }

    @Test
    void testGameNotOverMidGame() {
        WordleDictionary dict = new WordleDictionary(List.of("герой", "гонец"));
        WordleGame g = createStableGame(dict, "герой");

        g.makeMove("гонец");
        assertFalse(g.isGameOver());
        assertEquals(5, g.getSteps());
    }

    @Test
    void testEmptyInputThrowsException() {
        WordleGame game = createGameWithSecret("герой");
        assertThrows(InvalidWordException.class, () -> {
            game.makeMove("");
        });
    }

    @Test
    void testInputWithSpaces() {
        WordleGame game = createGameWithSecret("герой");
        // Пользователь случайно ввел пробел в конце
        String feedback = game.makeMove("герой ");
        assertEquals("+++++", feedback);
    }

    @Test
    void testAllLettersWrongPlace() {
        WordleDictionary dict = new WordleDictionary(List.of("слово", "волос"));
        WordleGame g = createStableGame(dict, "слово");

        String feedback = g.makeMove("волос");

        assertEquals("^^^^^", feedback);
    }

    @Test
    void testLoggingWorks() {
        WordleDictionary dict = new WordleDictionary(List.of("герой", "гонец"));
        WordleGame g = createStableGame(dict, "герой");
        g.makeMove("гонец");
        log.flush();
        String logContent = logWriter.toString();
        System.out.println("DEBUG LOG: '" + logContent + "'");

        assertTrue(logContent.contains("гонец"));
        assertTrue(logContent.contains("+^-^-"));
    }

    @Test
    void testYoLetterReplacement() {
        WordleGame game = createGameWithSecret("медок");
        String feedback = game.makeMove("мёдок");
        assertEquals("+++++", feedback);
    }

    @Test
    void testGameOverAfter6Attempts() {
        List<String> words = List.of("герой", "гонец", "экран", "балет", "выбор", "кросс", "степь");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame g = createStableGame(dict, "степь");

        List<String> inputs = List.of("герой", "гонец", "экран", "балет", "выбор", "кросс");
        for (String input : inputs) {
            g.makeMove(input);
        }

        assertTrue(g.isGameOver());
        assertFalse(g.isWin());
        assertEquals(0, g.getSteps());
    }

    @Test
    void testHintReturnsWordFromDictionary() {
        WordleDictionary dict = new WordleDictionary(List.of("герой", "гонец"));
        WordleGame game = new WordleGame(dict, log);

        String hint = game.suggestWord();

        // Подсказка должна быть одним из слов словаря
        assertTrue(hint.equals("герой") || hint.equals("гонец"));
    }
}
