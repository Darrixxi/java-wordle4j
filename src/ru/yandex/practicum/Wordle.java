package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws IOException {
        try (PrintWriter log = new PrintWriter(new FileWriter("game.log"), true)) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            ArrayList<String> rawWords = loader.load("words_ru.txt");

            WordleDictionary dictionary = new WordleDictionary(rawWords);
            WordleGame game = new WordleGame(dictionary, log);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Игра началась! Введите слово из 5 букв");

            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getSteps());
                System.out.print("Введите слово: ");

                String input = scanner.nextLine();

                if (input.isBlank()) {
                    System.out.println("Подсказка: " + game.suggestWord());
                    continue;
                }

                try {
                    String feedback = game.makeMove(input);
                    System.out.println(feedback);
                } catch (InvalidWordException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            if (game.isWin()) {
                System.out.println("Поздравляю! Вы угадали слово!");
            } else {
                System.out.println("Попыток больше нет. Вы проиграли. Загаданное слово было: " + game.getAnswer());
            }
        }
    }
}