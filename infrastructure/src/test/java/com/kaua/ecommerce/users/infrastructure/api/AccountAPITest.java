package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountOutput;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void givenAValidCommand_whenCallCreateAccount_thenShouldReturneAnAccountId() throws Exception {
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
    public void givenAnInvalidCommandFirstName_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final String aFirstName = null;
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandFirstNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "te";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandFirstNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = RandomStringUtils.generateValue(256);
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandLastName_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandLastNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "Te";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandLastNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = RandomStringUtils.generateValue(256);
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandEmail_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "testes";
        final var aEmail = "";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandPassword_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "Teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var expectedErrorMessage = "'password' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandPasswordLengthLessThan8_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "Testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandPasswordLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256).toLowerCase();
        final var expectedErrorMessage = "'password' must be between 3 and 255 characters";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
    public void givenAnInvalidCommandPasswordNotContainsOneUppercaseAndLowercaseLetter_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aFirstName = "teste";
        final var aLastName = "testes";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts")
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
}
