package Code;

import java.util.Scanner;

class ChatApp {
  private AuthService authService = new AuthService();
  private ChatService chatService = new ChatService();
  private FriendService friendService = new FriendService(authService);
  private GroupChatService groupChatService;
  private User currentUser = null;
  private static final Scanner scanner = new Scanner(System.in);

  private void viewGroupMessages(User user) {
    groupChatService.viewGroupMessages(user);
  }

  public ChatApp() {
    this.groupChatService = new GroupChatService(authService);
  }

  public void run() {
    while (true) {
      System.out.println(ConsoleColors.CYAN + "\n=== Welcome to ConsoleChat ===" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "1. Sign Up" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "2. Log In" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "3. Exit" + ConsoleColors.RESET);
      System.out.print(ConsoleColors.PURPLE + "Choose an option: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          authService.signUp();
          break;
        case "2":
          currentUser = authService.logIn();
          if (currentUser != null) {
            mainMenu();
            currentUser.setOnline(false); // Set offline on logout
          }
          break;
        case "3":
          System.out.println(ConsoleColors.GREEN + "Goodbye!" + ConsoleColors.RESET);
          return;
        default:
          System.out.println(ConsoleColors.RED + "Invalid option. Try again." + ConsoleColors.RESET);
      }
    }
  }

  private void mainMenu() {
    while (true) {
      System.out.println(ConsoleColors.CYAN + "\n=== Main Menu ===" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "1. View Inbox" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "2. Send Message" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "3. Manage Friends" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "4. Send Group Message" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "5. View Group Messages" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "6. Block/Unblock Users" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "7. Delete Message from Inbox" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "8. Search Inbox Messages" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "9. Sort Inbox Messages" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "10. Search Group Messages" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "11. Sort Group Messages" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "12. Logout" + ConsoleColors.RESET);

      System.out.print(ConsoleColors.PURPLE + "Choose an option: " + ConsoleColors.RESET);

      String option = scanner.nextLine();

      switch (option) {
        case "1":
          chatService.viewInbox(currentUser);
          break;
        case "2":
          chatService.sendMessage(currentUser, authService);
          break;
        case "3":
          friendService.manageFriends(currentUser);
          break;
        case "4":
          groupChatService.manageGroups(currentUser);
          break;
        case "5":
          viewGroupMessages(currentUser);
          break;
        case "6":
          manageBlockList(currentUser);
          break;
        case "7":
          chatService.deleteMessage(currentUser);
          break;
        case "8":
          chatService.searchMessages(currentUser);
          break;
        case "9":
          System.out.print(ConsoleColors.PURPLE + "Sort by (1) Oldest or (2) Newest: " + ConsoleColors.RESET);
          String order = scanner.nextLine();
          chatService.sortMessages(currentUser, order.equals("1"));
          break;
        case "10":
          groupChatService.searchGroupMessages(currentUser);
          break;
        case "11":
          groupChatService.sortGroupMessages(currentUser);
          break;
        case "12":
          return;
        default:
          System.out.println(ConsoleColors.RED + "Invalid option. Try again." + ConsoleColors.RESET);

      }
    }
  }

  // Block/Unblock Users
  private void manageBlockList(User user) {
    Scanner sc = scanner;
    while (true) {
      System.out.println(ConsoleColors.CYAN + "\n=== Block/Unblock Users ===" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "1. Block a user" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "2. Unblock a user" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "3. View blocked users" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "4. Back" + ConsoleColors.RESET);
      System.out.print(ConsoleColors.PURPLE + "Choose an option: " + ConsoleColors.RESET);
      String choice = sc.nextLine();
      if (choice.equals("1")) {
        System.out.print(ConsoleColors.PURPLE + "Enter username to block: " + ConsoleColors.RESET);
        String toBlock = sc.nextLine();
        if (toBlock.equals(user.getUsername())) {
          System.out.println(ConsoleColors.RED + "You cannot block yourself." + ConsoleColors.RESET);
        } else if (user.getBlockedUsers().contains(toBlock)) {
          System.out.println(ConsoleColors.YELLOW + "User already blocked." + ConsoleColors.RESET);
        } else {
          user.getBlockedUsers().add(toBlock);
          System.out.println(ConsoleColors.GREEN + "Blocked " + toBlock + ConsoleColors.RESET);
        }
      } else if (choice.equals("2")) {
        System.out.print(ConsoleColors.PURPLE + "Enter username to unblock: " + ConsoleColors.RESET);
        String toUnblock = sc.nextLine();
        if (user.getBlockedUsers().remove(toUnblock)) {
          System.out.println(ConsoleColors.GREEN + "Unblocked " + toUnblock + ConsoleColors.RESET);
        } else {
          System.out.println(ConsoleColors.YELLOW + "User was not blocked." + ConsoleColors.RESET);
        }
      } else if (choice.equals("3")) {
        System.out.println(ConsoleColors.CYAN + "Blocked users: " + user.getBlockedUsers() + ConsoleColors.RESET);
      } else if (choice.equals("4")) {
        return;
      } else {
        System.out.println(ConsoleColors.RED + "Invalid option." + ConsoleColors.RESET);
      }
    }
  }
}
