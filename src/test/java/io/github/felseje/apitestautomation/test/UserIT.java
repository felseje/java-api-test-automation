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
public class UserIT extends BaseIT {

  private final UserService userService = new UserService(new UserClient());

  private void assertUserData(Map<String, Object> expected, Map<String, Object> actual) {
    assertAll("User fields must match request",
        () -> assertEquals(
            expected.get("userId"), actual.get("userId"), "User id mismatch"
        ),
        () -> assertEquals(
            expected.get("name"), actual.get("name"), "User name mismatch"
        ),
        () -> assertEquals(
            expected.get("email"), actual.get("email"), "User email mismatch"
        ),
        () -> assertEquals(
            expected.get("password"), actual.get("password"), "User password mismatch"
        ),
        () -> assertEquals(
            expected.get("admin"), actual.get("admin"), "User administrator mismatch"
        )
    );
  }

  protected void scheduleUserRemoval(String userId) {
    scheduleCleanup("user", userId, () -> userService.deleteUser(userId));
  }

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
          .getJsonFieldValue("_id");
      UserResponse createdUser = userService.readUser(userId)
          .assumeStatusCode(200)
          .as(UserResponse.class);
      assertUserData(request.toMap(userId), createdUser.toMap());

      scheduleUserRemoval(userId);
    }

    @Test
    @DisplayName("Should read user by ID successfully when user exists")
    void shouldReadUserByIdSuccessfully() {
      // Arrange
      CreateUserRequest createUserRequest = UserProvider.getValidCreateUserRequest();
      String userId = userService.createUser(createUserRequest)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");

      // Act
      ApiResponse apiResponse = userService.readUser(userId);

      // Assert
      UserResponse createdUser = apiResponse.shouldBeOk().as(UserResponse.class);
      assertUserData(createUserRequest.toMap(userId), createdUser.toMap());

      scheduleUserRemoval(userId);
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
      CreateUserRequest createUserRequest = UserProvider.getValidCreateUserRequest();
      Map<String, Object> queryParams = Map.of("nome", createUserRequest.getName());
      String userId = userService.createUser(createUserRequest)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");

      // Act
      ApiResponse apiResponse = userService.readAllUsers(queryParams);

      // Assert
      UserListResponse users = apiResponse.shouldBeOk()
          .shouldHaveJsonField("quantidade", 1)
          .as(UserListResponse.class);
      UserResponse createdUser = users.getUsers().getFirst();
      assertUserData(createUserRequest.toMap(userId), createdUser.toMap());

      scheduleUserRemoval(userId);
    }

    @Test
    @DisplayName("Should update user successfully when user exists")
    void shouldUpdateUserSuccessfully() {
      // Arrange
      CreateUserRequest createUserRequest = UserProvider.getValidCreateUserRequest();
      String userId = userService.createUser(createUserRequest)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");
      UpdateUserRequest updateUserRequest = new UpdateUserRequest(
          createUserRequest.getName(),
          createUserRequest.getEmail(),
          "P4ssw0rd",
          createUserRequest.getAdministrator()
      );

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, updateUserRequest);

      // Assert
      apiResponse.shouldBeOk()
          .shouldHaveJsonField("message", "Registro alterado com sucesso");
      UserResponse updatedUser = userService.readUser(userId)
          .shouldBeOk()
          .as(UserResponse.class);
      assertUserData(createUserRequest.toMap(userId), updatedUser.toMap());

      scheduleUserRemoval(userId);
    }

    @Test
    @DisplayName("Should create user via PUT when ID does not exist")
    void shouldCreateUserViaPutWhenIdDoesNotExist() {
      // Arrange
      String userId = UserProvider.getValidRandomUserId();
      UpdateUserRequest updateUserRequest = UserProvider.getValidUpdateUserRequest();

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, updateUserRequest);

      // Assert
      String createdId = apiResponse.shouldBeCreated()
          .shouldHaveJsonField("message", "Cadastro realizado com sucesso")
          .getJsonFieldValue("_id");
      UserResponse createdUser = userService.readUser(createdId)
          .shouldBeOk()
          .as(UserResponse.class);
      assertUserData(updateUserRequest.toMap(createdId), createdUser.toMap());

      scheduleUserRemoval(createdId);
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

    private static Stream<Arguments> invalidCreateUserRequests() {
      return Stream.of(
          scenario(
              "blank name",
              (CreateUserRequest r) -> r.setName(""),
              "nome",
              "nome não pode ficar em branco"
          ),
          scenario(
              "blank email",
              (CreateUserRequest r) -> r.setEmail(""),
              "email",
              "email não pode ficar em branco"
          ),
          scenario(
              "blank password",
              (CreateUserRequest r) -> r.setPassword(""),
              "password",
              "password não pode ficar em branco"
          ),
          scenario(
              "blank administrator",
              (CreateUserRequest r) -> r.setAdministrator(""),
              "administrador",
              "administrador deve ser 'true' ou 'false'"
          )
      );
    }

    private static Stream<Arguments> invalidUpdateUserRequests() {
      return Stream.of(
          scenario(
              "blank name",
              (UpdateUserRequest r) -> r.setName(""),
              "nome",
              "nome não pode ficar em branco"
          ),
          scenario(
              "blank email",
              (UpdateUserRequest r) -> r.setEmail(""),
              "email",
              "email não pode ficar em branco"
          ),
          scenario(
              "blank password",
              (UpdateUserRequest r) -> r.setPassword(""),
              "password",
              "password não pode ficar em branco"
          ),
          scenario(
              "blank administrator",
              (UpdateUserRequest r) -> r.setAdministrator(""),
              "administrador",
              "administrador deve ser 'true' ou 'false'"
          )
      );
    }

    private static <T> Arguments scenario(
        String description,
        Consumer<T> invalidation,
        String jsonField,
        String expectedMessage) {
      return Arguments.of(description, invalidation, jsonField, expectedMessage);
    }


    @Test
    @DisplayName("Should return 400 Bad Request when creating user with duplicate email")
    void shouldFailToCreateUserWithDuplicateEmail() {
      // Arrange
      CreateUserRequest firstUserData = UserProvider.getValidCreateUserRequest();
      CreateUserRequest secondUserData = UserProvider.getValidCreateUserRequest();
      secondUserData.setEmail(firstUserData.getEmail());
      String userId = userService.createUser(firstUserData)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");

      // Act
      ApiResponse apiResponse = userService.createUser(secondUserData);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message", "Este email já está sendo usado");

      scheduleUserRemoval(userId);
    }

    @Test
    @DisplayName("Should fail to create user with empty body")
    void shouldFailToCreateUserWithEmptyBody() {
      // Arrange
      CreateUserRequest createUserRequest = new CreateUserRequest();

      // Act
      ApiResponse apiResponse = userService.createUser(createUserRequest);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldMatchJsonSchemaInClasspath("schemas/user/empty-body-response-error.json")
          .shouldHaveJsonField("nome", "nome é obrigatório")
          .shouldHaveJsonField("email", "email é obrigatório")
          .shouldHaveJsonField("password", "password é obrigatório")
          .shouldHaveJsonField("administrador", "administrador é obrigatório");
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
          .shouldMatchJsonSchemaInClasspath("schemas/user/missing-field-schema.json")
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
      String firstUserId = userService.createUser(firstUserData)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");
      String secondUserId = userService.createUser(secondUserData)
          .assumeStatusCode(201)
          .getJsonFieldValue("_id");
      UpdateUserRequest updateUserRequest = new UpdateUserRequest(
          secondUserData.getName(),
          firstUserData.getEmail(),
          secondUserData.getPassword(),
          secondUserData.getAdministrator()
      );

      // Act
      ApiResponse apiResponse = userService.updateUser(secondUserId, updateUserRequest);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldHaveJsonField("message", "Este email já está sendo usado");

      scheduleUserRemoval(firstUserId);
      scheduleUserRemoval(secondUserId);
    }

    // TODO: Criar massa necessária para o teste (usuário), limpando-a depois
    @Test
    @DisplayName("Should fail to update user with empty body")
    void shouldFailToUpdateUserWithEmptyBody() {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz1";
      UpdateUserRequest request = new UpdateUserRequest();

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, request);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldMatchJsonSchemaInClasspath("schemas/user/empty-body-response-error.json")
          .shouldHaveJsonField("nome", "nome é obrigatório")
          .shouldHaveJsonField("email", "email é obrigatório")
          .shouldHaveJsonField("password", "password é obrigatório")
          .shouldHaveJsonField("administrador", "administrador é obrigatório");
    }

    // TODO: Criar massa necessária para o teste (usuário), limpando-a depois
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidUpdateUserRequests")
    @DisplayName("Should return 400 Bad Request when updating user with invalid body")
    void shouldFailToUpdateUserWithMissingRequiredFields(
        String scenario,
        Consumer<UpdateUserRequest> invalidation,
        String jsonField,
        String expectedMessage
    ) {
      // Arrange
      String userId = "0uxuPY0cbmQhpEz1";
      UpdateUserRequest request = UserProvider.getValidUpdateUserRequest();
      invalidation.accept(request);

      // Act
      ApiResponse apiResponse = userService.updateUser(userId, request);

      // Assert
      apiResponse.shouldBeBadRequest()
          .shouldMatchJsonSchemaInClasspath("schemas/user/missing-field-schema.json")
          .shouldHaveJsonField(jsonField, expectedMessage);
    }

    @Test
    @DisplayName("Should return 200 OK with no records deleted message when deleting non-existent user")
    void shouldReturnNoRecordsDeletedWhenDeletingNonExistentUser() {
      // Arrange
      String userId = "NonExistentUser0";

      // Act
      ApiResponse apiResponse = userService.deleteUser(userId);

      // Assert
      apiResponse.shouldBeOk()
          .shouldHaveJsonField("message", "Nenhum registro excluído");
    }

    // TODO: Criar massa necessária (usuário com carrinho) para executar o teste, limpando-a depois
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