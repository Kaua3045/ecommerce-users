package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountOutput;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
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

import java.time.temporal.ChronoUnit;
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
    private CreateAccountMailUseCase createAccountMailUseCase;

    @MockBean
    private ConfirmAccountMailUseCase confirmAccountMailUseCase;

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

        Mockito.when(createAccountMailUseCase.execute(Mockito.any(CreateAccountMailCommand.class)))
                .thenReturn(Either.right(AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.ACCOUNT_CONFIRMATION,
                        Account.newAccount(
                                aFirstName,
                                aLastName,
                                aEmail,
                                aPassword
                        ),
                        InstantUtils.now().plus(1, ChronoUnit.HOURS)
                )));

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

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aId, cmd.accountId()) &&
                                Objects.equals(AccountMailType.ACCOUNT_CONFIRMATION, cmd.type()) &&
                                Objects.equals("Account Confirmation", cmd.subject()) &&
                                Objects.nonNull(cmd.expirestAt()) &&
                                Objects.nonNull(cmd.token())
        ));
    }

    @Test
    public void givenAnInvalidFirstName_whenCallCreateAccount_thenShouldReturnNotification() throws Exception {
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
    public void givenAnInvalidCommand_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final String aFirstName = null;
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        final var aInput = new CreateAccountApiInput(aFirstName, aLastName, aEmail, aPassword);

        Mockito.when(createAccountUseCase.execute(Mockito.any(CreateAccountCommand.class)))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

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
    public void givenAValidCommand_whenCallConfirmAccount_thenShouldReturneNoContent() throws Exception {
        // given
        final var aInput = RandomStringUtils.generateValue(36);

        Mockito.when(confirmAccountMailUseCase.execute(Mockito.any(ConfirmAccountMailCommand.class)))
                .thenReturn(Either.right(true));

        final var request = MockMvcRequestBuilders.patch("/accounts/confirm/{token}", aInput)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void givenAnInvalidTokenExpired_whenCallConfirmAccount_thenShouldReturneAnError() throws Exception {
        // given
        final var expectedErrorMessage = "Token expired";
        final var aInput = RandomStringUtils.generateValue(36);

        Mockito.when(confirmAccountMailUseCase.execute(Mockito.any(ConfirmAccountMailCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/confirm/{token}", aInput)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }
}
