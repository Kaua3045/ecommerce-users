package com.kaua.ecommerce.users.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.ControllerTest;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.role.create.CreateRoleOutput;
import com.kaua.ecommerce.users.application.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.application.role.remove.RemoveRolePermissionCommand;
import com.kaua.ecommerce.users.application.role.remove.RemoveRolePermissionUseCase;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdCommand;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdOutput;
import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdUseCase;
import com.kaua.ecommerce.users.application.role.retrieve.list.ListRolesOutput;
import com.kaua.ecommerce.users.application.role.retrieve.list.ListRolesUseCase;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleCommand;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleOutput;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleUseCase;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @MockBean
    private ListRolesUseCase listRolesUseCase;

    @MockBean
    private RemoveRolePermissionUseCase removeRolePermissionUseCase;

    @Test
    void givenAValidCommandWithDescriptionAndPermissions_whenCallCreateRole_thenShouldReturneAnRoleId() throws Exception {
        // given
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");
        final var aId = "123";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                                Objects.equals(aRoleType, cmd.roleType()) &&
                                Objects.equals(aIsDefault, cmd.isDefault()) &&
                                Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAValidCommandWithNullDescriptionAndNullPermissions_whenCallCreateRole_thenShouldReturneAnRoleId() throws Exception {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;
        final var aId = "123";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandExistingName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "Role already exists";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandExistingDefaultRole_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = true;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "Default role already exists";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNullName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandBlankName_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final String aName = "";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthLessThan3_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ce ";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthMoreThan50_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNullRoleType_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "'roleType' should not be null";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNotExistsRoleType_whenCallCreateRole_thenShouldReturnDomainException() throws Exception {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "not-exists";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var expectedErrorMessage = "RoleType not found, role types available: COMMON, EMPLOYEES";

        final var aInput = new CreateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAValidValuesWithDescriptionAndPermissions_whenCallUpdateRole_thenShouldReturnOkAndRoleId() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();
        final var aName = "Ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var aPermissions = Set.of("456", "789");

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
        Assertions.assertEquals(aIsDefault, actualCmd.isDefault());
        Assertions.assertEquals(aPermissions, actualCmd.permissions());
    }

    @Test
    void givenAValidValuesWithNullDescriptionAndNullPermissions_whenCallUpdateRole_thenShouldReturnOkAndRoleId() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();
        final var aName = "Ceo";
        final String aDescription = null;
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
        Assertions.assertEquals(aIsDefault, actualCmd.isDefault());
        Assertions.assertEquals(aPermissions, actualCmd.permissions());
    }

    @Test
    void givenAnInvalidCommandNotExistsRole_whenCallUpdateRole_thenShouldReturnNotFoundException() throws Exception {
        final var aId = "123";
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNullName_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandBlankName_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final var aName = "";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'name' should not be null or blank";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthLessThan3_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final var aName = "ce ";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNameLengthMoreThan50_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final var aName = RandomStringUtils.generateValue(51);
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'name' must be between 3 and 50 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandDescriptionLengthMoreThan255_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final var aName = "ceo";
        final var aDescription = RandomStringUtils.generateValue(256);
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'description' must be between 0 and 255 characters";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNullRoleType_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final String aRoleType = null;
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "'roleType' should not be null";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAnInvalidCommandNotExistsRoleType_whenCallUpdateRole_thenShouldReturnDomainException() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aId = aRole.getId().getValue();

        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "not-exists";
        final var aIsDefault = false;
        final Set<String> aPermissions = null;

        final var expectedErrorMessage = "RoleType not found, role types available: COMMON, EMPLOYEES";

        final var aInput = new UpdateRoleApiInput(aName, aDescription, aRoleType, aIsDefault, aPermissions);

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
                        Objects.equals(aRoleType, cmd.roleType()) &&
                        Objects.equals(aIsDefault, cmd.isDefault()) &&
                        Objects.equals(aPermissions, cmd.permissions())
        ));
    }

    @Test
    void givenAValidCommandWithRoleId_whenCallDeleteRole_thenShouldReturnOk() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, false);
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
        final var aIsDefault = false;
        final var aRole = Role.newRole(aName, aDescription, aRoleType, aIsDefault);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_default", equalTo(aIsDefault)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", equalTo(aRole.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", equalTo(aRole.getUpdatedAt().toString())));

        Mockito.verify(getRoleByIdUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id())));
    }

    @Test
    void givenAnInvalidId_whenCallGetRole_thenShouldThrowNotFoundException() throws Exception {
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

    @Test
    void givenAValidParams_whenCallListRoles_shouldReturnRoles() throws Exception {
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var aPage = 0;
        final var aPerPage = 1;
        final var aTerms = "user";
        final var aSort = "name";
        final var aDirection = "asc";
        final var aTotalItems = 1;
        final var aTotalPage = 1;
        final var aItems = List.of(ListRolesOutput.from(aRole));

        Mockito.when(listRolesUseCase.execute(Mockito.any(SearchQuery.class)))
                .thenReturn(new Pagination<>(
                        aPage,
                        aPerPage,
                        aTotalPage,
                        aTotalItems,
                        aItems
                ));

        final var request = MockMvcRequestBuilders.get("/roles")
                .queryParam("page", String.valueOf(aPage))
                .queryParam("perPage", String.valueOf(aPerPage))
                .queryParam("sort", aSort)
                .queryParam("dir", aDirection)
                .queryParam("search", aTerms)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", equalTo(aRole.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo(aRole.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", equalTo(aRole.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].role_type", equalTo(aRole.getRoleType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_default", equalTo(aRole.isDefault())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", equalTo(aRole.getCreatedAt().toString())));

        Mockito.verify(listRolesUseCase, Mockito.times(1)).execute(argThat(query ->
                Objects.equals(aPage, query.page()) &&
                        Objects.equals(aPerPage, query.perPage()) &&
                        Objects.equals(aSort, query.sort()) &&
                        Objects.equals(aDirection, query.direction()) &&
                        Objects.equals(aTerms, query.terms())
        ));
    }

    @Test
    void givenAValidIds_whenCallRemoveRolePermission_thenShouldReturnOk() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);
        final var aRolePermission = RolePermission.newRolePermission(PermissionID.unique(), "teste");
        aRole.addPermissions(Set.of(aRolePermission));

        final var aId = aRole.getId().getValue();
        final var aRolePermissionId = aRolePermission.getPermissionID().getValue();

        Mockito.doNothing()
                .when(removeRolePermissionUseCase)
                .execute(Mockito.any(RemoveRolePermissionCommand.class));

        final var request = MockMvcRequestBuilders.delete("/roles/{roleId}/permissions/{permissionId}", aId, aRolePermissionId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        final var cmdCaptor = ArgumentCaptor.forClass(RemoveRolePermissionCommand.class);

        Mockito.verify(removeRolePermissionUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aRolePermissionId, actualCmd.permissionId());
    }

    @Test
    void givenAValidRoleIdAndNotExistsPermissionId_whenCallRemoveRolePermission_thenShouldReturnOk() throws Exception {
        // given
        final var aRole = Role.newRole("User", "Common User", RoleTypes.COMMON, true);

        final var aId = aRole.getId().getValue();
        final var aRolePermissionId = "123";

        Mockito.doNothing()
                .when(removeRolePermissionUseCase)
                .execute(Mockito.any(RemoveRolePermissionCommand.class));

        final var request = MockMvcRequestBuilders.delete("/roles/{roleId}/permissions/{permissionId}", aId, aRolePermissionId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        final var cmdCaptor = ArgumentCaptor.forClass(RemoveRolePermissionCommand.class);

        Mockito.verify(removeRolePermissionUseCase, Mockito.times(1)).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(aId, actualCmd.id());
        Assertions.assertEquals(aRolePermissionId, actualCmd.permissionId());
    }

    @Test
    void givenAnInvalidRoleId_whenCallRemoveRolePermission_thenShouldReturnNotFoundException() throws Exception {
        final var aRolePermission = RolePermission.newRolePermission(PermissionID.unique(), "teste");

        final var aId = "123";
        final var aRolePermissionId = aRolePermission.getPermissionID().getValue();

        final var expectedErrorMessage = "Role with id 123 was not found";

        Mockito.doThrow(NotFoundException.with(Role.class, aId))
                .when(removeRolePermissionUseCase)
                .execute(Mockito.any(RemoveRolePermissionCommand.class));

        final var request = MockMvcRequestBuilders.delete("/roles/{roleId}/permissions/{permissionId}", aId, aRolePermissionId)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(removeRolePermissionUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(aId, cmd.id()) &&
                        Objects.equals(aRolePermissionId, cmd.permissionId())
        ));
    }
}
