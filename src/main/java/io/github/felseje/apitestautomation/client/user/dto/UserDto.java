package io.github.felseje.apitestautomation.client.user.dto;

import com.google.gson.annotations.SerializedName;
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
}
