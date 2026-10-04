package io.github.felseje.apitestautomation.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.github.felseje.apitestautomation.client.user.UserClient;
import io.github.felseje.apitestautomation.client.user.dto.request.CreateUserRequest;
import io.github.felseje.apitestautomation.client.user.dto.request.UpdateUserRequest;
import io.github.felseje.apitestautomation.client.user.dto.response.UserListResponse;
import io.github.felseje.apitestautomation.client.user.dto.response.UserResponse;
import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.service.user.UserService;
import io.github.felseje.apitestautomation.testdata.UserProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("user")
@DisplayName("User Tests")
public class UserIT {

  private final UserService userService = new UserService(new UserClient());

  @Nested
  @DisplayName("Success user scenarios")
  class SuccessUserScenarios {

    @Test
    @DisplayName("Should create user successfully when all required fields are valid")
    void shouldCreateUserSuccessfully() {
      // Arrange
      CreateUserRequest request = UserProvider.getValidCreateUserRequest();

      // Act
      ApiResponse apiResponse = userService.createUser(request);

      // Assert
      String userId = apiResponse.shouldBeCreated()
          .shouldHaveJsonField("message", "Cadastro realizado com sucesso")
          .shouldContainJsonField("_id")
          .getJsonFieldValue("_id");
      UserResponse createdUser = userService.readUser(userId)
          .shouldBeOk()
          .as(UserResponse.class);
      assertAll("User fields must match request",
          () -> assertEquals(userId, createdUser.getId(), "User id mismatch"),
          () -> assertEquals(request.getName(), createdUser.getName(), "User name mismatch"),
          () -> assertEquals(request.getEmail(), createdUser.getEmail(), "User email mismatch"),
          () -> assertEquals(request.getPassword(), createdUser.getPassword(),
              "User password mismatch"),
          () -> assertEquals(request.getAdministrator(), createdUser.getAdministrator(),
              "User administrator mismatch")
      );
    }

    @Test
    @DisplayName("Should read user by ID successfully when user exists")
    void shouldReadUserByIdSuccessfully() {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz1";

      // Act
      ApiResponse apiResponse = userService.readUser(userId);

      // Assert
      UserResponse user = apiResponse.shouldBeOk()
          .as(UserResponse.class);
      assertAll(
          () -> assertEquals("Fulano da Silva", user.getName(), "User name mismatch"),
          () -> assertEquals("fulano@qa.com", user.getEmail(), "User email mismatch"),
          () -> assertEquals("teste", user.getPassword(), "User password mismatch"),
          () -> assertEquals("true", user.getAdministrator(), "User administrator mismatch"),
          () -> assertEquals("0uxuPY0cbmQhpEz1", user.getId(), "User id mismatch")
      );
    }

    @Test
    @DisplayName("Should read all users successfully")
    void shouldReadAllUsersSuccessfully() {
      // Arrange
      Map<String, Object> queryParams = Map.of();

      // Act
      ApiResponse apiResponse = userService.readAllUsers(queryParams);

      // Assert
      List<?> users = apiResponse.shouldBeOk()
          .shouldHaveJsonFieldGreaterThan("quantidade", 1)
          .getJsonFieldValue("usuarios");
      assertFalse(users.isEmpty());
    }

    @Test
    @DisplayName("Should filter users by query params successfully")
    void shouldFilterUsersByQueryParamsSuccessfully() {
      // Arrange
      Map<String, Object> queryParams = Map.of("nome", "Fulano da Silva");

      // Act
      ApiResponse apiResponse = userService.readAllUsers(queryParams);

      // Assert
      UserListResponse users = apiResponse.shouldBeOk()
          .shouldHaveJsonField("quantidade", 1)
          .as(UserListResponse.class);
      UserResponse user = users.getUsers().getFirst();
      assertAll(
          () -> assertEquals("Fulano da Silva", user.getName(), "User name mismatch"),
          () -> assertEquals("fulano@qa.com", user.getEmail(), "User email mismatch"),
          () -> assertEquals("teste", user.getPassword(), "User password mismatch"),
          () -> assertEquals("true", user.getAdministrator(), "User administrator mismatch"),
          () -> assertEquals("0uxuPY0cbmQhpEz1", user.getId(), "User id mismatch")
      );
    }

