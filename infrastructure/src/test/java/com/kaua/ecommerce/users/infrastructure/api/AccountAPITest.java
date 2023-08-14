package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountOutput;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;

@ControllerTest(controllers = AccountAPI.class)
public class AccountAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateAccountUseCase createAccountUseCase;

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
                .thenReturn(Either.right(CreateAccountOutput.from(AccountID.from(aId))));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(aId)));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aFirstName, cmd.firstName()) &&
                                Objects.equals(aLastName, cmd.lastName()) &&
                                Objects.equals(aEmail, cmd.email()) &&
                                Objects.equals(aPassword, cmd.password())
        ));
    }
}
