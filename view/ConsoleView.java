package com.taskmanager.view;

import java.util.Scanner;

public class ConsoleView {
    protected static final Scanner scanner = new Scanner(System.in);

    public static final String LINE  = "=".repeat(55);
    public static final String DLINE = "-".repeat(55);

    public static void printHeader(String title) {
        System.out.println("\n" + LINE);
        int pad = (55 - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(pad, 0)) + title);
        System.out.println(LINE);
    }

    public static void printSuccess(String msg) {
        System.out.println("\n  ✔  " + msg);
    }

    public static void printError(String msg) {
        System.out.println("\n  ✘  ERROR: " + msg);
    }

    public static void printInfo(String msg) {
        System.out.println("  ▸  " + msg);
    }

    public static String prompt(String label) {
        System.out.print("  " + label + ": ");
        return scanner.nextLine().trim();
    }

    public static String promptOptional(String label) {
        System.out.print("  " + label + " (press Enter to skip): ");
        return scanner.nextLine().trim();
    }

    public static void pressEnterToContinue() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }

    public static int readMenuChoice(int max) {
        while (true) {
            String input = prompt("Enter choice (1-" + max + ")");
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= max) return choice;
                printError("Please enter a number between 1 and " + max);
            } catch (NumberFormatException e) {
                printError("Invalid input. Enter a number.");
            }
        }
    }
}
