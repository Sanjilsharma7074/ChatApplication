package Code;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class ChatService {
  private Map<String, Queue<Message>> messageQueue = new HashMap<>();

  // Block feature: Check if recipient has blocked sender
  public void sendMessage(User sender, AuthService authService) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter recipient username: " + ConsoleColors.RESET);
    String recipient = sc.nextLine();
    if (recipient.equals(sender.getUsername())) {
      System.out.println(ConsoleColors.RED + "You cannot send a message to yourself." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    User recipientUser = authService.getUsers().get(recipient);
    if (recipientUser == null) {
      System.out.println(ConsoleColors.RED + "User does not exist." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    if (recipientUser.getBlockedUsers().contains(sender.getUsername())) {
      System.out.println(ConsoleColors.RED + "You are blocked by this user. Message not sent." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    System.out.print(ConsoleColors.PURPLE + "Enter message: " + ConsoleColors.RESET);
    String content = sc.nextLine();
    Message message = new Message(sender.getUsername(), content, new Date());

    messageQueue.computeIfAbsent(recipient, k -> new LinkedList<>()).add(message);
    System.out.println(ConsoleColors.GREEN + "Message sent to " + recipient + " (Status: "
        + (recipientUser.isOnline() ? "Active" : "Offline") + ")" + ConsoleColors.RESET);
        sc.close();
  }

  // Delete message from inbox (by index)
  public void deleteMessage(User user) {
    Queue<Message> inbox = messageQueue.getOrDefault(user.getUsername(), new LinkedList<>());
    if (inbox.isEmpty()) {
      System.out.println(ConsoleColors.YELLOW + "Inbox is empty." + ConsoleColors.RESET);
      return;
    }
    List<Message> messages = new ArrayList<>(inbox);
    for (int i = 0; i < messages.size(); i++) {
      Message msg = messages.get(i);
      System.out.println(ConsoleColors.YELLOW + (i + 1) + ". From: " + msg.getSender() + " | Time: "
          + msg.getTimestamp() + ConsoleColors.RESET);
      System.out.println(ConsoleColors.CYAN + "   Message: " + msg.getContent() + ConsoleColors.RESET);
    }
    System.out.print(ConsoleColors.PURPLE + "Enter message number to delete (or 0 to cancel): " + ConsoleColors.RESET);
    Scanner sc = new Scanner(System.in);
    int idx;
    try {
      idx = Integer.parseInt(sc.nextLine());
    } catch (Exception e) {
      System.out.println(ConsoleColors.RED + "Invalid input." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    if (idx < 1 || idx > messages.size()) {
      System.out.println(ConsoleColors.YELLOW + "Cancelled or invalid choice." + ConsoleColors.RESET);
      sc.close();
      return;
    }
    Message toRemove = messages.get(idx - 1);
    inbox.remove(toRemove);
    System.out.println(ConsoleColors.GREEN + "Message deleted." + ConsoleColors.RESET);
    sc.close();
  }

  // Search messages in personal inbox
  public void searchMessages(User user) {
    Scanner sc = new Scanner(System.in);
    System.out.print(ConsoleColors.PURPLE + "Enter keyword to search: " + ConsoleColors.RESET);
    String keyword = sc.nextLine().toLowerCase();

    Queue<Message> inbox = messageQueue.getOrDefault(user.getUsername(), new LinkedList<>());
    boolean found = false;

    for (Message msg : inbox) {
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

  // Sort messages by timestamp (asc/desc)
  public void sortMessages(User user, boolean ascending) {
    Queue<Message> inbox = messageQueue.getOrDefault(user.getUsername(), new LinkedList<>());
    List<Message> sorted = new ArrayList<>(inbox);

    sorted.sort((m1, m2) -> ascending ? m1.getTimestamp().compareTo(m2.getTimestamp())
        : m2.getTimestamp().compareTo(m1.getTimestamp()));

    System.out.println(ConsoleColors.CYAN + "\n=== Sorted Inbox Messages ===" + ConsoleColors.RESET);
    for (Message msg : sorted) {
      System.out.println(
          ConsoleColors.CYAN + "From: " + msg.getSender() + " | Time: " + msg.getTimestamp() + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "Message: " + msg.getContent() + ConsoleColors.RESET);
      System.out.println(ConsoleColors.BLUE + "-------------------------" + ConsoleColors.RESET);
    }
  }

  public void viewInbox(User user) {
    Queue<Message> inbox = messageQueue.getOrDefault(user.getUsername(), new LinkedList<>());
    if (inbox.isEmpty()) {
      System.out.println(ConsoleColors.YELLOW + "Inbox is empty." + ConsoleColors.RESET);
    } else {
      System.out.println(ConsoleColors.CYAN + "\n=== Inbox ===" + ConsoleColors.RESET);
      for (Message msg : inbox) {
        System.out.println(
            ConsoleColors.CYAN + "From: " + msg.getSender() + " | Time: " + msg.getTimestamp() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Message: " + msg.getContent() + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "-------------------------" + ConsoleColors.RESET);
      }
    }
  }
}