    @Test
    @DisplayName("Should update user successfully when user exists")
    void shouldUpdateUserSuccessfully() {
      // Arrange
      CreateUserRequest createRequest = UserProvider.getValidCreateUserRequest();
      UserResponse user = userService.createUser(createRequest)
          .assumeStatusCode(201)
          .as(UserResponse.class);
      UpdateUserRequest updateRequest = new UpdateUserRequest(
          createRequest.getName(),
          createRequest.getEmail(),
          "P4ssw0rd",
          createRequest.getAdministrator()
      );

      // Act
      ApiResponse apiResponse = userService.updateUser(user.getId(), updateRequest);

      // Assert
      apiResponse.shouldBeOk()
          .shouldHaveJsonField("message", "Registro alterado com sucesso");
      UserResponse createdUser = userService.readUser(user.getId())
          .shouldBeOk()
          .as(UserResponse.class);
      assertAll("User fields must match request",
          () -> assertEquals(user.getId(), createdUser.getId(), "User id mismatch"),
          () -> assertEquals(updateRequest.getName(), createdUser.getName(), "User name mismatch"),
          () -> assertEquals(updateRequest.getEmail(), createdUser.getEmail(),
              "User email mismatch"),
          () -> assertEquals(updateRequest.getPassword(), createdUser.getPassword(),
              "User password mismatch"),
          () -> assertEquals(updateRequest.getAdministrator(), createdUser.getAdministrator(),
              "User administrator mismatch")
      );
    }

    @Test
    @DisplayName("Should create user via PUT when ID does not exist")
    void shouldCreateUserViaPutWhenIdDoesNotExist() {
      // Arrange
      String userId = UserProvider.getValidRandomUserId();
      UpdateUserRequest updateRequest = UserProvider.getValidUpdateUserRequest();

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, updateRequest);

      // Assert
      String createdId = apiResponse.shouldBeCreated()
          .shouldHaveJsonField("message", "Cadastro realizado com sucesso")
          .getJsonFieldValue("_id");
      UserResponse createdUser = userService.readUser(createdId)
          .shouldBeOk()
          .as(UserResponse.class);
      assertAll("User fields must match request",
          () -> assertEquals(createdId, createdUser.getId(), "User id mismatch"),
          () -> assertEquals(updateRequest.getName(), createdUser.getName(), "User name mismatch"),
          () -> assertEquals(updateRequest.getEmail(), createdUser.getEmail(),
              "User email mismatch"),
          () -> assertEquals(updateRequest.getPassword(), createdUser.getPassword(),
              "User password mismatch"),
          () -> assertEquals(updateRequest.getAdministrator(), createdUser.getAdministrator(),
              "User administrator mismatch")
      );
    }

