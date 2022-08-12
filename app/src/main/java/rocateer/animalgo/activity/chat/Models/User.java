package rocateer.animalgo.activity.chat.Models;

import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

  private String id;
  private String name;
  private String avatar;
  private String type;

  public User(String id, String name, String avatar, String type) {
    this.id = id;
    this.name = name;
    this.avatar = avatar;
    this.type = type;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getAvatar() {
    return avatar;
  }

  public String getType() {
    return type;
  }
}


