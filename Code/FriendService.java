package Code;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class FriendService {
  private AuthService authService;

  public FriendService(AuthService authService) {
    this.authService = authService;
  }

  public void manageFriends(User currentUser) {
    Scanner sc = new Scanner(System.in);
    while (true) {
      System.out.println(ConsoleColors.CYAN + "\n=== Friend Management ===" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "1. Send Friend Request" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "2. View Friend Requests" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "3. View Friend List" + ConsoleColors.RESET);
      System.out.println(ConsoleColors.YELLOW + "4. Back to Main Menu" + ConsoleColors.RESET);
      System.out.print(ConsoleColors.PURPLE + "Choose an option: " + ConsoleColors.RESET);
      String choice = sc.nextLine();

      switch (choice) {
        case "1":
          System.out.print(ConsoleColors.PURPLE + "Enter username to send request: " + ConsoleColors.RESET);
          String target = sc.nextLine();
          Map<String, User> users = authService.getUsers();
          if (!users.containsKey(target)) {
            System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
          } else if (target.equals(currentUser.getUsername())) {
            System.out.println(ConsoleColors.RED + "You can't add yourself." + ConsoleColors.RESET);
          } else {
            User targetUser = users.get(target);
            if (currentUser.getFriends().contains(target)) {
              System.out.println(ConsoleColors.YELLOW + "Already friends." + ConsoleColors.RESET);
            } else {
              currentUser.getSentRequests().add(target);
              targetUser.getReceivedRequests().add(currentUser.getUsername());
              System.out.println(ConsoleColors.GREEN + "Friend request sent to " + target + ConsoleColors.RESET);
            }
          }
          break;
        case "2":
          List<String> requests = currentUser.getReceivedRequests();
          if (requests.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No friend requests." + ConsoleColors.RESET);
          } else {
            for (String sender : requests) {
              System.out.println(ConsoleColors.CYAN + "Friend request from: " + sender + ConsoleColors.RESET);
              System.out.print(ConsoleColors.PURPLE + "Accept (a) / Reject (r): " + ConsoleColors.RESET);
              String decision = sc.nextLine();
              if (decision.equalsIgnoreCase("a")) {
                currentUser.getFriends().add(sender);
                authService.getUsers().get(sender).getFriends().add(currentUser.getUsername());
                authService.getUsers().get(sender).getSentRequests().remove(currentUser.getUsername());
                System.out.println(ConsoleColors.GREEN + "Friend request accepted." + ConsoleColors.RESET);
              } else {
                authService.getUsers().get(sender).getSentRequests().remove(currentUser.getUsername());
                System.out.println(ConsoleColors.YELLOW + "Friend request rejected." + ConsoleColors.RESET);
              }
            }
            requests.clear();
          }
          break;
        case "3":
          if (currentUser.getFriends().isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No friends yet." + ConsoleColors.RESET);
          } else {
            System.out.println(ConsoleColors.CYAN + "Your friends:" + ConsoleColors.RESET);
            for (String friend : currentUser.getFriends()) {
              User friendUser = authService.getUsers().get(friend);
              String status = (friendUser != null && friendUser.isOnline()) ? "Active" : "Offline";
              System.out.println(ConsoleColors.GREEN + "- " + friend + " (" + status + ")" + ConsoleColors.RESET);
            }
          }
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