    @Test
    @DisplayName("Should delete user successfully when user exists and has no cart")
    void shouldDeleteUserSuccessfully() {
      // Arrange
      CreateUserRequest createRequest = UserProvider.getValidCreateUserRequest();
      String userId = userService.createUser(createRequest)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");
      // Act
      ApiResponse apiResponse = userService.deleteUser(userId);

      // Assert
      apiResponse.shouldBeOk()
          .shouldHaveJsonField("message", "Registro excluído com sucesso");
      userService.readUser(userId)
          .shouldBeBadRequest()
          .shouldHaveJsonField("message", "Usuário não encontrado");
    }
  }

  @Nested
  @DisplayName("Failure user scenarios")
  class FailureUserScenarios {

    static Stream<Arguments> invalidCreateUserRequests() {
      return Stream.of(
          scenario(
              "blank name",
              r -> r.setName(""),
              "nome",
              "nome não pode ficar em branco"
          ),
          scenario(
              "blank email",
              r -> r.setEmail(""),
              "email",
              "email não pode ficar em branco"
          ),
          scenario(
              "blank password",
              r -> r.setPassword(""),
              "password",
              "password não pode ficar em branco"
          ),
          scenario(
              "blank administrator",
              r -> r.setAdministrator(""),
              "administrador",
              "administrador deve ser 'true' ou 'false'"
          )
      );
    }

    private static Arguments scenario(
        String description,
        Consumer<CreateUserRequest> invalidation,
        String jsonField,
        String expectedMessage) {
      return Arguments.of(description, invalidation, jsonField, expectedMessage);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when creating user with duplicate email")
    void shouldFailToCreateUserWithDuplicateEmail() {
      // Arrange
      CreateUserRequest firstUser = UserProvider.getValidCreateUserRequest();
      CreateUserRequest secondUser = UserProvider.getValidCreateUserRequest();
      secondUser.setEmail(firstUser.getEmail());
      userService.createUser(firstUser).assumeStatusCode(201);

      // Act
      ApiResponse apiResponse = userService.createUser(secondUser);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message", "Este email já está sendo usado");
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidCreateUserRequests")
    @DisplayName("Should return 400 Bad Request when creating user with missing required fields")
    void shouldFailToCreateUserWithMissingRequiredFields(
        String scenario,
        Consumer<CreateUserRequest> invalidation,
        String jsonField,
        String expectedMessage
    ) {
      // Arrange
      CreateUserRequest request = UserProvider.getValidCreateUserRequest();
      invalidation.accept(request);

      // Act
      ApiResponse apiResponse = userService.createUser(request);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField(jsonField, expectedMessage);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when reading user with non-existent ID")
    void shouldFailToReadUserWithNonExistentId() {
      // Arrange
      String userId = "N0Na3X1S73N7a1Da";

      // Act
      ApiResponse apiResponse = userService.readUser(userId);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message", "Usuário não encontrado");
    }

    @Test
    @DisplayName("Should return 400 Bad Request when updating user with duplicate email")
    void shouldFailToUpdateUserWithDuplicateEmail() {
      // Arrange
      CreateUserRequest firstUserData = UserProvider.getValidCreateUserRequest();
      CreateUserRequest secondUserData = UserProvider.getValidCreateUserRequest();
      userService.createUser(firstUserData)
          .assumeStatusCode(201);
      String secondUserId = userService.createUser(secondUserData)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");
      UpdateUserRequest updateRequest = new UpdateUserRequest(
          secondUserData.getName(),
          firstUserData.getEmail(),
          secondUserData.getPassword(),
          secondUserData.getAdministrator()
      );

      // Act
      ApiResponse apiResponse = userService.updateUser(secondUserId, updateRequest);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message", "Este email já está sendo usado");
    }

    @Test
    @DisplayName("Should return 400 Bad Request when updating user with invalid body")
    void shouldFailToUpdateUserWithInvalidBody() {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz1";
      UpdateUserRequest updateRequest = new UpdateUserRequest();

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, updateRequest);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldMatchJsonSchemaInClasspath("schemas/user/create-user-empty-body.json");
    }

    @Test
    @DisplayName("Should return 200 OK with no records deleted message when deleting non-existent user")
    void shouldReturnNoRecordsDeletedWhenDeletingNonExistentUser() {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz2";

      // Act
      ApiResponse apiResponse = userService.deleteUser(userId);

      // Assert
      apiResponse.shouldBeOk()
          .shouldHaveJsonField("message", "Nenhum registro excluído");
    }

    @Test
    @DisplayName("Should return 400 Bad Request when deleting user with an active cart")
    void shouldFailToDeleteUserWithActiveCart() {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz1";

      // Act
      ApiResponse apiResponse = userService.deleteUser(userId);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message",
              "Não é permitido excluir usuário com carrinho cadastrado");
    }
  }
}