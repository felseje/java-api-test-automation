package io.github.felseje.apitestautomation.client.user.dto.request;

import io.github.felseje.apitestautomation.client.user.dto.UserDto;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import java.util.HashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateUserRequest extends UserDto {

  public UpdateUserRequest(String name, String email, String password, String isAdmin) {
    super(name, email, password, isAdmin);
  }
}
