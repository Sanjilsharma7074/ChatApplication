package Code;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ChatApp chatApp = new ChatApp();
        chatApp.run();
        scanner.close();
    }
}