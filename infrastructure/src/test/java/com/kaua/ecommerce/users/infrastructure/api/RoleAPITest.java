package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.role.create.CreateRoleOutput;
import com.kaua.ecommerce.users.application.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
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

@ControllerTest(controllers = RoleAPI.class)
public class RoleAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateRoleUseCase createRoleUseCase;

    @Test
    void givenAValidCommandWithDescription_whenCallCreateRole_thenShouldReturneAnRoleId() throws Exception {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aId = "123";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.right(CreateRoleOutput.from(aId)));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                        Objects.equals(aName, cmd.name()) &&
                                Objects.equals(aDescription, cmd.description()) &&
                                Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallCreateRole_thenShouldReturneAnRoleId() throws Exception {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aId = "123";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.right(CreateRoleOutput.from(aId)));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandExistingName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var expectedErrorMessage = "Role already exists";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNullName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandBlankName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthLessThan3_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ce ";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthMoreThan50_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNullRoleType_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var expectedErrorMessage = "'roleType' should not be null";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNotExistsRoleType_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "not-exists";
        final var expectedErrorMessage = "RoleType not found, role types available: COMMON, EMPLOYEES";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(createRoleUseCase.execute(Mockito.any(CreateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(createRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }
}
