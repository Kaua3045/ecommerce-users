package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.role.create.CreateRoleOutput;
import com.kaua.ecommerce.users.application.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdCommand;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdOutput;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdUseCase;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleCommand;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleOutput;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleUseCase;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.roles.models.UpdateRoleApiInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

    @MockBean
    private UpdateRoleUseCase updateRoleUseCase;

    @MockBean
    private DeleteRoleUseCase deleteRoleUseCase;

    @MockBean
    private GetRoleByIdUseCase getRoleByIdUseCase;

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
        final String aName = "";
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

    @Test
    void givenAValidValuesWithDescription_whenCallUpdateRole_thenShouldReturnOkAndRoleId() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();
        final var aName = "Ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.right(UpdateRoleOutput.from(aRole)));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateRoleCommand.class);

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aName, actualCmd.name());
        Assertions.assertEquals(aDescription, actualCmd.description());
        Assertions.assertEquals(aRoleType, actualCmd.roleType());
    }

    @Test
    void givenAValidValuesWithNullDescription_whenCallUpdateRole_thenShouldReturnOkAndRoleId() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();
        final var aName = "Ceo";
        final String aDescription = null;
        final var aRoleType = "employees";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.right(UpdateRoleOutput.from(aRole)));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateRoleCommand.class);

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aName, actualCmd.name());
        Assertions.assertEquals(aDescription, actualCmd.description());
        Assertions.assertEquals(aRoleType, actualCmd.roleType());
    }

    @Test
    void givenAnInvalidCommandNotExistsRole_whenCallUpdateRole_thenShouldReturnNotFoundException() throws Exception {
        final var aId = "123";
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenThrow(NotFoundException.with(Role.class, aId));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNullName_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandBlankName_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var aName = "";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthLessThan3_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var aName = "ce ";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthMoreThan50_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNullRoleType_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var expectedErrorMessage = "'roleType' should not be null";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAnInvalidCommandNotExistsRoleType_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "not-exists";
        final var expectedErrorMessage = "RoleType not found, role types available: COMMON, EMPLOYEES";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType);

        Mockito.when(updateRoleUseCase.execute(Mockito.any(UpdateRoleCommand.class)))
                .thenReturn(Either.left(NotificationHandler.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.patch("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Mockito.verify(updateRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aName, cmd.name()) &&
                        Objects.equals(aDescription, cmd.description()) &&
                        Objects.equals(aRoleType, cmd.roleType())
        ));
    }

    @Test
    void givenAValidCommandWithRoleId_whenCallDeleteRole_thenShouldReturnOk() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON);
        final var aId = aRole.getId().getValue();

        final var request = MockMvcRequestBuilders.delete("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deleteRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidRoleId_whenCallDeleteRole_thenShouldReturnOk() throws Exception {
        // given
        final var aId = "invalid";

        final var request = MockMvcRequestBuilders.delete("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(deleteRoleUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAValidCommand_whenCallGetRole_thenShouldReturneAnRole() throws Exception {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aRole = Role.newRole(aName, aDescription, aRoleType);
        final var aId = aRole.getId().getValue();

        Mockito.when(getRoleByIdUseCase.execute(Mockito.any(GetRoleByIdCommand.class)))
                .thenReturn(GetRoleByIdOutput.from(aRole));

        final var request = MockMvcRequestBuilders.get("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(aId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(aName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(aDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role_type", equalTo(aRoleType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", equalTo(aRole.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", equalTo(aRole.getUpdatedAt().toString())));

        Mockito.verify(getRoleByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidId_whenCallGetAccount_thenShouldThrowNotFoundException() throws Exception {
        // given
        final var expectedErrorMessage = "Role with id 123 was not found";
        final var aId = "123";

        Mockito.when(getRoleByIdUseCase.execute(Mockito.any(GetRoleByIdCommand.class)))
                .thenThrow(NotFoundException.with(Role.class, aId));

        final var request = MockMvcRequestBuilders.get("/roles/{id}", aId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(getRoleByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }
}
