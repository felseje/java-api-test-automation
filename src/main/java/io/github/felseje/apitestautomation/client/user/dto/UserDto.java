package io.github.felseje.apitestautomation.client.user.dto;

import com.google.gson.annotations.SerializedName;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserDto {

  @SerializedName("nome")
  private String name;

  private String email;

  private String password;

  @SerializedName("administrador")
  private String administrator;

  public HashMap<String, Object> toMap(String userId) {
    ArgumentValidator.requireNotBlank(userId, "userId cannot be blank");
    final var map = new HashMap<String, Object>();
    map.put("userId", userId);
    map.put("userName", getName());
    map.put("userEmail", getEmail());
    map.put("userPassword", getPassword());
    map.put("isAdmin", Boolean.valueOf(getAdministrator()));
    return map;
  }
}
