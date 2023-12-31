package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountOutput;
import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdCommand;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdOutput;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.ListAccountsOutput;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.ListAccountsUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarOutput;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleOutput;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.config.ApiTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.UpdateAccountRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;

@ControllerTest(controllers = AccountAPI.class)
public class AccountAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateAccountUseCase createAccountUseCase;

    @MockBean
    private DeleteAccountUseCase deleteAccountUseCase;

    @MockBean
    private GetAccountByIdUseCase getAccountByIdUseCase;

    @MockBean
    private UpdateAvatarUseCase updateAvatarUseCase;

    @MockBean
    private UpdateAccountRoleUseCase updateAccountRoleUseCase;

    @MockBean
    private ListAccountsUseCase listAccountsUseCase;

    @Test
    void givenAValidCommand_whenCallCreateAccount_thenShouldReturnAnAccountId() throws Exception {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aId = "123";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.right(CreateAccountOutput
                        .from(aId, aEmail, aPassword)));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(aEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", equalTo(aPassword)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandFirstName_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final String aFirstName = null;
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandFirstNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "te";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandFirstNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = RandomStringUtils.generateValue(256);
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandLastName_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandLastNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "Te";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandLastNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = RandomStringUtils.generateValue(256);
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandEmail_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "testes";
        final var aEmail = "";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandPassword_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var expectedErrorMessage = "'password' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandPasswordLengthLessThan8_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "Testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandPasswordLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256).toLowerCase();
        final var expectedErrorMessage = "'password' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAnInvalidCommandPasswordNotContainsOneUppercaseAndLowercaseLetter_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aFirstName, cmd.firstName()) &&
                        Objects.equals(aLastName, cmd.lastName()) &&
                        Objects.equals(aEmail, cmd.email()) &&
                        Objects.equals(aPassword, cmd.password())
        ));
    }

    @Test
    void givenAValidCommandWithAccountId_whenCallDeleteAccount_thenShouldReturnOk() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "testes",
                "teste",
                "teste@testes.com",
                "1234567Ab",
                null
        );
        final var aId = aAccount.getId().getValue();

        final var request = MockMvcRequestBuilders.delete("/accounts/{id}", aId)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deleteAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidAccountId_whenCallDeleteAccount_thenShouldReturnOk() throws Exception {
        // given
        final var aId = "invalid";

        final var request = MockMvcRequestBuilders.delete("/accounts/{id}", aId)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deleteAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAValidCommand_whenCallGetAccount_thenShouldReturnAnAccount() throws Exception {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                Role.newRole("CEO", null, RoleTypes.EMPLOYEES, false)
        );
        final var aId = aAccount.getId().getValue();

        Mockito.when(getAccountByIdUseCase.execute(Mockito.any(GetAccountByIdCommand.class)))
                .thenReturn(GetAccountByIdOutput.from(aAccount));

        final var request = MockMvcRequestBuilders.get("/accounts/{id}", aId)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name", equalTo(aFirstName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name", equalTo(aLastName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(aEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avatar_url", equalTo(aAccount.getAvatarUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail_status", equalTo(aAccount.getMailStatus().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", equalTo(aAccount.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", equalTo(aAccount.getUpdatedAt().toString())));

        Mockito.verify(getAccountByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidId_whenCallGetAccount_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var expectedErrorMessage = "Account with id 123 was not found";
        final var aId = "123";

        Mockito.when(getAccountByIdUseCase.execute(Mockito.any(GetAccountByIdCommand.class)))
                .thenThrow(NotFoundException.with(Account.class, aId).get());

        final var request = MockMvcRequestBuilders.get("/accounts/{id}", aId)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(getAccountByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAValidIdAndImage_whenCallUpdateAvatarAccount_thenShouldReturnOkAndAccountId() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "teste123A",
                null
        );
        final var aId = aAccount.getId().getValue();

        final var aImage = new MockMultipartFile(
                "avatar",
                "image.png",
                "image/png",
                "image".getBytes()
        );

        Mockito.when(updateAvatarUseCase.execute(Mockito.any(UpdateAvatarCommand.class)))
                .thenReturn(UpdateAvatarOutput.from(aAccount));

        final var request = MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/accounts/{id}/avatar", aId)
                .file(aImage)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateAvatarCommand.class);

        Mockito.verify(updateAvatarUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.accountId());
        Assertions.assertEquals(aImage.getContentType(), actualCmd.resource().contentType());
        Assertions.assertEquals(aImage.getOriginalFilename(), actualCmd.resource().fileName());
    }

    @Test
    void givenAnInvalidIdAndImageSize_whenCallUpdateAvatarAccount_thenShouldThrowDomainException() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "teste123A",
                null
        );
        final var aId = aAccount.getId().getValue();
        final var aImage = new MockMultipartFile(
                "avatar",
                "image.png",
                "image/png",
                new byte[600 * 1024 + 2]
        );

        final var expectedErrorMessage = "Maximum image size is 600kb";

        Mockito.when(updateAvatarUseCase.execute(Mockito.any(UpdateAvatarCommand.class)))
                .thenThrow(new ImageSizeNotValidException());

        final var request = MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/accounts/{id}/avatar", aId)
                .file(aImage)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateAvatarUseCase, Mockito.times(0)).execute(Mockito.any());
    }

    @Test
    void givenAnInvalidIdAndImageType_whenCallUpdateAvatarAccount_thenShouldThrowDomainException() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "teste123A",
                null
        );
        final var aId = aAccount.getId().getValue();
        final var aImage = new MockMultipartFile(
                "avatar",
                "video.mp4",
                "video/mp4",
                "video".getBytes()
        );

        final var expectedErrorMessage = "Image type is not valid, types accept: jpg, jpeg and png";

        Mockito.when(updateAvatarUseCase.execute(Mockito.any(UpdateAvatarCommand.class)))
                .thenThrow(new ImageTypeNotValidException());

        final var request = MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/accounts/{id}/avatar", aId)
                .file(aImage)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateAvatarUseCase, Mockito.times(0)).execute(Mockito.any());
    }

    @Test
    void givenAnInvalidId_whenCallUpdateAvatarAccount_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var aId = "123";
        final var aImage = new MockMultipartFile(
                "avatar",
                "image.png",
                "image/png",
                "image".getBytes()
        );
        final var expectedErrorMessage = "Account with id 123 was not found";

        Mockito.when(updateAvatarUseCase.execute(Mockito.any(UpdateAvatarCommand.class)))
                .thenThrow(NotFoundException.with(Account.class, aId).get());

        final var request = MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/accounts/{id}/avatar", aId)
                .file(aImage)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateAvatarCommand.class);

        Mockito.verify(updateAvatarUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.accountId());
        Assertions.assertEquals(aImage.getContentType(), actualCmd.resource().contentType());
        Assertions.assertEquals(aImage.getOriginalFilename(), actualCmd.resource().fileName());
    }

    @Test
    void givenAnInvalidIdAndImage_whenCallUpdateAvatarAccount_thenShouldThrowException() throws Exception {
        // given
        final var aId = "123";
        final var aImage = new MockMultipartFile(
                "avatar",
                "image.png",
                "image/png",
                "image".getBytes()
        );
        final var expectedErrorMessage = "Internal server error";

        Mockito.when(updateAvatarUseCase.execute(Mockito.any(UpdateAvatarCommand.class)))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        final var request = MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/accounts/{id}/avatar", aId)
                .file(aImage)
                .accept(MediaType.APPLICATION_JSON)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateAvatarUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    void givenAValidIdAndRoleId_whenCallUpdateAccountRole_thenShouldReturnOkAndAccountId() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "teste123A",
                Role.newRole("CEO", null, RoleTypes.EMPLOYEES, false)
        );
        final var aRole = Role.newRole("User", "common user", RoleTypes.COMMON, true);
        final var aAccountId = aAccount.getId().getValue();
        final var aRoleId = aRole.getId().getValue();

        final var aInput = new UpdateAccountRoleApiInput(aRoleId);

        Mockito.when(updateAccountRoleUseCase.execute(Mockito.any(UpdateAccountRoleCommand.class)))
                .thenReturn(UpdateAccountRoleOutput.from(aAccount));

        final var request = MockMvcRequestBuilders.patch("/accounts/{id}/role", aAccountId)
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aAccountId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateAccountRoleCommand.class);

        Mockito.verify(updateAccountRoleUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aAccountId, actualCmd.id());
        Assertions.assertEquals(aRoleId, actualCmd.roleId());
    }

    @Test
    void givenAnInvalidAccountId_whenCallUpdateAccountRole_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var aRole = Role.newRole("User", "common user", RoleTypes.COMMON, true);
        final var aAccountId = "123";
        final var aRoleId = aRole.getId().getValue();

        final var expectedErrorMessage = "Account with id 123 was not found";

        final var aInput = new UpdateAccountRoleApiInput(aRoleId);

        Mockito.when(updateAccountRoleUseCase.execute(Mockito.any(UpdateAccountRoleCommand.class)))
                .thenThrow(NotFoundException.with(Account.class, aAccountId).get());

        final var request = MockMvcRequestBuilders.patch("/accounts/{id}/role", aAccountId)
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateAccountRoleCommand.class);

        Mockito.verify(updateAccountRoleUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aAccountId, actualCmd.id());
        Assertions.assertEquals(aRoleId, actualCmd.roleId());
    }

    @Test
    void givenAnInvalidRoleId_whenCallUpdateAccountRole_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "teste123A",
                Role.newRole("CEO", null, RoleTypes.EMPLOYEES, false)
        );
        final var aAccountId = aAccount.getId().getValue();
        final var aRoleId = "123";

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aInput = new UpdateAccountRoleApiInput(aRoleId);

        Mockito.when(updateAccountRoleUseCase.execute(Mockito.any(UpdateAccountRoleCommand.class)))
                .thenThrow(NotFoundException.with(Role.class, aRoleId).get());

        final var request = MockMvcRequestBuilders.patch("/accounts/{id}/role", aAccountId)
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateAccountRoleCommand.class);

        Mockito.verify(updateAccountRoleUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aAccountId, actualCmd.id());
        Assertions.assertEquals(aRoleId, actualCmd.roleId());
    }

    @Test
    void givenAValidParams_whenCallListAccounts_shouldReturnAccounts() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aAccount = Account.newAccount(
                "fulano",
                "teste",
                "fulano.teste@test.com",
                "12345677Ab*",
                aRole
        );

        final var aPage = 0;
        final var aPerPage = 1;
        final var aTerms = "ful";
        final var aSort = "firstName";
        final var aDirection = "asc";
        final var aTotalItems = 1;
        final var aTotalPage = 1;
        final var aItems = List.of(ListAccountsOutput.from(aAccount));

        Mockito.when(listAccountsUseCase.execute(Mockito.any(SearchQuery.class)))
                .thenReturn(new Pagination<>(
                        aPage,
                        aPerPage,
                        aTotalPage,
                        aTotalItems,
                        aItems
                ));

        final var request = MockMvcRequestBuilders.get("/accounts")
                .queryParam("page", String.valueOf(aPage))
                .queryParam("perPage", String.valueOf(aPerPage))
                .queryParam("sort", aSort)
                .queryParam("dir", aDirection)
                .queryParam("search", aTerms)
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", equalTo(aPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", equalTo(aPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", equalTo(aTotalPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalItems", equalTo(aTotalItems)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(aItems.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", equalTo(aAccount.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].first_name", equalTo(aAccount.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].last_name", equalTo(aAccount.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].email", equalTo(aAccount.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", equalTo(aAccount.getCreatedAt().toString())));

        Mockito.verify(listAccountsUseCase, Mockito.times(1)).execute(argThat(query ->
                Objects.equals(aPage, query.page()) &&
                        Objects.equals(aPerPage, query.perPage()) &&
                        Objects.equals(aSort, query.sort()) &&
                        Objects.equals(aDirection, query.direction()) &&
                        Objects.equals(aTerms, query.terms())
        ));
    }
}
