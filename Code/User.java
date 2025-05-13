package Code;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class User {
  private String username;
  private String password;
  private List<String> friends = new ArrayList<>();
  private List<String> sentRequests = new ArrayList<>();
  private List<String> receivedRequests = new ArrayList<>();
  private List<String> groups = new ArrayList<>();
  private Set<String> blockedUsers = new HashSet<>();
  private boolean online = false; // Automatic status

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public List<String> getFriends() {
    return friends;
  }

  public List<String> getSentRequests() {
    return sentRequests;
  }

  public List<String> getReceivedRequests() {
    return receivedRequests;
  }

  public List<String> getGroups() {
    return groups;
  }

  public Set<String> getBlockedUsers() {
    return blockedUsers;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }
}
