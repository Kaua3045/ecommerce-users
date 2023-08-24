package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.update.password.RequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordCommand;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;

@ControllerTest(controllers = AccountMailAPI.class)
public class AccountMailAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateAccountMailUseCase createAccountMailUseCase;

    @MockBean
    private ConfirmAccountMailUseCase confirmAccountMailUseCase;

    @MockBean
    private RequestResetPasswordUseCase requestResetPasswordUseCase;

    @MockBean
    private ResetPasswordUseCase resetPasswordUseCase;

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

    @Test
    public void givenAValidCommand_whenCallCreateAccountMail_thenShouldReturneAnAccountMailId() throws Exception {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aId = "123";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateAccountMailOutput.from(aId)));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount.getId().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandToken_whenCallCreateAccountMail_thenShouldReturnDomainException() throws Exception {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567Ab"
        );
        final var expectedErrorMessage = "'token' should not be null or blank";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount.getId().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandAccountMailType_whenCallCreateAccountMail_thenShouldReturnDomainException() throws Exception {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567Ab"
        );
        final var expectedErrorMessage = "'type' should not be null";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount.getId().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandExpiresAt_whenCallCreateAccountMail_thenShouldReturnDomainException() throws Exception {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567Ab"
        );
        final var expectedErrorMessage = "'expiresAt' should not be null";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount.getId().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandAccountId_whenCallCreateAccountMail_thenShouldReturnDomainException() throws Exception {
        final var aAccount = "invalid";
        final var expectedErrorMessage = "'accountId' should not be null";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandExpiresAtBeforeNow_whenCallCreateAccountMail_thenShouldReturnDomainException() throws Exception {
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567Ab"
        );
        final var expectedErrorMessage = "'expiresAt' should not be before now";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount.getId().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAnInvalidCommandAccountNotExists_whenCallCreateAccountMail_thenShouldReturnNotFound() throws Exception {
        final var aAccount = "8d24aaa1-c759-4b2a-82d1-51e592f14585";
        final var expectedErrorMessage = "Account with id 8d24aaa1-c759-4b2a-82d1-51e592f14585 was not found";

        Mockito.when(createAccountMailUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Account.class, aAccount));

        final var request = MockMvcRequestBuilders.post("/accounts/confirm/{accountId}", aAccount)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(createAccountMailUseCase, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenCallRequestResetPassword_thenShouldReturnNotContent() throws Exception {
        // given
        final var aEmail = "teste@teste.com";

        final var aInput = new RequestResetPasswordApiInput(aEmail);

        final var request = MockMvcRequestBuilders.post("/accounts/request-reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(requestResetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aEmail, cmd.email())
        ));
    }

    @Test
    public void givenAInvalidCommandEmailNotFound_whenCallRequestResetPassword_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var aEmail = "teste@teste.com";

        final var aInput = new RequestResetPasswordApiInput(aEmail);

        Mockito.doThrow(NotFoundException.class)
                .when(requestResetPasswordUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.post("/accounts/request-reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(requestResetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aEmail, cmd.email())
        ));
    }

    @Test
    public void givenAValidCommand_whenCallResetPassword_thenShouldReturneNoContent() throws Exception {
        // given
        final var aToken = RandomStringUtils.generateValue(36);
        final var aPassword = "1234567Ab*";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.right(true));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void givenAnInvalidTokenExpired_whenCallResetPassword_thenShouldReturneAnError() throws Exception {
        // given
        final var expectedErrorMessage = "Token expired";
        final var aToken = RandomStringUtils.generateValue(36);
        final var aPassword = "1234567Ab*";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnInvalidCommandPassword_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aToken = RandomStringUtils.generateValue(36);
        final String aPassword = null;
        final var expectedErrorMessage = "'password' should not be null or blank";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(resetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aToken, cmd.token()) &&
                        Objects.isNull(cmd.newPassword())
        ));
    }

    @Test
    public void givenAnInvalidCommandPasswordLengthLessThan8_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aPassword = "123";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(resetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aToken, cmd.token()) &&
                        Objects.equals(aPassword, cmd.newPassword())
        ));
    }

    @Test
    public void givenAnInvalidCommandPasswordLengthMoreThan255_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aPassword = RandomStringUtils.generateValue(256).toLowerCase();
        final var expectedErrorMessage = "'password' must be between 3 and 255 characters";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(resetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aToken, cmd.token()) &&
                        Objects.equals(aPassword, cmd.newPassword())
        ));
    }

    @Test
    public void givenAnInvalidCommandPasswordNotContainsOneUppercaseAndLowercaseLetter_whenCallCreateAccount_thenShouldReturnDomainException() throws Exception {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aPassword = "12345678";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        final var aInput = new ResetPasswordApiInput(aPassword);

        Mockito.when(resetPasswordUseCase.execute(Mockito.any(ResetPasswordCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/accounts/reset-password/{token}", aToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(resetPasswordUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aToken, cmd.token()) &&
                        Objects.equals(aPassword, cmd.newPassword())
        ));
    }
}
