package Code;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class GroupChatService {
  private Map<String, GroupChat> groups = new HashMap<>();
  private AuthService authService;

  public GroupChatService(AuthService authService) {
    this.authService = authService;
  }

  public void searchGroupMessages(User user) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();

    GroupChat group = groups.get(groupName);
    if (group == null || !group.getMembers().contains(user.getUsername())) {
      System.out.println(ConsoleColors.RED + "Group not found or access denied." + ConsoleColors.RESET);
      sc.close();
      return;
    }

    System.out.print(ConsoleColors.PURPLE + "Enter keyword to search: " + ConsoleColors.RESET);
    String keyword = sc.nextLine().toLowerCase();
    boolean found = false;

    for (Message msg : group.getMessages()) {
      if (msg.getContent().toLowerCase().contains(keyword)) {
        System.out.println(
            ConsoleColors.CYAN + "From: " + msg.getSender() + " | Time: " + msg.getTimestamp() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Message: " + msg.getContent() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "-------------------------" + ConsoleColors.RESET);
        found = true;
      }
    }

    if (!found) {
      System.out.println(ConsoleColors.RED + "No matching messages found." + ConsoleColors.RESET);
    }
    sc.close();
  }

  public void sortGroupMessages(User user) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();

    GroupChat group = groups.get(groupName);
    if (group == null || !group.getMembers().contains(user.getUsername())) {
      System.out.println(ConsoleColors.RED + "Group not found or access denied." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    System.out.print(ConsoleColors.PURPLE + "Sort by (1) Oldest or (2) Newest: " + ConsoleColors.RESET);
    String order = sc.nextLine();
    List<Message> sorted = new ArrayList<>(group.getMessages());

    sorted.sort((m1, m2) -> order.equals("1") ? m1.getTimestamp().compareTo(m2.getTimestamp())
        : m2.getTimestamp().compareTo(m1.getTimestamp()));

    for (Message msg : sorted) {
      System.out.println(
          ConsoleColors.CYAN + "From: " + msg.getSender() + " | Time: " + msg.getTimestamp() + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "Message: " + msg.getContent() + ConsoleColors.RESET);
      System.out.println(ConsoleColors.BLUE + "-------------------------" + ConsoleColors.RESET);
    }
    sc.close();
  }

  public void createGroup(User creator) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();
    GroupChat group = new GroupChat(groupName, creator);
    groups.put(groupName, group);
    creator.getGroups().add(groupName);
    System.out.println(ConsoleColors.GREEN + "Group created successfully." + ConsoleColors.RESET);
    sc.close();
  }

  public void addUserToGroup(User creator) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name to add user: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();
    System.out.print(ConsoleColors.PURPLE + "Enter username to add to group: " + ConsoleColors.RESET);
    String username = sc.nextLine();

    GroupChat group = groups.get(groupName);
    if (group != null && group.getCreator().equals(creator)) {
      User targetUser = authService.getUsers().get(username);
      if (targetUser == null) {
        System.out.println(ConsoleColors.RED + "User does not exist." + ConsoleColors.RESET);
        sc.close();
        return;
      }
      group.getMembers().add(username);
      targetUser.getGroups().add(groupName);
      System.out.println(ConsoleColors.GREEN + username + " added to the group." + ConsoleColors.RESET);
    } else {
      System.out.println(
          ConsoleColors.RED + "You are not the creator of the group or the group doesn't exist." + ConsoleColors.RESET);
    }
    sc.close();
  }

  public void sendGroupMessage(User sender) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();

    GroupChat group = groups.get(groupName);
    if (group == null || !group.getMembers().contains(sender.getUsername())) {
      System.out.println(ConsoleColors.RED + "Group not found or access denied." + ConsoleColors.RESET);
      sc.close();
      return;
    }

    System.out.print(ConsoleColors.PURPLE + "Enter message: " + ConsoleColors.RESET);
    String content = sc.nextLine();
    Message message = new Message(sender.getUsername(), content, new Date());
    group.getMessages().add(message);
    System.out.println(ConsoleColors.GREEN + "Message sent to group " + groupName + ConsoleColors.RESET);
    sc.close();
  }

  public void viewGroupMessages(User user) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter group name: " + ConsoleColors.RESET);
    String groupName = sc.nextLine();

    GroupChat group = groups.get(groupName);
    if (group == null || !group.getMembers().contains(user.getUsername())) {
      System.out.println(ConsoleColors.RED + "Group not found or access denied." + ConsoleColors.RESET);
      sc.close();
      return;
    }

    if (group.getMessages().isEmpty()) {
      System.out.println(ConsoleColors.YELLOW + "No messages in this group." + ConsoleColors.RESET);
    } else {
      System.out.println(ConsoleColors.CYAN + "\n=== Group Messages ===" + ConsoleColors.RESET);
      for (Message msg : group.getMessages()) {
        System.out.println(
            ConsoleColors.CYAN + "From: " + msg.getSender() + " | Time: " + msg.getTimestamp() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Message: " + msg.getContent() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "-------------------------" + ConsoleColors.RESET);
      }
    }
    sc.close();
  }

  public void manageGroups(User user) {
    Scanner sc = new Scanner(System.in);
    while (true) {
      System.out.println(ConsoleColors.CYAN + "\n=== Group Chat Management ===" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "1. Create Group" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "2. Add User to Group" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "3. Send Group Message" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "4. Back to Main Menu" + ConsoleColors.RESET);
      System.out.print(ConsoleColors.PURPLE + "Choose an option: " + ConsoleColors.RESET);

      String option = sc.nextLine();

      switch (option) {
        case "1":
          createGroup(user);
          break;
        case "2":
          addUserToGroup(user);
          break;
        case "3":
          sendGroupMessage(user);
          break;
        case "4":
          return;
        default:
          System.out.println(ConsoleColors.RED + "Invalid option." + ConsoleColors.RESET);
      }
      sc.close();
    }
  }
}
