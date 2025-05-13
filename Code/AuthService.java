package Code;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

class AuthService {
  private Map<String, User> users = new HashMap<>();

  public void signUp() {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter username: " + ConsoleColors.RESET);
    String username = sc.nextLine();
    if (users.containsKey(username)) {
      System.out.println(ConsoleColors.RED + "Username already exists." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    System.out.print(ConsoleColors.PURPLE + "Enter password: " + ConsoleColors.RESET);
    String password = sc.nextLine();
    users.put(username, new User(username, password));
    System.out.println(ConsoleColors.GREEN + "Sign up successful." + ConsoleColors.RESET);
    sc.close();
  }

  public User logIn() {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter username: " + ConsoleColors.RESET);
    String username = sc.nextLine();
    System.out.print(ConsoleColors.PURPLE + "Enter password: " + ConsoleColors.RESET);
    String password = sc.nextLine();
    User user = users.get(username);
    if (user != null && user.getPassword().equals(password)) {
      user.setOnline(true); // Set online
      System.out.println(ConsoleColors.GREEN + "Login successful. Welcome, " + username + "!" + ConsoleColors.RESET);
      System.out.println(
          ConsoleColors.BLUE + "Your status: " + (user.isOnline() ? "Active" : "Offline") + ConsoleColors.RESET);
          sc.close();
      return user;
    } else {
      System.out.println(ConsoleColors.RED + "Invalid credentials." + ConsoleColors.RESET);
      sc.close();
      return null;
    }
  }

  public Map<String, User> getUsers() {
    return users;
  }
}
