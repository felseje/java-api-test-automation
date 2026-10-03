package io.github.felseje.apitestautomation.client.user.dto.request;

import io.github.felseje.apitestautomation.client.user.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequest extends UserDto {

  public CreateUserRequest(String name, String email, String password, String isAdmin) {
    super(name, email, password, isAdmin);
  }
}
