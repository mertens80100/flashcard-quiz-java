import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static ArrayList<Card> cards = new ArrayList<>();
    private static String fileName = "cards.txt";

    public static void main(String[] args) {
        loadCards();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Flashcard Quiz ===");
            System.out.println("1 - Add Flashcard");
            System.out.println("2 - List Flashcards");
            System.out.println("3 - Start Quiz");
            System.out.println("0 - Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Question: ");
                String q = scanner.nextLine().trim();
                System.out.print("Answer: ");
                String a = scanner.nextLine().trim();

                if (!q.isEmpty() && !a.isEmpty()) {
                    cards.add(new Card(q, a));
                    saveCards();
                    System.out.println("Card added.");
                }
            } else if (choice.equals("2")) {
                if (cards.isEmpty()) {
                    System.out.println("No flashcards yet.");
                } else {
                    for (int i = 0; i < cards.size(); i++) {
                        Card c = cards.get(i);
                        System.out.println((i + 1) + ". Q: " + c.getQuestion() + " | A: " + c.getAnswer());
                    }
                }
            } else if (choice.equals("3")) {
                runQuiz(scanner);
            } else if (choice.equals("0")) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    private static void runQuiz(Scanner scanner) {
        if (cards.isEmpty()) {
            System.out.println("Add some flashcards first!");
            return;
        }
        ArrayList<Card> quizList = new ArrayList<>(cards);
        Collections.shuffle(quizList);

        int correct = 0;
        for (Card c : quizList) {
            System.out.println("\nQ: " + c.getQuestion());
            System.out.print("Your answer: ");
            String userAns = scanner.nextLine().trim();

            if (userAns.equalsIgnoreCase(c.getAnswer())) {
                System.out.println("Correct!");
                correct++;
            } else {
                System.out.println("Wrong! Correct answer: " + c.getAnswer());
            }
        }
        System.out.println("\nQuiz finished! Score: " + correct + " / " + quizList.size());
    }

    private static void loadCards() {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 2) {
                    cards.add(new Card(p[0], p[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load cards.");
        }
    }

    private static void saveCards() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Card c : cards) {
                pw.println(c.toFileLine());
            }
        } catch (Exception e) {
            System.out.println("Could not save cards.");
        }
    }
}
