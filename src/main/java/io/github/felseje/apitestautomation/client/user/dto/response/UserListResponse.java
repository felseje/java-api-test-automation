package io.github.felseje.apitestautomation.client.user.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {

  @SerializedName("quantidade")
  private Integer amount;

  @SerializedName("usuarios")
  private List<UserResponse> users;
}
