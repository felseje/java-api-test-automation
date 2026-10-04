package io.github.felseje.apitestautomation.client.user.dto.response;

import com.google.gson.annotations.SerializedName;
import io.github.felseje.apitestautomation.client.user.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends UserDto {

  @SerializedName("_id")
  private String id;

  private String message;

  public UserResponse(String id, String name, String email, String password, String admin) {
    super(name, email, password, admin);
    this.id = id;
  }

  public UserResponse(String name, String email, String password, String admin) {
    super(name, email, password, admin);
  }

  public UserResponse(String name, String email, String password, Boolean isAdmin) {
    super(name, email, password, isAdmin.toString());
  }
}
