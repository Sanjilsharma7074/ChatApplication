package Code;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class GroupChat {
  private String name;
  private User creator;
  private Set<String> members = new HashSet<>();
  private List<Message> messages = new ArrayList<>();

  public GroupChat(String name, User creator) {
    this.name = name;
    this.creator = creator;
    this.members.add(creator.getUsername());
  }

  public String getName() {
    return name;
  }

  public User getCreator() {
    return creator;
  }

  public Set<String> getMembers() {
    return members;
  }

  public List<Message> getMessages() {
    return messages;
  }
}
